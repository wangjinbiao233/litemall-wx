package org.linlinjava.litemall.wx.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallAddress;
import org.linlinjava.litemall.db.domain.LitemallCart;
import org.linlinjava.litemall.db.domain.LitemallCustomDiscount;
import org.linlinjava.litemall.db.domain.LitemallDiscount;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallGoodsSpecification;
import org.linlinjava.litemall.db.domain.LitemallProduct;
import org.linlinjava.litemall.db.service.LitemallAddressService;
import org.linlinjava.litemall.db.service.LitemallCartService;
import org.linlinjava.litemall.db.service.LitemallCouponService;
import org.linlinjava.litemall.db.service.LitemallDiscountService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallGoodsSpecificationService;
import org.linlinjava.litemall.db.service.LitemallProductService;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/cart")
public class WxCartController {
	private final Log logger = LogFactory.getLog(WxCartController.class);

	@Autowired
	private LitemallCartService cartService;
	@Autowired
	private LitemallGoodsService goodsService;
	@Autowired
	private LitemallProductService productService;
	@Autowired
	private LitemallGoodsSpecificationService goodsSpecificationService;
	@Autowired
	private LitemallAddressService addressService;
	@Autowired
	private LitemallCouponService apiCouponService;
	
    @Autowired
    private LitemallDiscountService litemallDiscountService;
	/**
	 * 获取购物车
	 */
	@RequestMapping("index")
	public Object index(@RequestBody String body) {	
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		
		if (userId == null) {
			return ResponseUtil.fail(401, "请登录");
		}

		List<LitemallCart> cartList = cartService.queryByUid(userId);
		Integer goodsCount = 0;
		BigDecimal goodsAmount = new BigDecimal(0.00);
		Integer checkedGoodsCount = 0;
		BigDecimal checkedGoodsAmount = new BigDecimal(0.00);
		for (LitemallCart cart : cartList) {
			goodsCount += cart.getNumber();
			goodsAmount = goodsAmount.add(cart.getRetailPrice().multiply(new BigDecimal(cart.getNumber())));
			if (cart.getChecked()) {
				checkedGoodsCount += cart.getNumber();
				checkedGoodsAmount = checkedGoodsAmount
						.add(cart.getRetailPrice().multiply(new BigDecimal(cart.getNumber())));
			}
		}
		Map<String, Object> cartTotal = new HashMap<>();
		cartTotal.put("goodsCount", goodsCount);
		cartTotal.put("goodsAmount", goodsAmount);
		cartTotal.put("checkedGoodsCount", checkedGoodsCount);
		cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);

		Map<String, Object> result = new HashMap<>();
		result.put("cartList", cartList);
		result.put("cartTotal", cartTotal);

