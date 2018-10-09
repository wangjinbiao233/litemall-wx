package org.linlinjava.litemall.db.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallProduct;
import org.springframework.util.Assert;

/*
 * 订单流程：下单成功－》支付订单－》发货－》收货
 * 订单状态：
 * 101 订单生成，未支付；102，订单生产，但是未支付就取消；
 * 201 支付完成，商家未发货；202，订单生产，已付款未发货，却取消  
 * 301 商家发货，用户未确认；或已预约   302 部分发货    303 发货后退款中 304 发货后退款成功 
 * 401 用户确认收货，订单结束 或预约完成； 402 用户没有确认收货，但是快递反馈已收获后，超过一定时间，系统自动确认收货，订单结束。 403 部分收货
 * 501 已完成 
 *
 * 当101用户未付款时，此时用户可以进行的操作是取消订单，或者付款操作
 * 当201支付完成而商家未发货时，此时用户可以取消订单并申请退款
 * 当301商家已发货时，此时用户可以有确认收货的操作
 * 当401用户确认收货以后，此时用户可以进行的操作是删除订单，评价商品，或者再次购买
 * 当402系统自动确认收货以后，此时用户可以删除订单，评价商品，或者再次购买
 * 
 *
 * 目前不支持订单退货
 */
public class OrderUtil {

    public static final Short STATUS_CREATE = 101;
    public static final Short STATUS_PAY = 201;
    public static final Short STATUS_SHIP= 301;
    public static final Short STATUS_PART_SHIP= 302;
    public static final Short STATUS_REFUNDING_MONEY= 303;
    public static final Short STATUS_REFUND_MONEY= 304;
    
    
    public static final Short STATUS_CONFIRM = 401;
//    public static final Short STATUS_CONSUME = 501;
//    public static final Short STATUS_COMPLETE = 601;
    public static final Short STATUS_CANCEL= 102;
    public static final Short STATUS_REFUND = 202;
    public static final Short STATUS_AUTO_CONFIRM= 402;
    public static final Short STATUS_PART_CONFIRM= 403;
    
    public static final Short STATUS_FINISHED= 501;
    

    public static String orderStatusText(LitemallOrder order) {
        int status = order.getOrderStatus().intValue();

        if (status == 101) {
            return "未付款";
        }

        if (status == 102) {
            return "已取消";
        }

        if (status == 201) {
            return "已付款";
        }

        if (status == 202) {
            // 进一步跟踪退款状态
            return "已退款";
        }

        if (status == 301) {
            return "已发货";
        }
        
        if (status == 302) {
        	return "部分发货";
        }
        
        if (status == 303) {
        	return "退款中";
        }
        
        if (status == 304) {
        	return "已退款";
        }

        if (status == 401) {
            return "已收货";
        }

        if (status == 402) {
            return "已收货(系统)";
        }
        
        if (status == 403) {
        	return "部分收货";
        }
        
        if (status == 501) {
        	return "已完成";
        }

        Assert.state(false, "orderStatus不支持");
        return "";
    }


    public static String orderStatusText(int status) {

        if (status == 101) {
            return "未付款";
        }

        if (status == 102) {
            return "已取消";
        }

        if (status == 201) {
            return "已付款";
        }

        if (status == 202) {
            // 进一步跟踪退款状态
            return "已退款";
        }

        if (status == 301) {
            return "已发货";
        }

        if (status == 302) {
            return "部分发货";
        }

        if (status == 303) {
            return "退款中";
        }

        if (status == 304) {
            return "已退款";
        }

        if (status == 401) {
            return "已收货";
        }

        if (status == 402) {
            return "已收货(系统)";
        }

        if (status == 403) {
            return "部分收货";
        }

        if (status == 501) {
            return "已完成";
        }

        Assert.state(false, "orderStatus不支持");
        return "";
    }

