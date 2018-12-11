package org.linlinjava.litemall.wx.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.OrderHandleOption;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.SmsUtil;
import org.linlinjava.litemall.wx.util.Cache;
import org.linlinjava.litemall.wx.util.CacheManager;
import org.linlinjava.litemall.wx.util.weixin.OpenIdUtil;
import org.linlinjava.litemall.wx.util.weixin.WXBizDataCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;

import net.sf.ezmorph.object.BigDecimalMorpher;
import net.sf.json.JSONObject;

/*
 * 订单设计
 *
 * 订单状态：
 * 101 订单生成，未支付；102，订单生产，但是未支付就取消；
 * 201 支付完成，商家未发货；202，订单生产，已付款未发货，但是退款取消；
 * 301 商家发货，用户未确认；
 * 401 用户确认收货，订单结束； 402 用户没有确认收货，但是快递反馈已收获后，超过一定时间，系统自动确认收货，订单结束。
 *
 * 当101用户未付款时，此时用户可以进行的操作是取消订单，或者付款操作
 * 当201支付完成而商家未发货时，此时用户可以取消订单并申请退款
 * 当301商家已发货时，此时用户可以有确认收货的操作
 * 当401用户确认收货以后，此时用户可以进行的操作是删除订单，评价商品，或者再次购买
 * 当402系统自动确认收货以后，此时用户可以删除订单，评价商品，或者再次购买
 *
 * 目前不支持订单退货和售后服务
 *
 */
@RestController
@RequestMapping("/wx/order")
public class WxOrderController {
	private final Log logger = LogFactory.getLog(WxOrderController.class);

	@Autowired
	private LitemallOrderService orderService;
	@Autowired
	private LitemallOrderGoodsService orderGoodsService;
	@Autowired
	private LitemallAddressService addressService;
	@Autowired
	private LitemallCartService cartService;
	@Autowired
	private LitemallRegionService regionService;
	@Autowired
	private LitemallProductService productService;

	@Autowired
	private LitemallUserService userService;
	@Autowired
	private WxPayService wxService;
	
	@Autowired
	private LitemallDiscountService litemallDiscountService;
	
	@Autowired
	private LitemallGoodsService  litemallGoodsService;

	@Autowired
	private LitemallDistributionProfitService litemallDistributionProfitService;	
	
    @Autowired
    private LitemallReserveService litemallReserveService;
    
	@Autowired
	private LitemallRechargeService litemallRechargeService;
	
	@Autowired
	private WmwImgMedicalAnalyseService wmwimgMedicalAanalyseService;

	@Autowired
	private LitemallStoreService litemallStoreService;
	
	
	
	public WxOrderController() {
	}


	private String detailedAddress(LitemallAddress zmallAddress) {
		Integer provinceId = zmallAddress.getProvinceId();
		Integer cityId = zmallAddress.getCityId();
		Integer areaId = zmallAddress.getAreaId();
		String provinceName = regionService.findById(provinceId).getName();
		String cityName = regionService.findById(cityId).getName();
		String areaName = regionService.findById(areaId).getName();
		String fullRegion = provinceName + " " + cityName + " " + areaName;
		return fullRegion + " " + zmallAddress.getAddress();
	}