		return ResponseUtil.ok(result);
	}

	/**
	 * 添加商品加入购物车
	 */
	@RequestMapping("add")
	public Object add(@RequestBody LitemallCart cart) {
		if (cart == null) {
			return ResponseUtil.badArgument();
		}
		Integer userId = cart.getUserId();
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		Integer productId = cart.getProductId();
		Integer number = cart.getNumber().intValue();
		Integer goodsId = cart.getGoodsId();

		// 判断商品是否可以购买
		LitemallGoods goods = goodsService.findById(goodsId);
		if (goods == null || !goods.getIsOnSale()) {
			return ResponseUtil.fail(400, "商品已下架");
		}

		LitemallProduct product = productService.findById(productId);
		// 判断购物车中是否存在此规格商品
		LitemallCart existCart = cartService.queryExist(goodsId, productId, userId);
		if (existCart == null) {
			// 取得规格的信息,判断规格库存
			if (product == null || number > product.getGoodsNumber()) {
				return ResponseUtil.fail(400, "库存不足");
			}

			Integer[] ids = product.getGoodsSpecificationIds();
			String goodsSepcifitionValue = null;
			for (Integer id : ids) {
				LitemallGoodsSpecification goodsSpecification = goodsSpecificationService.findById(id);
				if (goodsSpecification == null || !goodsSpecification.getGoodsId().equals(goodsId)) {
					return ResponseUtil.badArgument();
				}
				if (goodsSepcifitionValue == null) {
					goodsSepcifitionValue = goodsSpecification.getValue();
				} else {
					goodsSepcifitionValue = goodsSepcifitionValue + " " + goodsSpecification.getValue();
				}
			}

			cart.setId(null);
			cart.setGoodsSn(goods.getGoodsSn());
			cart.setGoodsName((goods.getName()));
			cart.setPicUrl(goods.getPrimaryPicUrl());
			cart.setRetailPrice(product.getRetailPrice());
			cart.setGoodsSpecificationIds(product.getGoodsSpecificationIds());
			cart.setGoodsSpecificationValues(goodsSepcifitionValue);
			cart.setUserId(userId);
			cart.setChecked(true);
			cart.setAddTime(LocalDateTime.now());
			cartService.add(cart);
		} else {
			// 取得规格的信息,判断规格库存
			int num = existCart.getNumber() + number;
			if (num > product.getGoodsNumber()) {
				return ResponseUtil.fail(400, "库存不足");
			}
			existCart.setNumber((short) num);
			cartService.update(existCart);
		}

		return goodscount(userId);
	}

	/**
	 * 立即购买商品
	 *
	 * 和 前面一个方法add的区别在于 1. 如果购物车内有相同商品存在，前者的逻辑是数量添加，这里的逻辑是数量覆盖 2.
	 * 添加成功以后，前者的逻辑是返回当前购物车商品数量，这里的逻辑是返回对应购物车项的ID
	 */
	@RequestMapping("fastadd")
	public Object fastadd(@RequestBody LitemallCart cart) {
		if (cart == null) {
			return ResponseUtil.badArgument();
		}

		Integer userId = cart.getUserId();
		if (userId == null) {
			return ResponseUtil.unlogin();
		}

		Integer productId = cart.getProductId();
		Integer number = cart.getNumber().intValue();
		Integer goodsId = cart.getGoodsId();

		// 判断商品是否可以购买
		LitemallGoods goods = goodsService.findById(goodsId);
		if (goods == null || !goods.getIsOnSale()) {
			return ResponseUtil.fail(400, "商品已下架");
		}

		LitemallProduct product = productService.findById(productId);
		// 判断购物车中是否存在此规格商品
		LitemallCart existCart = cartService.queryExist(goodsId, productId, userId);
		if (existCart == null) {
			// 取得规格的信息,判断规格库存
			if (product == null || number > product.getGoodsNumber()) {
				return ResponseUtil.fail(400, "库存不足");
			}

			Integer[] ids = product.getGoodsSpecificationIds();
			String goodsSepcifitionValue = null;
			for (Integer id : ids) {
				LitemallGoodsSpecification goodsSpecification = goodsSpecificationService.findById(id);
				if (goodsSpecification == null || !goodsSpecification.getGoodsId().equals(goodsId)) {
					return ResponseUtil.badArgument();
				}
				if (goodsSepcifitionValue == null) {
					goodsSepcifitionValue = goodsSpecification.getValue();
				} else {
					goodsSepcifitionValue = goodsSepcifitionValue + " " + goodsSpecification.getValue();
				}
			}

			cart.setId(null);
			cart.setGoodsSn(goods.getGoodsSn());
			cart.setGoodsName((goods.getName()));
			cart.setPicUrl(goods.getPrimaryPicUrl());
			cart.setRetailPrice(product.getRetailPrice());
			cart.setGoodsSpecificationIds(product.getGoodsSpecificationIds());
			cart.setGoodsSpecificationValues(goodsSepcifitionValue);
			cart.setUserId(userId);
			cart.setChecked(true);
			cartService.add(cart);
		} else {
			// 取得规格的信息,判断规格库存
			int num = number;
			if (num > product.getGoodsNumber()) {
				return ResponseUtil.fail(400, "库存不足");
			}
			existCart.setNumber((short) num);
			cartService.update(existCart);
		}

		return ResponseUtil.ok(existCart != null ? existCart.getId() : cart.getId());
	}

	/**
	 * 更新指定的购物车信息 目前只支持修改商品的数量
	 */
	@RequestMapping("update")
	public Object update(@LoginUser Integer userId, @RequestBody LitemallCart cart) {
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (cart == null) {
			return ResponseUtil.fail402();
		}

		Integer productId = cart.getProductId();
		Integer number = cart.getNumber().intValue();
		Integer goodsId = cart.getGoodsId();
		Integer id = cart.getId();

		// 判断是否存在该订单
		// 如果不存在，直接返回错误
		LitemallCart existCart = cartService.findById(id);
		if (existCart == null) {
			return ResponseUtil.fail403();
		}

		// 判断goodsId和productId是否与当前cart里的值一致
		if (!existCart.getGoodsId().equals(goodsId)) {
			return ResponseUtil.fail403();
		}
		if (!existCart.getProductId().equals(productId)) {
			return ResponseUtil.fail403();
		}

		// 判断商品是否可以购买
		LitemallGoods goods = goodsService.findById(goodsId);
		if (goods == null || !goods.getIsOnSale()) {
			return ResponseUtil.fail(403, "商品已下架");
		}

		// 取得规格的信息,判断规格库存
		LitemallProduct product = productService.findById(productId);
		if (product == null || product.getGoodsNumber() < number) {
			return ResponseUtil.fail(403, "库存不足");
		}

		existCart.setNumber(number.shortValue());
		cartService.update(existCart);
		return ResponseUtil.ok();
	}

	/**
	 * 是否选择商品，如果已经选择，则取消选择，批量操作
	 */
	@RequestMapping("checked")
	public Object checked(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (body == null) {
			return ResponseUtil.fail402();
		}

		List<Integer> productIds = JacksonUtil.parseIntegerList(body, "productIds");
		if (productIds == null) {
			return ResponseUtil.fail402();
		}
		Integer checkValue = JacksonUtil.parseInteger(body, "isChecked");
		if (checkValue == null) {
			return ResponseUtil.fail402();
		}
		Boolean isChecked = ((checkValue.intValue()) == 1);

		cartService.updateCheck(userId, productIds, isChecked);
		return index(body);
	}

	// 删除选中的购物车商品，批量删除
	@RequestMapping("delete")
	public Object delete(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (body == null) {
			return ResponseUtil.fail402();
		}

		List<Integer> productIds = JacksonUtil.parseIntegerList(body, "productIds");

		if (productIds == null) {
			return ResponseUtil.fail402();
		}

		cartService.delete(productIds, userId);
		return index(body);
	}

	/*
	 * 获取购物车商品的总件件数 用户也是可选登录，如果没有登录，则返回空数据
	 */
	@RequestMapping("goodscount")
	public Object goodscount(Integer userId) {
		if (userId == null) {
			return ResponseUtil.ok(0);
		}

		int goodsCount = 0;
		List<LitemallCart> cartList = cartService.queryByUid(userId);
		for (LitemallCart cart : cartList) {
			goodsCount += cart.getNumber();
		}

		return ResponseUtil.ok(goodsCount);
	}

	/**
	 * 订单提交前的检验和填写相关订单信息
	 */
	@RequestMapping("checkout")
	public Object checkout( @RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		Integer cartId = JacksonUtil.parseInteger(body, "cartId");
		Integer addressId = JacksonUtil.parseInteger(body, "addressId");
		Integer couponId = JacksonUtil.parseInteger(body, "couponId");
		if (userId == null) {
			return ResponseUtil.fail401();
		}

		// 收货地址
		LitemallAddress checkedAddress = null;
		if (addressId == null || addressId.equals(0)) {
			checkedAddress = addressService.findDefault(userId);
			// 如果仍然没有地址，则是没有收获地址
			// 返回一个空的地址id=0，这样前端则会提醒添加地址
			if (checkedAddress == null) {
				checkedAddress = new LitemallAddress();
				checkedAddress.setId(0);
				addressId = 0;
			} else {
				addressId = checkedAddress.getId();
			}

		} else {
			checkedAddress = addressService.findById(addressId);
			// 如果null, 则报错
			if (checkedAddress == null) {
				return ResponseUtil.badArgumentValue();
			}
		}

		// 获取可用的优惠券信息
		// 使用优惠券减免的金额
		Integer couponPrice = 0;
		if(couponId != 0) {
			LitemallDiscount litemallDiscount = litemallDiscountService.selectById(couponId);		
			couponPrice = litemallDiscount.getDiscountsPrice();
		}
		
		// 商品价格

		Map<String, Object> data = new HashMap();
		data.put("flag",2);
		List<LitemallCart> checkedGoodsList = null;
		if (cartId == null || cartId.equals(0)) {
			checkedGoodsList = cartService.queryByUidAndChecked(userId);
			//此处为购物车跳转
				for (LitemallCart cart : checkedGoodsList) {
					//根据商品id查询商品
					LitemallGoods goods=goodsService.findById(cart.getGoodsId());
					if("1".equals(goods.getFlag())){
						data.put("flag",1);
					}
				}
		} else {
			LitemallCart cart = cartService.findById(cartId);
			if (cart == null) {
				return ResponseUtil.badArgumentValue();
			}
			LitemallGoods goods=goodsService.findById(cart.getGoodsId());
			if("1".equals(goods.getFlag())){
				data.put("flag",1);
			}
			checkedGoodsList = new ArrayList<>(1);
			checkedGoodsList.add(cart);
		}
		BigDecimal checkedGoodsPrice = new BigDecimal(0.00);
		for (LitemallCart cart : checkedGoodsList) {
			checkedGoodsPrice = checkedGoodsPrice.add(cart.getRetailPrice().multiply(new BigDecimal(cart.getNumber())));
		}

		// 根据订单商品总价计算运费，满88则免运费，否则8元；
		BigDecimal freightPrice = new BigDecimal(0.00);
		if (checkedGoodsPrice.compareTo(new BigDecimal(88.00)) == -1) {
			
			//test coding.....
			freightPrice = new BigDecimal(0.00);
		}

		// 可以使用的其他钱，例如用户积分
		BigDecimal integralPrice = new BigDecimal(0.00);

		// 订单费用
		BigDecimal orderTotalPrice = checkedGoodsPrice.add(freightPrice).subtract(new BigDecimal(couponPrice));
		BigDecimal actualPrice = orderTotalPrice.subtract(integralPrice);
		
		List<LitemallCustomDiscount> couponList = litemallDiscountService.selectMyUseCoupotList(userId,checkedGoodsPrice);
		data.put("checkedAddress", checkedAddress);
		data.put("freightPrice", freightPrice);
		data.put("checkedCoupon", 0);
		data.put("couponList", couponList);
		data.put("couponPrice", couponPrice);
		data.put("checkedGoodsList", checkedGoodsList);
		data.put("goodsTotalPrice", checkedGoodsPrice);
		data.put("orderTotalPrice", orderTotalPrice);
		data.put("actualPrice", actualPrice);
		data.put("addressId", addressId);
		return ResponseUtil.ok(data);

	}

	/**
	 * 选择优惠券列表
	 */
	@RequestMapping("checkedCouponList")
	public Object checkedCouponList(@LoginUser Integer userId) {
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		return ResponseUtil.fail501();
	}
}