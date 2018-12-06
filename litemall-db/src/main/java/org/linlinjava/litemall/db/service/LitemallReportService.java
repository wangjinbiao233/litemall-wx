package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallDistributionProfitMapper;
import org.linlinjava.litemall.db.dao.LitemallLabelMapper;
import org.linlinjava.litemall.db.dao.LitemallOrderGoodsMapper;
import org.linlinjava.litemall.db.dao.LitemallUserMapper;
import org.linlinjava.litemall.db.domain.LitemallLabel;
import org.linlinjava.litemall.db.domain.LitemallReportParam;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.dto.AccountBalanceDTO;
import org.linlinjava.litemall.db.dto.SaleOrderReportDTO;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报表统计 service
 *
 * @author huanghaoqi
 * @version 1.0
 * @date 2018年09月27日 10:25:39
 * @since JDK1.8
 */
@Service
public class LitemallReportService {
    @Resource
    private LitemallOrderGoodsMapper litemallOrderGoodsMapper;
    @Resource
    private LitemallUserMapper litemallUserMapper;
    @Resource
    private LitemallLabelMapper litemallLabelMapper;

    /**
     * 方法描述  销售订单统计 -- 总数
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:27:08
     */
    public long saleOrderCount(LitemallReportParam param) {
        Long count = litemallOrderGoodsMapper.saleOrderCount(param);
        return count == null ? 0L : count;
    }

    /**
     * 方法描述  销售订单统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:01:10
     */
    public List<SaleOrderReportDTO> saleOrderList(LitemallReportParam param) {
        List<SaleOrderReportDTO> result = litemallOrderGoodsMapper.saleOrderList(param);
        if (!CollectionUtils.isEmpty(result)) {
            result.forEach(o -> {
                o.setOrderStatusName(OrderUtil.orderStatusText(o.getOrderStatus()));
                // 剩余疗程数
                Integer residueTreatmentNum = o.getResidueTreatmentNum();
                // 疗程总数
                Integer treatmentNumCount = o.getTreatmentNumCount();
                if (residueTreatmentNum != null && treatmentNumCount != null) {
                    Integer treatmentNum = treatmentNumCount - residueTreatmentNum;
                    o.setTreatmentNum(treatmentNum);
                }
                // 商品所属
                String goodsFlag = o.getGoodsFlag();
                if ("1".equals(goodsFlag)) {
                    o.setGoodsFlagName("实物商品");
                }
                if ("2".equals(goodsFlag)) {
                    o.setGoodsFlagName("服务类商品");
                }
            });
        }
        return result;
    }

    /**
     * 方法描述  销售执行统计 -- 总数
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:27:08
     */
    public long saleExcuteCount(LitemallReportParam param) {
        Long count = litemallOrderGoodsMapper.saleExcuteCount(param);
        return count == null ? 0L : count;
    }

    /**
     * 方法描述  销售执行统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:01:10
     */
    public List<SaleOrderReportDTO> saleExcuteList(LitemallReportParam param) {
        List<SaleOrderReportDTO> result = litemallOrderGoodsMapper.saleExcuteList(param);
        if (!CollectionUtils.isEmpty(result)) {
            result.forEach(o -> {
                // 商品所属
                String goodsFlag = o.getGoodsFlag();
                if ("1".equals(goodsFlag)) {
                    o.setGoodsFlagName("实物商品");
                }
                if ("2".equals(goodsFlag)) {
                    o.setGoodsFlagName("服务类商品");
                }
            });
        }
        return result;
    }

    /**
     * 方法描述  用户账户余额统计 -- 总数
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:27:08
     */
    public long accountBanlanceCount(LitemallReportParam param) {
        Long count = litemallUserMapper.accountBanlanceCount(param);
        return count == null ? 0L : count;
    }

    /**
     * 方法描述  用户账户余额统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:01:10
     */
    public List<AccountBalanceDTO> accountBanlanceList(LitemallReportParam param) {
        return litemallUserMapper.accountBanlanceList(param);
    }

    /**
     * 方法描述  用户对账明细统计 -- 总数
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:27:08
     */
    public long accountCheckCount(LitemallReportParam param) {
        Long count = litemallUserMapper.accountCheckCount(param);
        return count == null ? 0L : count;
    }

    /**
     * 方法描述  用户账对账明细统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:01:10
     */
    public List<AccountBalanceDTO> accountCheckList(LitemallReportParam param) {
        List<AccountBalanceDTO> result = litemallUserMapper.accountCheckList(param);
        result.forEach(a -> {
            Integer rechargeType = a.getRechargeType();
            String rechargeTypeName = "";
            if (rechargeType != null) {
                if (1 == rechargeType) {
                    rechargeTypeName = "银联";
                }
                if (2 == rechargeType) {
                    rechargeTypeName = "支付宝";
                }
                if (3 == rechargeType) {
                    rechargeTypeName = "微信";
                }
            }
            a.setRechargeTypeName(rechargeTypeName);
        });
        return result;

    }


