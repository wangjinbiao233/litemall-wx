package org.linlinjava.litemall.db.service;

import com.google.gson.Gson;
import org.linlinjava.litemall.db.dao.LitemallOrderGoodsMapper;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallReportParam;
import org.linlinjava.litemall.db.dto.SaleOrderReportDTO;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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
}