    public static OrderHandleOption build(LitemallOrder order){
        int status = order.getOrderStatus().intValue();
        OrderHandleOption handleOption = new OrderHandleOption();

        if (status == 101) {
            // 如果订单没有被取消，且没有支付，则可支付，可取消
            handleOption.setCancel(true);
            handleOption.setPay(true);
        }
        else if (status == 102) {
            // 如果订单已经取消或是已完成，则可删除
            handleOption.setDelete(true);
            handleOption.setRebuy(true);
        }
        else if (status == 201) {
            // 如果订单已付款，没有发货或者部分发货，则可退款操作
            handleOption.setRefund(true); 
        }
        else if (status == 202 || status == 304) {
            // 如果订单已经取消或是已完成，则可删除
            handleOption.setDelete(true);
            handleOption.setRebuy(true);
        }
        else if (status == 301 ||  status == 302 ) {
            // 如果订单已经发货，没有收货，则可收货操作,
            // 此时不能取消订单
    		handleOption.setConfirm(true);
        }
        
        else if(status == 403) {
        	//部分收货
        	
        }
        else if (status ==  303) {
            // 退款中
        }  
        
        else if (status ==  401 || status == 402 || status == 304) {
            // 如果订单已经支付，且已经收货，则可完成交易、评论和再次购买
            handleOption.setDelete(true);
            handleOption.setComment(true);
            handleOption.setRebuy(true);
        }  
        
        else if (status ==  501) {
        	// 已完成
        }  
        
        else {
            Assert.state(false, "status不支持");
        }
        return handleOption;
    }
    
    public static OrderHandleOption detailBuild(LitemallOrder order,List<LitemallOrderGoods> orderGoodsList){
    	int status = order.getOrderStatus().intValue();
    	OrderHandleOption handleOption = new OrderHandleOption();
    	
    	if (status == 101) {
    		// 如果订单没有被取消，且没有支付，则可支付，可取消
    		handleOption.setCancel(true);
    		handleOption.setPay(true);
    	}
    	else if (status == 102) {
    		// 如果订单已经取消或是已完成，则可删除
    		handleOption.setDelete(true);
    		handleOption.setRebuy(true);
    	}
    	else if (status == 201) {
    		// 如果订单已付款，没有发货或者部分发货，则可退款操作
    		handleOption.setRefund(true); 
    	}
    	else if (status == 202 || status == 304) {
    		// 如果订单已经取消或是已完成，则可删除
    		handleOption.setDelete(true);
    		handleOption.setRebuy(true);
    	}
    	else if (status == 301 ||  status == 302 ) {
    		// 如果订单已经发货，没有收货，则可收货操作,
    		// 此时不能取消订单
    		handleOption.setConfirm(true);
    	}
    	
    	
    	else if(status == 403) {
    		//部分收货
    		for (LitemallOrderGoods orderGoods : orderGoodsList) {
    			if (orderGoods.getFlag().equals("1")) {
    				statusBuild(orderGoods.getOrderStatus());
    			} 
    		}
    	}
    	
    	else if (status ==  303) {
    		// 退款中
    		
    	}  
    	
    	else if (status ==  401 || status == 402 || status == 304) {
    		// 如果订单已经支付，且已经收货，则可完成交易、评论和再次购买
    		handleOption.setDelete(true);
    		handleOption.setComment(true);
    		handleOption.setRebuy(true);
    	}  
    	
    	 else if (status ==  501) {
         	// 已完成
         }  
    	
    	else {
    		Assert.state(false, "status不支持");
    	}
    	return handleOption;
    }
    