	/**
	 * 订单列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param showType
	 *            订单信息 0， 全部订单 1，待付款 2，待发货 3，待收货 4，待评价
	 * @param page
	 *            分页页数
	 * @param size
	 *            分页大小
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功', data: { data: xxx , count: xxx }
	 *         } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@RequestMapping("list")
	public Object list(@RequestBody String body, HttpServletRequest request) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		Integer showType = JacksonUtil.parseInteger(body, "showType");
		String flag = JacksonUtil.parseString(body, "flag");

		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (showType == null) {
			showType = 0;
		}

		List<Short> orderStatus = OrderUtil.orderStatus(showType);
		List<Short> orderGoodsStatus = OrderUtil.orderGoodsStatus(showType);
		List<LitemallOrder> orderList = orderService.queryByOrderStatus(userId, orderStatus);
		int count = orderService.countByOrderStatus(userId, orderStatus);

		List<Map<String, Object>> orderVoList = new ArrayList<>(orderList.size());
		for (LitemallOrder order : orderList) {
			Map<String, Object> orderVo = new HashMap<>();
			orderVo.put("id", order.getId());
			orderVo.put("orderSn", order.getOrderSn());
			orderVo.put("shipSn", order.getShipSn());
			orderVo.put("actualPrice", order.getActualPrice());
			orderVo.put("orderStatusText", OrderUtil.orderStatusText(order));
			orderVo.put("handleOption", OrderUtil.build(order));

			List<LitemallOrderGoods> orderGoodsList = orderGoodsService.queryByOidAndStatus(order.getId(),orderGoodsStatus);
			List<Map<String, Object>> orderAllGoodsVoList = new ArrayList<>(orderGoodsList.size());

			// 商品订单和服务订单标志
			List<String> flagList = new ArrayList<String>();  
			for (LitemallOrderGoods orderGoods : orderGoodsList) {
				Map<String, Object> orderGoodsVo = new HashMap<>();
				orderGoodsVo.put("id", orderGoods.getId());
				orderGoodsVo.put("goodsSn", orderGoods.getGoodsSn());
				orderGoodsVo.put("storeId", orderGoods.getStoreId());
				orderGoodsVo.put("goodsName", orderGoods.getGoodsName());
				orderGoodsVo.put("number", orderGoods.getNumber());
				orderGoodsVo.put("treatmentNum", orderGoods.getTreatmentNum());
				orderGoodsVo.put("retailPrice", orderGoods.getRetailPrice());
				orderGoodsVo.put("picUrl", orderGoods.getPicUrl());
				List<LitemallReserve>  litemallReserveList = litemallReserveService.selectFinishByOrderGoodsId(orderGoods.getId());
				orderGoodsVo.put("litemallReserveList", litemallReserveList);
				orderGoodsVo.put("goodsId", orderGoods.getGoodsId());
				
				LitemallGoods litemallGoods = litemallGoodsService.findById(orderGoods.getGoodsId());
				orderGoodsVo.put("goodsTreatmentNum", litemallGoods.getTreatmentNum()*orderGoods.getNumber());
				
				flagList.add(flag);				
				if(!flag.equals("0")) {
					if(orderGoods.getFlag().equals(flag)) {
						if(orderGoods.getFlag().equals("1")) {
							//商品订单
							orderAllGoodsVoList.add(orderGoodsVo);
						}else {
							//服务订单
							if(showType == 3) {
								//已预约列表
								if(litemallReserveList.size()>0) {
									orderAllGoodsVoList.add(orderGoodsVo);
								}
							}else {
								//预约列表							
								orderAllGoodsVoList.add(orderGoodsVo);
							}
						}
					}
				}else {
					orderAllGoodsVoList.add(orderGoodsVo);
				}
			}
			if(orderAllGoodsVoList.size()>0) {
				orderVo.put("goodsList", orderAllGoodsVoList);
				orderVoList.add(orderVo);
			}			
		}		
		Map<String, Object> result = new HashMap<>();
		result.put("count", count);
		result.put("data", orderVoList);

		return ResponseUtil.ok(result);
	}

	/**
	 * 订单详情
	 *
	 * @param userId
	 *            用户ID
	 * @param orderId
	 *            订单信息
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功', data: { orderInfo: xxx ,
	 *         orderGoods: xxx } } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@RequestMapping("detail")
	public Object detail(@RequestBody String body, HttpServletRequest request) {

		Integer userId = JacksonUtil.parseInteger(body, "userId");
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		Integer showType = JacksonUtil.parseInteger(body, "showType");
		Integer reserveId = JacksonUtil.parseInteger(body, "reserveId");
		String flag = JacksonUtil.parseString(body, "flag");

		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (orderId == null) {
			return ResponseUtil.fail402();
		}

		// 订单信息
		LitemallOrder order = orderService.findById(orderId);
		if (null == order) {
			return ResponseUtil.fail(403, "订单不存在");
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.fail(403, "不是当前用户的订单");
		}

		/**
		 * 当订单处于未支付时 查需要询支付状态
		 */
		if (order.getActualPrice().compareTo(new BigDecimal(0.00)) != 0 && order.getOrderStatus().intValue() == 101 && order.getPayType() != null && order.getPayType() == 1) {
			WxPayOrderQueryResult wxPayOrderQueryResult = null;
			try {
				wxPayOrderQueryResult = wxService.queryOrder(null, order.getOrderSn());
			} catch (WxPayException e) {
				e.printStackTrace();
				return ResponseUtil.fail(403, "查询失败");
			}
			if (wxPayOrderQueryResult.getTradeState().equals("SUCCESS")) {
				// 支付成功,且订单处于未支付状态
				LitemallOrder updateOrder = new LitemallOrder();
				updateOrder.setId(order.getId());
				// 微信的订单号
				updateOrder.setOrderStatus(OrderUtil.STATUS_PAY);
				updateOrder.setPayId(wxPayOrderQueryResult.getTransactionId());
				orderService.update(updateOrder);
			}
		}
		order = orderService.findById(orderId);
		
		List<Short> orderStatus = OrderUtil.orderStatus(showType);
		List<LitemallOrderGoods> orderGoodsList = orderGoodsService.queryByOidAndStatus(order.getId(), orderStatus);


		
		order.setOrderFlag(flag);
		Map<String, Object> orderVo = new HashMap<String, Object>();
		orderVo.put("id", order.getId());
		orderVo.put("orderSn", order.getOrderSn());
		orderVo.put("addTime", LocalDate.now());
		orderVo.put("consignee", order.getConsignee());
		orderVo.put("mobile", order.getMobile());
		orderVo.put("address", order.getAddress());
		orderVo.put("goodsPrice", order.getGoodsPrice());
		orderVo.put("freightPrice", order.getFreightPrice());
		orderVo.put("actualPrice", order.getActualPrice());
		orderVo.put("payId", order.getPayId());
		orderVo.put("orderStatusText", OrderUtil.orderStatusText(order));
		orderVo.put("handleOption", OrderUtil.detailBuild(order,orderGoodsList));
		orderVo.put("getStoreId",order.getGetStoreId());
		if(order.getGetStoreId() != null && order.getGetStoreId() != ""){
			LitemallStore store=litemallStoreService.selectStoreById(order.getGetStoreId());
			orderVo.put("storeName",store.getStoreName());
		}


