package org.linlinjava.litemall.wx.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallReserve;
import org.linlinjava.litemall.db.domain.LitemallTime;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallReserveService;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 *预约接口
 */
@RestController
@RequestMapping("/wx/reserve")
public class WxReserveController {
	private final Log logger = LogFactory.getLog(WxReserveController.class);

	@Autowired
	private LitemallReserveService litemallReserveService;

	@Autowired
	private LitemallOrderGoodsService litemallOrderGoodsService;

	@Autowired
	private LitemallOrderService litemallOrderService;
	
    @Autowired
    private LitemallGoodsService goodsService;

	public WxReserveController() {
	}

	/**
	 * 查看预约列表
	 */
	@PostMapping("reserveList")
	public Object reserveList(@RequestBody LitemallReserve litemallReserve) {
		Integer userId = litemallReserve.getUserId();
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		List<LitemallReserve> list = litemallReserveService.selectReserveList(litemallReserve);// 获取订单
		Map<String, Object> result = new HashMap<>();
		result.put("data", list);

		return ResponseUtil.ok(result);

	}

	// init 初始化数据

	@GetMapping("initTimeData")
	public Object timeData(String storeId, String currentTime) {
		// 循环时间段进行遍历 封装成list 返回到前端
		// String storeId= "11";
		String timeStr = "09:00,09:30,10:00,10:30,11:00,11:30,12:00,12:30,13:00,"
				+ "13:30,14:00,14:30,15:00,15:30,16:00,16:30,17:00,17:30,18:00,18:30";
		String[] timeArr = timeStr.split(",");

		// 日期格式转换
		String dateStr = currentTime.replaceAll("-", "/");
		String[] dateStrArr = dateStr.split("/");
		if (dateStrArr[1].length() == 1) {
			dateStrArr[1] = "0" + dateStrArr[1];
		}
		if (dateStrArr[2].length() == 1) {
			dateStrArr[2] = "0" + dateStrArr[2];
		}

		currentTime = dateStrArr[0] + "/" + dateStrArr[1] + "/" + dateStrArr[2];
		// 查询门店最大人预约数
		String maxServicePeo = litemallOrderGoodsService.selectMaxServicePeo(storeId);
		System.out.println(maxServicePeo);
		List<LitemallTime> resList = new ArrayList<LitemallTime>();
		for (String time : timeArr) {
			LitemallTime resLitemallTime = litemallOrderGoodsService.selectTimeData(time, storeId, currentTime);
			resLitemallTime.setMaxSercePeo(maxServicePeo);
			resLitemallTime.setStoreId(storeId);
			resLitemallTime.setTime(time);
			resList.add(resLitemallTime);
		}

		return ResponseUtil.ok(resList);

	}

	/**
	 * 插入预约信息
	 */
	@RequestMapping(value = "/reserveDetail", method = RequestMethod.POST)
	public Object reserveDetail(@RequestBody LitemallReserve litemallReserve) {

		/*
		 * LitemallOrderGoods LitemallOrderGoods =
		 * litemallOrderGoodsService.queryById(litemallReserve.getOrderGoodsId());
		 * //已服务状态 LitemallOrderGoods.setOrderStatus(OrderUtil.STATUS_CONSUME);
		 * litemallOrderGoodsService.update(LitemallOrderGoods);
		 */

		// 获取reserveDate 格式化

		String dateStr = litemallReserve.getReserveDate().replaceAll("-", "/");
		String[] dateStrArr = dateStr.split("/");
		if (dateStrArr[1].length() == 1) {
			dateStrArr[1] = "0" + dateStrArr[1];
		}
		if (dateStrArr[2].length() == 1) {
			dateStrArr[2] = "0" + dateStrArr[2];
		}
		litemallReserve.setReserveDate(dateStrArr[0] + "/" + dateStrArr[1] + "/" + dateStrArr[2]);
		// 插入数据到
		litemallOrderGoodsService.reserveDetail(litemallReserve);

		// 更新order_goods里面的状态
		LitemallOrderGoods litemallOrderGoods = litemallOrderGoodsService.queryById(litemallReserve.getOrderGoodsId());
		litemallOrderGoods.setTreatmentNum(litemallOrderGoods.getTreatmentNum() - 1);
		
		if (litemallOrderGoods.getTreatmentNum() < 0) {
			return ResponseUtil.fail(403, "预约失败");
		}
		
		if (litemallOrderGoods.getTreatmentNum() == 0) {
			litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_SHIP);
		}else {
			litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_PART_SHIP);
		}
		
		litemallOrderGoodsService.update(litemallOrderGoods);

		// 可能需要更新订单总状态
		LitemallOrder litemallOrder = litemallOrderService.findById(litemallOrderGoods.getOrderId());
		List<LitemallOrderGoods> orderGoodsList = litemallOrderGoodsService.queryByOid(litemallOrderGoods.getOrderId());
		if (orderGoodsList.size() > 0) {
			List<String> flagList = new ArrayList<String>();
			List<Short> orderStatusList = new ArrayList<Short>();
			for (LitemallOrderGoods good : orderGoodsList) {
				flagList.add(good.getFlag());
				orderStatusList.add(good.getOrderStatus());
			}

			if (orderStatusList.contains(OrderUtil.STATUS_PAY) || litemallOrderGoods.getOrderStatus().equals( OrderUtil.STATUS_PART_SHIP)) {
				// 订单中还有未发货或者未预约的订单
				litemallOrder.setOrderStatus(OrderUtil.STATUS_PART_SHIP);

			} else {
				litemallOrder.setOrderStatus(OrderUtil.STATUS_SHIP);
			}
			litemallOrderService.update(litemallOrder);
		}
		return ResponseUtil.ok();
	}

	/**
	 * 取消预约信息
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public Object cancel(@RequestBody LitemallReserve litemallReserve) {

		// 删除预约信息
		LitemallReserve oldLitemallReserve = litemallReserveService.selectById(litemallReserve.getId());
		litemallReserveService.deleteById(litemallReserve.getId());

		// 更新订单明细状态
		LitemallOrderGoods litemallOrderGoods = litemallOrderGoodsService
				.queryById(oldLitemallReserve.getOrderGoodsId());
		litemallOrderGoods.setTreatmentNum(litemallOrderGoods.getTreatmentNum() + 1);
		
		// 可能需要更新订单总状态
		boolean isShip = true;
				
		 // 商品信息
        LitemallGoods info = goodsService.findById(litemallOrderGoods.getGoodsId());
		if(litemallOrderGoods.getTreatmentNum() == info.getTreatmentNum()*litemallOrderGoods.getNumber()) {
			litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_PAY);
		}else {
			litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_PART_SHIP);
			isShip = false;
		}
		
		litemallOrderGoodsService.update(litemallOrderGoods);

		
		LitemallOrder litemallOrder = litemallOrderService.findById(litemallOrderGoods.getOrderId());
		List<LitemallOrderGoods> orderGoodsList = litemallOrderGoodsService.queryByOid(litemallOrderGoods.getOrderId());
		if (orderGoodsList.size() > 0) {
			for (LitemallOrderGoods good : orderGoodsList) {
				if (good.getOrderStatus().equals(OrderUtil.STATUS_PAY)) {
					isShip = false;
					break;
				}
			}
					
			if (isShip == true) {
    			// 已发货
				litemallOrder.setOrderStatus(OrderUtil.STATUS_SHIP);
    		} else {
    			// 部分发货
    			litemallOrder.setOrderStatus(OrderUtil.STATUS_PART_SHIP);
    		}
			litemallOrderService.update(litemallOrder);
		}

		return ResponseUtil.ok();
	}

}