package org.linlinjava.litemall.admin.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallExpress;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallProduct;
import org.linlinjava.litemall.db.service.LitemallDistributionProfitService;
import org.linlinjava.litemall.db.service.LitemallExpressService;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallProductService;
import org.linlinjava.litemall.db.service.LitemallRechargeService;
import org.linlinjava.litemall.db.util.OrderHandleOption;
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
@RequestMapping("/admin/order")
public class OrderController {
    private final Log logger = LogFactory.getLog(OrderController.class);

    @Autowired
    private LitemallOrderService orderService;
    
    @Autowired
    private LitemallOrderGoodsService litemallOrderGoodsService;
    
    @Autowired
    private LitemallExpressService litemallExpressService;
    
    @Autowired
	private LitemallProductService productService;

	@Autowired
	private LitemallDistributionProfitService litemallDistributionProfitService;
	 
	@Autowired
	private LitemallRechargeService litemallRechargeService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)  
    public Object list(@LoginAdmin Integer adminId,
    		           LitemallOrder litemallOrder,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        
        List<LitemallOrder> orderList = orderService.querySelective(litemallOrder, page, limit, sort, order);
        //判断订单是服务订单或者商品订单
        if(orderList.size() > 0) {        	
        	for(LitemallOrder litemall :orderList) { 		
        		litemall.setOrderStatusDisp(OrderUtil.orderStatusText(litemall));
        		List<LitemallOrderGoods> litemallOrderGoodsList = litemallOrderGoodsService.queryByOid(litemall.getId());
        		litemall.setLitemallOrderGoodsList(litemallOrderGoodsList);
        		if(litemallOrderGoodsList != null && litemallOrderGoodsList.size() > 0) {    
        			
        			//商品名称拼接
        			List<String> orderGoodsNameList = new ArrayList<String>();
            		for(LitemallOrderGoods litemallOrderGoods :litemallOrderGoodsList) { 
            			orderGoodsNameList.add(litemallOrderGoods.getGoodsName());            			
                		litemall.setOrderGoodsName(StringUtils.join(orderGoodsNameList, ","));
                		
                		//判断订单是否可以退货
                		OrderHandleOption handleOption = OrderUtil.orderGoodsbuild(litemall, litemallOrderGoods);
                		litemall.setIsReturn(String.valueOf(handleOption.isReturn()));
            		}
        		}
        	}
        }        
        
        //判断订单是否可取消
        if(orderList.size() > 0) {        	
        	for(LitemallOrder litemall :orderList) {
        		OrderHandleOption handleOption = OrderUtil.build(litemall);
        		litemall.setIsCancel(String.valueOf(handleOption.isCancel()));
        	}
        }
        
        //判断订单是否可发货
        if(orderList.size() > 0) {        	
        	for(LitemallOrder litemall :orderList) {
        		
        		List<LitemallOrderGoods> litemallOrderGoodsList = litemallOrderGoodsService.queryByOid(litemall.getId());
        		if(litemallOrderGoodsList != null && litemallOrderGoodsList.size() > 0) { 

            		litemall.setIsShip("false");
            		for(LitemallOrderGoods litemallOrderGoods :litemallOrderGoodsList) { 
		        		if (litemall.getOrderStatus() == 201 ||  litemall.getOrderStatus() == 302 || litemall.getOrderStatus() == 403 ) {
		        			//已发货或者部分发货时 订单明细中有未发货的订单才可以发货
		        			
		        			if(litemallOrderGoods.getOrderStatus() == 201 && litemallOrderGoods.getFlag().equals("1")) {
                				litemall.setIsShip("true");
                				break;
                			}		        			
		                }
    				}	                	
                }        		
        	}
        }
        
        int total = orderService.countSelective(litemallOrder, page, limit, sort, order);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", orderList);

        return ResponseUtil.ok(data);
    }    

    /*
     * 目前的逻辑不支持管理员创建
     */
    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallOrder order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.unsupport();
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.fail401();
        }

        LitemallOrder order = orderService.findById(id);
        return ResponseUtil.ok(order);
    }

    /*
     * 目前仅仅支持管理员设置发货相关的信息
     */
    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallOrder order) throws Exception{
    	
         ZonedDateTime zdt = order.getShipStartTime().atZone(ZoneId.systemDefault());
         Date date = Date.from(zdt.toInstant());
         Calendar cal = Calendar.getInstance();
         cal.setTimeInMillis(date.getTime());
         cal.add(Calendar.HOUR, +8);
         
         Instant instant = cal.getTime().toInstant();
         ZoneId zoneId = ZoneId.systemDefault();
         LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
         order.setShipStartTime(localDateTime);         
         
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        Integer orderId = order.getId();
        if(orderId == null){
            return ResponseUtil.badArgument();
        }

        LitemallOrder zmallOrder = orderService.findById(orderId);
        if(zmallOrder == null){
            return ResponseUtil.badArgumentValue();
        }

        if(OrderUtil.isPayStatus(zmallOrder) || OrderUtil.isShipStatus(zmallOrder) || OrderUtil.isPartShipStatus(zmallOrder)||OrderUtil.isPartConfirmStatus(zmallOrder)){
            LitemallOrder newOrder = new LitemallOrder();
            newOrder.setId(orderId);
            newOrder.setShipChannel(order.getShipChannel());
            newOrder.setShipSn(order.getShipSn());
            
            int orderCnt = orderService.countByShipSn(newOrder);
            if(orderCnt > 0) {
            	return ResponseUtil.ok("快递单号不能重复", null);
            }
            
            boolean isShip = true;
            List<LitemallOrderGoods> litemallOrderGoodsList = litemallOrderGoodsService.queryByOid(orderId);    	
            if(litemallOrderGoodsList != null && litemallOrderGoodsList.size() > 0) {    
        		for(LitemallOrderGoods litemallOrderGoods :litemallOrderGoodsList) { 
        			if(litemallOrderGoods.getFlag().equals("1")) {
        				litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_SHIP);        				
        				litemallOrderGoodsService.update(litemallOrderGoods);
        			}
        			
        			//服务订单中存在已付款未发货的，更新总订单状态为部分发货，否则为已发货
        			if(litemallOrderGoods.getFlag().equals("2")) {
    					if (litemallOrderGoods.getOrderStatus().equals(OrderUtil.STATUS_PAY)) {
        					isShip = false;
        				}
        			}
        		}
            }
                        
            //订单状态
        	if (isShip == true) {
    			// 已发货
        		if(!zmallOrder.getOrderStatus().equals(OrderUtil.STATUS_PART_CONFIRM)) {
        			order.setOrderStatus(OrderUtil.STATUS_SHIP);
        		}
    		} else {
    			// 部分发货
    			order.setOrderStatus(OrderUtil.STATUS_PART_SHIP);
    		}
    		order.setConfirmTime(LocalDateTime.now());
    		orderService.update(order);
 		   
        } else {
        	return ResponseUtil.ok("不能发货",null);
        }
        return ResponseUtil.ok(zmallOrder);
    }
    
    
    /*
     * 部分退货
     */
    @PostMapping("/orderReturn")
    public Object orderReturn(@LoginAdmin Integer adminId, @RequestBody LitemallOrder order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
    	LitemallOrder oldOrder = orderService.findById(order.getId());
		if (oldOrder == null) {
			return ResponseUtil.badArgument();
		}
		
		//商品金额
		BigDecimal checkedGoodsPrice = new BigDecimal(0.00);
		 // 设置订单详情取消状态
		 List<LitemallOrderGoods> litemallOrderGoodsList = litemallOrderGoodsService.queryByOid(order.getId());    	
		 if(litemallOrderGoodsList != null && litemallOrderGoodsList.size() > 0) {    
			for(LitemallOrderGoods litemallOrderGoods :litemallOrderGoodsList) { 
				if(litemallOrderGoods.getFlag().equals("1")) {
					litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_REFUND_MONEY);        				
					litemallOrderGoodsService.update(litemallOrderGoods);
					
					Integer productId = litemallOrderGoods.getProductId();
					LitemallProduct product = productService.findById(productId);
					if (product == null) {
						return ResponseUtil.badArgumentValue();
					}
					Integer number = product.getGoodsNumber() + litemallOrderGoods.getNumber();
					product.setGoodsNumber(number);
					productService.updateById(product);
					
					checkedGoodsPrice = checkedGoodsPrice
							.add(litemallOrderGoods.getRetailPrice().multiply(new BigDecimal(litemallOrderGoods.getNumber())));
				}
			}
		 }
		 
		 //退款金额
		 BigDecimal actualPrice = checkedGoodsPrice.subtract(oldOrder.getIntegralPrice()).subtract(oldOrder.getCouponPrice());
		
		// 设置订单取消状态
		order.setOrderStatus(OrderUtil.STATUS_REFUND_MONEY);
		orderService.update(order);

		//退款操作
		if(oldOrder.getPayType() == 2) {
			litemallDistributionProfitService.refundMoneyByUserId(oldOrder.getUserId(), actualPrice.doubleValue(),oldOrder.getOrderSn());
		}else if(oldOrder.getPayType() == 3) {
			litemallRechargeService.refundMoneyByUserId(oldOrder.getUserId(), actualPrice.doubleValue(),oldOrder.getOrderSn());
		}
		
        return ResponseUtil.ok(oldOrder);
    }
    
    
    /*
     * 取消订单
     */
    @PostMapping("/updateOrderStatus")
    public Object updateOrderStatus(@LoginAdmin Integer adminId, @RequestBody LitemallOrder order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        Integer orderId = order.getId();
        if(orderId == null){
            return ResponseUtil.badArgument();
        }

        LitemallOrder litemallOrder = orderService.findById(orderId);
		if (litemallOrder == null) {
			return ResponseUtil.badArgumentValue();
		}
		
		// 检测是否能够取消
		OrderHandleOption handleOption = OrderUtil.build(litemallOrder);
		if (!handleOption.isCancel()) {
			return ResponseUtil.fail(403, "订单不能取消");
		}

		// 设置订单已取消状态
		litemallOrder.setOrderStatus(OrderUtil.STATUS_CANCEL);
		orderService.update(litemallOrder);

		// 商品货品数量增加
		List<LitemallOrderGoods> orderGoodsList = litemallOrderGoodsService.queryByOid(orderId);
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
        return ResponseUtil.ok(litemallOrder);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallOrder order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.unsupport();
    }
    
    @GetMapping("/orderDetailList")
    public Object orderDetailList(Integer orderId){    	
    	List<LitemallOrderGoods> litemallOrderGoodsList = litemallOrderGoodsService.queryByOid(orderId);    	
    	Map<String, Object> data = new HashMap<>();    	
    	data.put("items", litemallOrderGoodsList);    	
    	return ResponseUtil.ok(data);
    }
    
    @GetMapping("/litemallExpressList")
    public Object litemallExpressList(){    	
    	List<LitemallExpress> litemallExpressList = litemallExpressService.query(null);
    	Map<String, Object> data = new HashMap<>();    	
    	data.put("items", litemallExpressList);    	
    	return ResponseUtil.ok(data);
    }
    
}
