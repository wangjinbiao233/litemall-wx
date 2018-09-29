package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallOrderGoodsMapper;
import org.linlinjava.litemall.db.dao.LitemallUserMapper;
import org.linlinjava.litemall.db.domain.LitemallReportParam;
import org.linlinjava.litemall.db.dto.AccountBalanceDTO;
import org.linlinjava.litemall.db.dto.SaleOrderReportDTO;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

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
}