    public static OrderHandleOption statusBuild(int status ){
        OrderHandleOption handleOption = new OrderHandleOption();

        if (status == 101) {
            // 如果订单没有被取消，且没有支付，则可支付，可取消
            handleOption.setCancel(true);
            handleOption.setPay(true);
        }
        else if (status == 102) {
            // 如果订单已经取消或是已完成，则可删除
            handleOption.setDelete(true);
            handleOption.setRebuy(true);
        }
        else if (status == 201) {
            // 如果订单已付款，没有发货或者部分发货，则可退款操作
            handleOption.setRefund(true); 
        }
        else if (status == 202 || status == 304) {
            // 如果订单已经取消或是已完成，则可删除
            handleOption.setDelete(true);
            handleOption.setRebuy(true);
        }
        else if (status == 301 ||  status == 302 ) {
            // 如果订单已经发货，没有收货，则可收货操作,
            // 此时不能取消订单
    		handleOption.setConfirm(true);
        }
        
        else if(status == 403) {
        	//部分收货
        	
        }
        else if (status ==  303) {
            // 退款中
        }  
        
        else if (status ==  401 || status == 402 || status == 304) {
            // 如果订单已经支付，且已经收货，则可完成交易、评论和再次购买
            handleOption.setDelete(true);
            handleOption.setComment(true);
            handleOption.setRebuy(true);
        }  
        else if (status ==  501) {
        	// 已完成
        }  
        
        else {
            Assert.state(false, "status不支持");
        }
        return handleOption;
    }
    
    
    public static OrderHandleOption orderGoodsbuild(LitemallOrder litemall ,LitemallOrderGoods litemallOrderGoods){
    	OrderHandleOption handleOption = new OrderHandleOption();
        int status = litemallOrderGoods.getOrderStatus().intValue();
        String flag = litemallOrderGoods.getFlag();
        
        if(flag.equals("1")) {
        	//商品订单
        	
           if(status == 301) {
        	   	LocalDateTime nowTime  = LocalDateTime.now();        				
   				LocalDateTime orderTime = litemall.getShipStartTime().plusDays(15);
   			
	   			if(nowTime.compareTo(orderTime) < 0) {
	   				//已发货状态
	   				 handleOption.setReturn(true);
	   			}else {
	   				 handleOption.setReturn(false);
	   			}   	
           }
        }
        return handleOption;
    }

    public static List<Short> orderStatus(Integer showType){
        // 全部订单
        if (showType == 0) {
            return null;
        }

        List<Short> status = new ArrayList<Short>(2);

        if (showType.equals(1)) {
            // 待付款订单
            status.add((short)101);
        }
        else if (showType.equals(2)) {
            // 待发货订单
            status.add((short)201);
            status.add((short)302);
            status.add((short)403);
            status.add((short)304);
        }
        else if (showType.equals(3)) {
            // 待收货订单
            status.add((short)301);
            status.add((short)302);
            status.add((short)403);
            status.add((short)304);
        }
        else if (showType.equals(4)) {
            // 待评价订单
            status.add((short)401);
            status.add((short)403);
        }
        
        else if (showType.equals(5)) {
            // 已完成订单
            status.add((short)501);
        }
        else if (showType.equals(6)) {
        	// 已完成订单
        	status.add((short)202);
        	status.add((short)304);
        }
        else {
            Assert.state(false, "showType不支持");
        }
        return status;
    }
    
    
    public static List<Short> orderGoodsStatus(Integer showType){
    	// 全部订单
    	if (showType == 0) {
    		return null;
    	}
    	
    	List<Short> status = new ArrayList<Short>(2);
    	
    	if (showType.equals(1)) {
    		// 待付款订单
    		status.add((short)101);
    	}
    	else if (showType.equals(2)) {
    		// 待发货订单
    		status.add((short)201);
    		status.add((short)302);
    	}
    	else if (showType.equals(3)) {
    		// 待收货订单
    		status.add((short)301);
    		status.add((short)302);
    	}
    	else if (showType.equals(4)) {
    		// 待评价订单
    		status.add((short)401);
    	}
    	else if (showType.equals(5)) {
    		// 待评价订单
    		status.add((short)501);
    	}
    	else if (showType.equals(6)) {
        	// 已完成订单
        	status.add((short)202);
        	status.add((short)304);
        }
    	else {
    		Assert.state(false, "showType不支持");
    	}
    	return status;
    }


    public static boolean isPayStatus(LitemallOrder zmallOrder) {
        return OrderUtil.STATUS_PAY == zmallOrder.getOrderStatus().shortValue();
    }

    public static boolean isShipStatus(LitemallOrder zmallOrder) {
        return OrderUtil.STATUS_SHIP == zmallOrder.getOrderStatus().shortValue();
    }
    
    public static boolean isPartShipStatus(LitemallOrder zmallOrder) {
    	return OrderUtil.STATUS_PART_SHIP == zmallOrder.getOrderStatus().shortValue();
    }
    
    public static boolean isPartConfirmStatus(LitemallOrder zmallOrder) {
    	return OrderUtil.STATUS_PART_CONFIRM == zmallOrder.getOrderStatus().shortValue();
    }
}
