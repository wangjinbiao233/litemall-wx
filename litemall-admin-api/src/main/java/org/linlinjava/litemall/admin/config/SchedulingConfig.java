package org.linlinjava.litemall.admin.config;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private final Log logger = LogFactory.getLog(SchedulingConfig.class);
    @Autowired
    private LitemallOrderService orderService;
    @Autowired
    private LitemallOrderGoodsService orderGoodsService;

    //每周一至周五早上5点执行，自动收货
    @Scheduled(cron = "0 0 5 ? * MON-FRI")
    public void execute() {
        logger.info("batch Automatic receiving started");
        System.out.print(LocalDateTime.now() + " -- execute自动收货定时任务 \n");
        //查询超过7天，用户未确认收货的订单列表（包括实物商品与服务商品）
        //订单状态为商家发货，用户未确认收货的实物商品；或已预约完的服务商品
        List<LitemallOrder> orderlist = orderService.queryOrderByStatusShip(OrderUtil.STATUS_SHIP);
        if(orderlist != null && orderlist.size() > 0){
            for (LitemallOrder order: orderlist) {

                //判断订单状态是否可自动收货
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
        logger.info("batch Automatic receiving end");
    }

    //每周一至周五上午4点执行，计算佣金
    @Scheduled(cron = "0 0 4 ? * MON-FRI")
    public void AutoSettlementCommission() {
        logger.info("batch Automatic compute order started");
        System.out.print(LocalDateTime.now() + " -- execute自动计算订单佣金定时任务 \n");

        //查询是否有待结算订单
        List<Integer> list = orderGoodsService.findForTheOrder();
        if(list != null && list.size() > 0){
            list.forEach((id)-> System.out.println("订单id ---->  "+id));
            orderGoodsService.autoSettlementCommission();
        }
        logger.info("batch Automatic compute order end");

    }
}
