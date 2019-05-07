package org.linlinjava.litemall.admin.config;


import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.util.OrderHandleOption;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    @Autowired
    private LitemallOrderService orderService;
    @Autowired
    private LitemallOrderGoodsService orderGoodsService;

    //每周一到周五早上5点执行，自动收货
    @Scheduled(cron = "0 0 5 * * ?")
    public void execute() {
        System.out.print(LocalDateTime.now() + " -- execute自动收货定时任务 \n");
        //查询所有7天前的待收货订单（包括实物商品与服务商品）
        List<LitemallOrder> orderlist = orderService.queryOrderByStatusShip(OrderUtil.STATUS_SHIP);
        if(orderlist != null && orderlist.size() > 0){
            for (LitemallOrder order: orderlist) {

                OrderHandleOption handleOption = OrderUtil.build(order);
                if (!handleOption.isConfirm()) {
                    continue;
                }
                //查询订单商品表详情
                List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(order.getId());

                //是否可以自动收货标识
                boolean isConfirm = true;

                List<Short> orderStatusList = new ArrayList<Short>();
                for (LitemallOrderGoods orderGoods : litemallOrderGoodsList) {
                    orderStatusList.add(orderGoods.getOrderStatus());

                    //订单详情中的订单状态不等于[商家发货，用户未确认；或已预约]的订单，则不能自动收货
                    if(!orderGoods.getOrderStatus().equals(OrderUtil.STATUS_SHIP)){
                        isConfirm = false;
                        continue;
                    }
                }
                if(isConfirm == true){
                    for (LitemallOrderGoods litemallOrderGoods : litemallOrderGoodsList) {
                        if(orderStatusList.contains(OrderUtil.STATUS_PAY)
                                || orderStatusList.contains(OrderUtil.STATUS_PART_SHIP)
                                || orderStatusList.contains(OrderUtil.STATUS_PART_CONFIRM)
                                || litemallOrderGoods.getTreatmentNum() != 0) {
                            // 订单中还有未发货，部分发货,未预约的订单
                            isConfirm = false;
                        } else {
                            litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_AUTO_CONFIRM);
                            orderGoodsService.update(litemallOrderGoods);
                        }
                    }
                }
                //验证是否可自动收货
                if (isConfirm == true) {
                    // 自动收货,更新订单收货时间，状态
                    order.setOrderStatus(OrderUtil.STATUS_AUTO_CONFIRM);
                    order.setConfirmTime(LocalDateTime.now());
                    orderService.update(order);
                }
            }
        }





    }
}
