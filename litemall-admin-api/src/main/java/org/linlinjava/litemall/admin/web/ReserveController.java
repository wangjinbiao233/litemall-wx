package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallReserve;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reserve")
public class ReserveController {

    @Autowired
    private LitemallReserveService litemallReserveService;
    
    @Autowired
    private LitemallOrderGoodsService litemallOrderGoodsService;
    
    @Autowired
    private LitemallOrderService litemallOrderService;
    
    @Autowired
    private LitemallGoodsService goodsService;
    
    @GetMapping("/reserveDetail")
    public Object reserveDetail(Integer orderGoodsId){    	
    	List<LitemallReserve> litemallReserveList = litemallReserveService.selectByOrderGoodsId(orderGoodsId);
    	Map<String, Object> data = new HashMap<>();    	
    	data.put("item", litemallReserveList);    	
    	return ResponseUtil.ok(data);
    }
    
    @RequestMapping(value = "/reserveList", method = RequestMethod.POST)  
    public Object reserveList(@LoginAdmin Integer adminId,LitemallReserve litemallReserve, 
    		           @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,String sort, String order){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
                
        List<LitemallReserve> litemallReserveList = litemallReserveService.queryReserve(litemallReserve, page, limit, sort, order);        
        int total = litemallReserveService.countReserve(litemallReserve, page, limit, sort, order);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", litemallReserveList);
        return ResponseUtil.ok(data);
    }    
    
    
    /*
     * 目前仅仅支持管理员设置发货相关的信息
     */
    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallReserve litemallReserve){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        litemallReserveService.update(litemallReserve);
        return ResponseUtil.ok(litemallReserve);
    }
    
    /**
     * 取消预约信息
     */
    @RequestMapping( value = "/cancel", method = RequestMethod.POST)
    public Object cancel(@RequestBody LitemallReserve litemallReserve) { 
 	   
 	    //删除预约信息
		LitemallReserve oldLitemallReserve = litemallReserveService.selectById(litemallReserve.getId());
		litemallReserveService.deleteById(litemallReserve.getId());
		
 	    //更新订单明细状态
 	   	LitemallOrderGoods litemallOrderGoods = litemallOrderGoodsService.queryById(oldLitemallReserve.getOrderGoodsId());
 		litemallOrderGoods.setTreatmentNum(litemallOrderGoods.getTreatmentNum()+1);
 		
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
 		
 	   //可能需要更新订单总状态
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