    /**
     * 方法描述  分销报表统计 -- 总数
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:27:08
     */
    public long distributionReportCount(LitemallReportParam param) {
        Long count = litemallOrderGoodsMapper.distributionReportCount(param);
        return count == null ? 0L : count;
    }

    /**
     * 方法描述  分销报表统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 14:01:10
     */
    public List<SaleOrderReportDTO> distributionReportList(LitemallReportParam param) {
        List<SaleOrderReportDTO> result = litemallOrderGoodsMapper.distributionReportList(param);
        if (!CollectionUtils.isEmpty(result)) {
            result.forEach(o -> {
                o.setOrderStatusName(OrderUtil.orderStatusText(o.getOrderStatus()));
                // 剩余疗程数
                Integer residueTreatmentNum = o.getResidueTreatmentNum();
                // 疗程总数
                Integer treatmentNumCount = o.getTreatmentNumCount();
                if (residueTreatmentNum != null && treatmentNumCount != null) {
                    Integer treatmentNum = treatmentNumCount - residueTreatmentNum;
                    o.setTreatmentNum(treatmentNum);
                }
                // 商品所属
                String goodsFlag = o.getGoodsFlag();
                if ("1".equals(goodsFlag)) {
                    o.setGoodsFlagName("实物商品");
                }
                if ("2".equals(goodsFlag)) {
                    o.setGoodsFlagName("服务类商品");
                }
                // 分销商
                Integer distributionId = o.getDistributionId();
                if (distributionId != null) {
                    LitemallUser litemallUser = litemallUserMapper.selectByPrimaryKey(distributionId);
                    if (litemallUser != null) {
                        o.setDistributionName(litemallUser.getUsername());
                    }
                    // 分销商标签
                    List<LitemallLabel> litemallLabelList = litemallLabelMapper.selectByDistributionId(distributionId);
                    if (!CollectionUtils.isEmpty(litemallLabelList)) {
                        List<String> names=new ArrayList<>();
                        litemallLabelList.forEach(litemallLabel -> {
                            String name=litemallLabel.getLabelName();
                            if(!StringUtils.isEmpty(name)){
                                names.add(name);
                            }
                        });
                        if(!CollectionUtils.isEmpty(names)){
                            o.setDistributionLabelNames(String.join(",", names));
                        }
                    }
                }
                // 操作类型
                Integer operationType = o.getOperationType();
                BigDecimal profitMoney = o.getProfitMoney();
                String operationName = "-";
                if (operationType != null) {
                    if (operationType == 1) {
                        operationName = "订单佣金";
                        if (profitMoney != null) {
                            profitMoney = new BigDecimal("+" + profitMoney);
                        }
                    }
                    if (operationType == 2) {
                        operationName = "提现";
                        if (profitMoney != null) {
                            profitMoney = new BigDecimal("-" + profitMoney);
                        }
                    }
                    if (operationType == 3) {
                        operationName = "充值";
                        if (profitMoney != null) {
                            profitMoney = new BigDecimal("+" + profitMoney);
                        }
                    }
                    if (operationType == 4) {
                        operationName = "消费";
                        if (profitMoney != null) {
                            profitMoney = new BigDecimal("-" + profitMoney);
                        }
                    }
                    if (operationType == 5) {
                        operationName = "退款";
                        if (profitMoney != null) {
                            profitMoney = new BigDecimal("+" + profitMoney);
                        }
                    }
                    if (operationType == 6) {
                        operationName = "提现失败";
                    }
                }
                o.setOperationTypeName(operationName);
                o.setProfitMoney(profitMoney);
            });
        }
        return result;
    }

    /**
     * 方法描述  获取所有分销商的下级和下下级ID
     *
     * @author huanghaoqi
     * @date 2018年11月28日 09:40:44
     */
    public List<Integer> listDistributionUserId() {
        List<Integer> result = new ArrayList<>();
        // 分销商下级
        List<Integer> nextDistributionUserIds = litemallUserMapper.listNextDistributionUserIds();
        if (!CollectionUtils.isEmpty(nextDistributionUserIds)) {
            result.addAll(nextDistributionUserIds);
        }
        // 分销商下下级ID
        List<Integer> nextNextDistributionUserIds = litemallUserMapper.listNextNextDistributionUserIds();
        if (!CollectionUtils.isEmpty(nextNextDistributionUserIds)) {
            result.addAll(nextNextDistributionUserIds);
        }
        return result;
    }
}