		if (showType == null) {
			showType = 0;
		}

		List<Map<String, Object>> orderGoodsVoList = new ArrayList<>(orderGoodsList.size());
		for (LitemallOrderGoods orderGoods : orderGoodsList) {
			if (!flag.equals(0) && orderGoods.getFlag().equals(flag)) {

				Map<String, Object> orderGoodsVo = new HashMap<>();
				orderGoodsVo.put("id", orderGoods.getId());
				orderGoodsVo.put("goodsName", orderGoods.getGoodsName());
				orderGoodsVo.put("number", orderGoods.getNumber());
				orderGoodsVo.put("picUrl", orderGoods.getPicUrl());
				orderGoodsVo.put("reserveFlag", orderGoods.getReserveFlag());
				orderGoodsVo.put("orderStatus", orderGoods.getOrderStatus());
				orderGoodsVo.put("goodsId", orderGoods.getGoodsId());
				orderGoodsVo.put("retailPrice",orderGoods.getRetailPrice());
				orderGoodsVo.put("goodsSpecifitionValues", orderGoods.getGoodsSpecificationValues());
				orderGoodsVoList.add(orderGoodsVo);
			}
		}
		LitemallReserve litemallReserve = null;
		if(reserveId != null) {
			litemallReserve = litemallReserveService.selectById(reserveId);
		}
		
		Map<String, Object> result = new HashMap<>();
		result.put("orderInfo", orderVo);
		result.put("litemallReserve", litemallReserve);
		result.put("orderGoods", orderGoodsVoList);
		return ResponseUtil.ok(result);
	}

	/**
	 * 提交订单 1. 根据购物车ID、地址ID、优惠券ID，创建订单表项 2. 购物车清空 3. TODO 优惠券设置已用 4. 商品货品数量减少
	 *
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ cartId：xxx, addressId: xxx, couponId: xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功', data: { orderInfo: xxx } } 失败则 {
	 *         errno: XXX, errmsg: XXX }
	 */
	@RequestMapping("submit")
	public Object submit(@RequestBody String body) {
		if (body == null) {
			return ResponseUtil.badArgument();
		}
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer cartId = JacksonUtil.parseInteger(body, "cartId");
		Integer addressId = JacksonUtil.parseInteger(body, "addressId");
		Integer couponId = JacksonUtil.parseInteger(body, "couponId");
		Short payType = JacksonUtil.parseShort(body, "payType");
		Integer storeId = JacksonUtil.parseInteger(body, "storeId");

		//增加参数
		Integer pageFlag = JacksonUtil.parseInteger(body, "pageFlag");
		Integer radioFlag = JacksonUtil.parseInteger(body, "radioFlag");
		Integer storeIndex = JacksonUtil.parseInteger(body, "storeIndex");
		List<Integer> storeIds = JacksonUtil.parseIntegerList(body, "storeIds");
		//pageFlag为1则是实物商品，需要地址或者门店自取；为2则是服务商品

		//radioFlag为1则是地址选取，为2则是门店自取

		if(pageFlag == 1 && radioFlag  == 1){
			if (cartId == null  || couponId == null || addressId ==null) {
				return ResponseUtil.badArgument();
			}
		}else if(pageFlag == 1 && radioFlag  == 2){
			if(cartId == null  || couponId == null || storeIds ==null){
				return ResponseUtil.badArgument();
			}
		}





		// 获取可用的优惠券信息
		// 使用优惠券减免的金额
		BigDecimal couponPrice = new BigDecimal(0.00);
		if (couponId != 0) {
			LitemallDiscount litemallDiscount = litemallDiscountService.selectById(couponId);
			couponPrice = new BigDecimal(litemallDiscount.getDiscountsPrice());
		}

		// 货品价格
		List<LitemallCart> checkedGoodsList = null;
		if (cartId.equals(0)) {
			checkedGoodsList = cartService.queryByUidAndChecked(userId);
		} else {
			LitemallCart cart = cartService.findById(cartId);
			checkedGoodsList = new ArrayList<>(1);
			checkedGoodsList.add(cart);
		}
		if (checkedGoodsList.size() == 0) {
			return ResponseUtil.badArgumentValue();
		}
		BigDecimal checkedGoodsPrice = new BigDecimal(0.00);
		for (LitemallCart checkGoods : checkedGoodsList) {
			checkedGoodsPrice = checkedGoodsPrice
					.add(checkGoods.getRetailPrice().multiply(new BigDecimal(checkGoods.getNumber())));
		}

		// 根据订单商品总价计算运费，满88则免运费，否则8元；
		BigDecimal freightPrice = new BigDecimal(0.00);
		// if(checkedGoodsPrice.compareTo(new BigDecimal(88.00)) == -1){
		// freightPrice = new BigDecimal(8.00);
		// }

		// 可以使用的其他钱，例如用户积分
		BigDecimal integralPrice = new BigDecimal(0.00);

		// 订单费用
		BigDecimal orderTotalPrice = checkedGoodsPrice.add(freightPrice);
		BigDecimal actualPrice = orderTotalPrice.subtract(integralPrice).subtract(couponPrice);

		// 添加订单表
		LitemallOrder order = new LitemallOrder();

		if(pageFlag == 1 && radioFlag  == 1){
			//此处为实物商品的地址选取
			// 收货地址
			LitemallAddress checkedAddress = addressService.findById(addressId);
			order.setConsignee(checkedAddress.getName());
			order.setMobile(checkedAddress.getMobile());
			String detailedAddress = detailedAddress(checkedAddress);
			order.setAddress(detailedAddress);
		}else if(pageFlag == 1 && radioFlag  == 2){
			//此处为实物商品的门店自取
			order.setGetStoreId(String.valueOf(storeIds.get(storeIndex)));
		}else if(pageFlag == 2){
			//此处为服务商品
		}
		order.setUserId(userId);
		order.setOrderSn(orderService.generateOrderSn(userId));
		order.setAddTime(LocalDateTime.now());
		order.setOrderStatus(OrderUtil.STATUS_CREATE);
		order.setGoodsPrice(checkedGoodsPrice);
		order.setFreightPrice(freightPrice);
		order.setCouponPrice(couponPrice);
		order.setIntegralPrice(integralPrice);
		order.setOrderPrice(orderTotalPrice);
		order.setActualPrice(actualPrice);
		order.setPayType(payType);
		if (couponId != 0) {
			order.setCouponId(couponId);
		}
		orderService.add(order);

		// 添加订单商品表
		for (LitemallCart cartGoods : checkedGoodsList) {

			// 商品价格
			BigDecimal goodsPrice = cartGoods.getRetailPrice().multiply(new BigDecimal(cartGoods.getNumber()));

			// 优惠价格 积分+优惠券
			BigDecimal totalIntegralPrice = couponPrice.add(integralPrice);

			// 商品实际支付价格 商品售价*商品数量 - 优惠金额百分比
			BigDecimal goodActualPrice = goodsPrice.subtract(
					totalIntegralPrice.multiply(goodsPrice.divide(checkedGoodsPrice, 3, BigDecimal.ROUND_HALF_EVEN)));

			LitemallOrderGoods orderGoods = new LitemallOrderGoods();
			
			// 查询商品需要判断商品  的flag 是否为 服务 
			
			LitemallGoods litemallGoods = litemallGoodsService.findById(cartGoods.getGoodsId());
			
			if("2".equals(litemallGoods.getFlag())) {
				orderGoods.setStoreId(storeId);
				System.out.println(litemallGoods.getFlag());
			} 
			
			orderGoods.setOrderId(order.getId());
			orderGoods.setGoodsId(cartGoods.getGoodsId());
			orderGoods.setGoodsSn(cartGoods.getGoodsSn());
			orderGoods.setTreatmentNum(litemallGoods.getTreatmentNum()*cartGoods.getNumber());
			orderGoods.setProductId(cartGoods.getProductId());
			orderGoods.setGoodsName(cartGoods.getGoodsName());
			orderGoods.setPicUrl(cartGoods.getPicUrl());
			orderGoods.setRetailPrice(cartGoods.getRetailPrice());
			orderGoods.setActualPrice(goodActualPrice);
			orderGoods.setOrderStatus(OrderUtil.STATUS_CREATE);
			orderGoods.setNumber(cartGoods.getNumber());
			orderGoods.setGoodsSpecificationIds(cartGoods.getGoodsSpecificationIds());
			orderGoods.setGoodsSpecificationValues(cartGoods.getGoodsSpecificationValues());
			orderGoodsService.add(orderGoods);
		}

		// 删除购物车里面的商品信息
		for (LitemallCart checkGoods : checkedGoodsList) {
			cartService.clearGoodsByGoodsId(checkGoods.getGoodsId());
		}

		// 商品货品数量减少
		for (LitemallCart checkGoods : checkedGoodsList) {
			checkedGoodsPrice = checkedGoodsPrice
					.add(checkGoods.getRetailPrice().multiply(new BigDecimal(checkGoods.getNumber())));
			Integer productId = checkGoods.getProductId();
			LitemallProduct product = productService.findById(productId);
			if (product == null) {
				return ResponseUtil.badArgumentValue();
			}

			Integer remainNumber = product.getGoodsNumber() - checkGoods.getNumber();
			if (remainNumber < 0) {
				return ResponseUtil.badArgumentValue();
			}
			product.setGoodsNumber(remainNumber);
			productService.updateById(product);
		}

		// 更改优惠券状态为已使用
		if (couponId != 0) {
			LitemallDiscount litemallDiscount = litemallDiscountService.selectById(couponId);
			litemallDiscount.setIsUse(1);
			litemallDiscountService.updateById(litemallDiscount);
		}

		Map<String, Object> data = new HashMap<>();
		data.put("orderInfo", order);
		return ResponseUtil.ok(data);
	}

	/**
	 * 取消订单 1. 检测当前订单是否能够取消 2. 设置订单取消状态 3. 商品货品数量增加
	 * 
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@PostMapping("cancel")
	public Object cancel(@RequestBody String body) {

		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		if (orderId == null) {
			return ResponseUtil.badArgument();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.badArgumentValue();
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}

		// 检测是否能够取消
		OrderHandleOption handleOption = OrderUtil.build(order);
		if (!handleOption.isCancel()) {
			return ResponseUtil.fail(403, "订单不能取消");
		}

		// 设置订单已取消状态
		order.setOrderStatus(OrderUtil.STATUS_CANCEL);
		orderService.update(order);

		// 商品货品数量增加
		List<LitemallOrderGoods> orderGoodsList = orderGoodsService.queryByOid(orderId);
		for (LitemallOrderGoods orderGoods : orderGoodsList) {
			Integer productId = orderGoods.getProductId();
			LitemallProduct product = productService.findById(productId);
			if (product == null) {
				return ResponseUtil.badArgumentValue();
			}
			Integer number = product.getGoodsNumber() + orderGoods.getNumber();
			product.setGoodsNumber(number);
			productService.updateById(product);
		}
		
		// 更改优惠券状态为未使用
		if (order.getCouponId() != null) {
			LitemallDiscount litemallDiscount = litemallDiscountService.selectById(order.getCouponId());
			litemallDiscount.setIsUse(0);
			litemallDiscountService.updateById(litemallDiscount);
		}
				
		return ResponseUtil.ok();
	}

	/**
	 * 付款 1. 检测当前订单是否能够付款 2. 设置订单付款状态 3. TODO 微信后台申请支付，同时设置付款状态
	 * 
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@PostMapping("pay")
	public Object pay(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		if (orderId == null) {
			return ResponseUtil.badArgument();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.badArgument();
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}

		// 检测是否能够付款
		OrderHandleOption handleOption = OrderUtil.build(order);
		if (!handleOption.isPay()) {
			return ResponseUtil.fail(403, "订单不能付款");
		}

		// 微信后台申请微信支付订单号
		String payId = "";
		// 微信支付订单号生产未支付
		Short payStatus = 1;

		// 已支付、未发货或者未服务
		order.setOrderStatus(OrderUtil.STATUS_PAY);
		order.setPayId(payId);
		order.setPayStatus(payStatus);
		orderService.update(order);

		return ResponseUtil.ok();
	}

	/**
	 * 付款成功回调接口 1. 检测当前订单是否是付款状态 2. 设置订单付款成功状态相关信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx, payId: xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 *
	 *         注意，这里pay_notify是示例地址，开发者应该设立一个隐蔽的回调地址 TODO 这里需要根据微信支付文档设计
	 */
	@PostMapping("pay_notify")
	public Object pay_notify(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		Integer payId = JacksonUtil.parseInteger(body, "payId");
		if (orderId == null || payId == null) {
			return ResponseUtil.badArgument();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.badArgument();
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}

		// 检测是否是付款状态
		if (!order.getOrderStatus().equals(OrderUtil.STATUS_PAY)) {
			logger.error("系统内部错误");
		}
		if (!order.getPayId().equals(payId)) {
			logger.error("系统内部错误");
		}

		Short payStatus = (short) 2;
		order.setPayStatus(payStatus);

		List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(orderId);
		if (litemallOrderGoodsList != null && litemallOrderGoodsList.size() > 0) {

			// 商品订单和服务订单标志
			for (LitemallOrderGoods litemallOrderGoods : litemallOrderGoodsList) {
				litemallOrderGoods.setOrderStatus(payStatus);
				orderGoodsService.update(litemallOrderGoods);
			}
		}

		order.setPayTime(LocalDateTime.now());
		orderService.update(order);

		return ResponseUtil.ok();
	}

	/**
	 * 退款取消订单 1. 检测当前订单是否能够退款取消 2. 设置订单退款取消状态 3. TODO 退款 4. 商品货品数量增加
	 * 
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@PostMapping("refund")
	public Object refund(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		if (orderId == null) {
			return ResponseUtil.badArgument();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.badArgument();
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}

		OrderHandleOption handleOption = OrderUtil.build(order);
		if (!handleOption.isRefund()) {
			return ResponseUtil.fail(403, "订单不能取消");
		}

		// 设置订单取消状态
		List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(orderId);
		if (litemallOrderGoodsList != null && litemallOrderGoodsList.size() > 0) {

			// 商品订单和服务订单标志
			for (LitemallOrderGoods litemallOrderGoods : litemallOrderGoodsList) {
				litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_REFUND);
				orderGoodsService.update(litemallOrderGoods);
			}
		}
		order.setOrderStatus(OrderUtil.STATUS_REFUND);
		orderService.update(order);

		// 退款操作(余额支付)
		if(order.getPayType() == 2) {
			litemallDistributionProfitService.refundMoneyByUserId(userId, order.getActualPrice().doubleValue(),order.getOrderSn());
		}else if(order.getPayType() == 3) {
			litemallRechargeService.refundMoneyByUserId(userId, order.getActualPrice().doubleValue(),order.getOrderSn());
		}

		// 商品货品数量增加
		List<LitemallOrderGoods> orderGoodsList = orderGoodsService.queryByOid(orderId);
		for (LitemallOrderGoods orderGoods : orderGoodsList) {
			Integer productId = orderGoods.getProductId();
			LitemallProduct product = productService.findById(productId);
			if (product == null) {
				return ResponseUtil.badArgumentValue();
			}
			Integer number = product.getGoodsNumber() + orderGoods.getNumber();
			product.setGoodsNumber(number);
			productService.updateById(product);
		}
		
		// 更改优惠券状态为未使用
		if (order.getCouponId() != null) {
			LitemallDiscount litemallDiscount = litemallDiscountService.selectById(order.getCouponId());
			litemallDiscount.setIsUse(0);
			litemallDiscountService.updateById(litemallDiscount);
		}

		return ResponseUtil.ok();
	}

	/**
	 * 发货 1. 检测当前订单是否能够发货 2. 设置订单发货状态
	 * 
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx, shipSn: xxx, shipChannel: xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@PostMapping("ship")
	public Object ship(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		String shipSn = JacksonUtil.parseString(body, "shipSn");
		String shipChannel = JacksonUtil.parseString(body, "shipChannel");
		if (orderId == null || shipSn == null || shipChannel == null) {
			return ResponseUtil.badArgument();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.badArgument();
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}

		// 如果订单不是已付款状态，则不能发货
		if (!order.getOrderStatus().equals(OrderUtil.STATUS_PAY)) {
			return ResponseUtil.fail(403, "订单不能发货");
		}

		order.setOrderStatus(OrderUtil.STATUS_SHIP);
		order.setShipSn(shipSn);
		order.setShipChannel(shipChannel);
		order.setShipStartTime(LocalDateTime.now());
		orderService.update(order);

		return ResponseUtil.ok();
	}

	/**
	 * 确认收货 1. 检测当前订单是否能够确认订单 2. 设置订单确认状态
	 * 
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@PostMapping("confirm")
	public Object confirm(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		if (orderId == null) {
			return ResponseUtil.badArgument();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.badArgument();
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}

		OrderHandleOption handleOption = OrderUtil.build(order);
		if (!handleOption.isConfirm()) {
			return ResponseUtil.fail(403, "订单不能确认收货");
		}

		// 判断订单状态
		List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(orderId);
		boolean isConfirm = true;
		// 商品订单和服务订单标志
		for (LitemallOrderGoods litemallOrderGoods : litemallOrderGoodsList) {
			if (litemallOrderGoods.getFlag().equals("1")) {
				litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_CONFIRM);
				orderGoodsService.update(litemallOrderGoods);
			} else {
				if(!litemallOrderGoods.getOrderStatus().equals(OrderUtil.STATUS_CONFIRM)) {
					isConfirm = false;
				}
			}
		}

		if (isConfirm == true) {
			// 已收货
			order.setOrderStatus(OrderUtil.STATUS_CONFIRM);
		} else {
			// 部分收货
			order.setOrderStatus(OrderUtil.STATUS_PART_CONFIRM);
		}
		order.setConfirmTime(LocalDateTime.now());
		orderService.update(order);

		return ResponseUtil.ok();
	}

	/**
	 * 自动确认收货 1. 检测当前订单是否能够自动确认订单 2. 设置订单自动确认状态
	 * 
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@PostMapping("autoconfirm")
	public Object autoconfirm(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		if (orderId == null) {
			return ResponseUtil.badArgument();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.badArgument();
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}

		OrderHandleOption handleOption = OrderUtil.build(order);
		if (!handleOption.isConfirm()) {
			return ResponseUtil.fail(403, "订单不能确认收货");
		}

		List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(orderId);
		boolean isConfirm = true;
		// 商品订单和服务订单标志
		for (LitemallOrderGoods litemallOrderGoods : litemallOrderGoodsList) {
			if (litemallOrderGoods.getFlag().equals("1")) {
				litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_CONFIRM);
				orderGoodsService.update(litemallOrderGoods);
			} else {
				if(!litemallOrderGoods.getOrderStatus().equals(OrderUtil.STATUS_CONFIRM)) {
					isConfirm = false;
				}
				
			}
		}

		if (isConfirm == true) {
			// 已收货
			order.setOrderStatus(OrderUtil.STATUS_CONFIRM);
		} else {
			// 部分收货
			order.setOrderStatus(OrderUtil.STATUS_PART_CONFIRM);
		}
		order.setConfirmTime(LocalDateTime.now());
		orderService.update(order);

		return ResponseUtil.ok();
	}

	/**
	 * 删除订单 1. 检测当前订单是否删除 2. 设置订单删除状态
	 * 
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 */
	@PostMapping("delete")
	public Object delete(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		if (orderId == null) {
			return ResponseUtil.badArgument();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.badArgument();
		}
		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}

		OrderHandleOption handleOption = OrderUtil.build(order);
		if (!handleOption.isDelete()) {
			return ResponseUtil.fail(403, "订单不能删除");
		}

		// 订单order_status没有字段用于标识删除
		// 而是存在专门的delete字段表示是否删除
		orderService.deleteById(orderId);

		return ResponseUtil.ok();
	}

	/**
	 * 可以评价的订单商品信息
	 *
	 * @param userId
	 *            用户ID
	 * @param orderId
	 *            订单ID
	 * @param goodsId
	 *            商品ID
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功', data: xxx } 失败则 { errno: XXX,
	 *         errmsg: XXX }
	 */
	@PostMapping("comment")
	public Object comment(@RequestBody String body, HttpServletRequest request) {

		Integer userId = JacksonUtil.parseInteger(body, "userId");
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		Integer goodsId = JacksonUtil.parseInteger(body, "goodsId");

		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		if (orderId == null) {
			return ResponseUtil.badArgument();
		}

		List<LitemallOrderGoods> orderGoodsList = orderGoodsService.findByOidAndGid(orderId, goodsId);
		int size = orderGoodsList.size();

		Assert.state(size < 2, "存在多个符合条件的订单商品");

		if (size == 0) {
			return ResponseUtil.badArgumentValue();
		}

		LitemallOrderGoods orderGoods = orderGoodsList.get(0);
		return ResponseUtil.ok(orderGoods);
	}

	/**
	 * 查询支付是否成功
	 * 
	 * @param userId
	 * @param orderId
	 * @param request
	 * @return
	 */
	@RequestMapping("orderQuery")
	public Object orderQuery(@RequestBody String body, Integer orderId, HttpServletRequest request) {
		
		Map<String, Object> result = new HashMap<>();
		
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		orderId = JacksonUtil.parseInteger(body, "orderId");
		System.out.println("====================================="+orderId);
		
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (orderId == null) {
			return ResponseUtil.fail402();
		}

		LitemallOrder order = orderService.findById(orderId);
		LitemallUser user = userService.findById(userId);
		if (user.getWeixinOpenid() == null) {
			return ResponseUtil.fail(403, "用户openid不存在");
		}
		if (order == null) {
			return ResponseUtil.fail(403, "订单不存在");
		}

		if (!order.getUserId().equals(userId)) {
			return ResponseUtil.badArgumentValue();
		}	
	
		
		if(	order.getActualPrice().compareTo(new BigDecimal(0.00)) == 0) {
			LitemallOrder updateOrder = new LitemallOrder();
			updateOrder.setId(order.getId());
			updateOrder.setOrderStatus(OrderUtil.STATUS_PAY);

			List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(orderId);

			// 商品订单和服务订单标志
			for (LitemallOrderGoods litemallOrderGoods : litemallOrderGoodsList) {
					litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_PAY);
					orderGoodsService.update(litemallOrderGoods);
			}

			// 微信的订单号
			updateOrder.setPayId(null);
			orderService.update(updateOrder);
			
			result.put("tradeStatus", "SUCCESS");
			return ResponseUtil.ok(result);
		}
		
		WxPayOrderQueryResult wxPayOrderQueryResult = null;
		try {
			wxPayOrderQueryResult = wxService.queryOrder(null, order.getOrderSn());
		} catch (WxPayException e) {
			e.printStackTrace();
			return ResponseUtil.fail(403, "查询失败");
		}
	
		if (wxPayOrderQueryResult.getTradeState().equals("SUCCESS")) {
			// 支付成功,且订单处于未支付状态
			if (order.getOrderStatus().intValue() == 101 && order.getPayType() == 1) {
				LitemallOrder updateOrder = new LitemallOrder();
				updateOrder.setId(order.getId());
				updateOrder.setOrderStatus(OrderUtil.STATUS_PAY);

				List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(orderId);

				// 商品订单和服务订单标志
				for (LitemallOrderGoods litemallOrderGoods : litemallOrderGoodsList) {
						litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_PAY);
						orderGoodsService.update(litemallOrderGoods);
				}

				// 微信的订单号
				updateOrder.setPayId(wxPayOrderQueryResult.getTransactionId());
				orderService.update(updateOrder);
			}

			result.put("tradeStatus", "SUCCESS");
			return ResponseUtil.ok(result);
		}
		result.put("tradeStatus", wxPayOrderQueryResult.getTradeState());
		result.put("tradeMsg", wxPayOrderQueryResult.getErrCodeDes());
		return ResponseUtil.ok(result);
	}

	/**
	 * 查询支付是否成功
	 * 
	 * @param userId
	 * @param orderId
	 * @param request
	 * @return
	 */
	@RequestMapping("getVerificationCode")
	public Object getVerificationCode(@RequestBody String body, HttpServletRequest request) {

		String mobile = JacksonUtil.parseString(body, "mobile");

		Map<String, Object> result = new HashMap<>();

		// 将上次生成的验证码移除掉
		CacheManager.invalidate(mobile);
		// 6位验证码
		String authCode = SmsUtil.generate(6);
		// 5分钟过期
		CacheManager.putContent(mobile, authCode, 10 * 60 * 1000);
		String content = "【梵朗PHILAB】验证码为：" + authCode + ", 10分钟内有效。";
		try {
			SmsUtil.sendSms(mobile, content, 1);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.fail(403, "发送失败");
		}

		result.put("verificationCode", authCode);
		return ResponseUtil.ok(result);
	}

	/**
	 * 验证验证码是否正确
	 * 
	 * @param request
	 * @param response
	 * @throws MobileException
	 */
	@RequestMapping("verifyCode")
	public Object verifyCode(@RequestBody String body, HttpServletRequest request) {

		Map<String, Object> result = new HashMap<>();
		String mobile = JacksonUtil.parseString(body, "mobile");
		String authCode = JacksonUtil.parseString(body, "authCode");

		Cache cache = CacheManager.getContent(mobile);
		if (cache == null || cache.isExpired())
			return ResponseUtil.fail(403, "验证码已失效!");
		else {
			if (authCode.equals(cache.getValue().toString())) {
				CacheManager.invalidate("mobile");
			} else
				return ResponseUtil.fail(403, "验证码校验错误!");
		}
		return ResponseUtil.ok(result);
	}

	/**
	 * 余额支付
	 */
	@RequestMapping("moneyPay")
	public Object moneyPay(@RequestBody String body, HttpServletRequest request) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		Integer orderId = JacksonUtil.parseInteger(body, "orderId");
		Short payType = JacksonUtil.parseShort(body, "payType");

		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (orderId == null) {
			return ResponseUtil.fail402();
		}

		LitemallOrder order = orderService.findById(orderId);
		if (order == null) {
			return ResponseUtil.fail(403, "订单不存在");
		}

		// 检测是否能够付款
		OrderHandleOption handleOption = OrderUtil.build(order);
		if (!handleOption.isPay()) {
			return ResponseUtil.fail(403, "订单不能付款");
		}

		// 更改订单状态
		LitemallOrder updateOrder = new LitemallOrder();
		updateOrder.setId(order.getId());
		List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(order.getId());
		for (LitemallOrderGoods litemallOrderGoods : litemallOrderGoodsList) {
			litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_PAY);
			orderGoodsService.update(litemallOrderGoods);
		}
		
		if(payType != null) {
			 updateOrder.setPayType(payType);
        }
		updateOrder.setOrderStatus(OrderUtil.STATUS_PAY);
		orderService.update(updateOrder);

		if(payType != null) {
			
			if(payType == 2) {
				// 更改账户余额
				litemallDistributionProfitService.consumptionMoneyByUserId(userId, order.getActualPrice().doubleValue(),order.getOrderSn());
				
			}else if(payType == 3) {
				litemallRechargeService.consumptionMoneyByUserId(userId, order.getActualPrice().doubleValue(),order.getOrderSn());
			}
		}
		
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("return_code", "SUCCESS");
		obj.put("return_msg", "OK");
		return obj;
	}

	/**
	 * 取得用户信息
	 */
	@RequestMapping("getUserInfo")
	public Object getUserInfo(@RequestBody String body, HttpServletRequest request) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");

		if (userId == null) {
			return ResponseUtil.fail401();
		}
		LitemallUser user = userService.findById(userId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("user", user);
		return obj;
	}

	@RequestMapping("weChatGetPhone")
    public Object weChatRegister(@RequestBody String body, HttpServletRequest request){
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String code = JacksonUtil.parseString(body, "code");
			String iv = JacksonUtil.parseString(body, "iv");
			String encryptedData = JacksonUtil.parseString(body, "encryptedData");
			
			if(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(iv) && StringUtils.isNotBlank(encryptedData)){
				String result = OpenIdUtil.oauth2GetOpenid(code);
				JSONObject jsonObject = JSONObject.fromObject(result);
				if(jsonObject.containsKey("session_key") && jsonObject.containsKey("openid")){
					String session_key = (String) jsonObject.get("session_key");
					String deString = WXBizDataCrypt.getInstance().decrypt(encryptedData, session_key, iv, "utf-8");
					JSONObject jsonObject1 = JSONObject.fromObject(deString);
			
				
					if(jsonObject1.containsKey("phoneNumber")){
						String phoneNumber = (String) jsonObject1.get("phoneNumber");
						if(StringUtils.isNotBlank(phoneNumber)){
							obj.put("phoneNumber", phoneNumber);
						} else {
							return ResponseUtil.fail(403, "解析错误！");
						}
					}else {
						return ResponseUtil.fail(403, "解析错误！");
					}
				}

		}
		return obj;
	}catch (Exception e) {
		return ResponseUtil.fail(403, "解析错误！");
	}

}
}