package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallReportParam;
import org.linlinjava.litemall.db.dto.AccountBalanceDTO;
import org.linlinjava.litemall.db.dto.SaleOrderReportDTO;
import org.linlinjava.litemall.db.service.LitemallReportService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表统计
 *
 * @author huanghaoqi
 * @version 1.0
 * @date 2018年09月27日 13:49:15
 * @since JDK1.8
 */
@RestController
@RequestMapping("/admin/report")
public class ReportController {
    private final Log logger = LogFactory.getLog(ReportController.class);

    @Autowired
    private LitemallReportService reportService;

    /**
     * 方法描述  销售订单统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 13:49:44
     */
    @PostMapping("/saleOrderList")
    public Object saleOrderList(@LoginAdmin Integer adminId,
                                LitemallReportParam param) {
        if (adminId == null) {
            return ResponseUtil.unlogin();
        }
        Map<String, Object> data = new HashMap<>();
        int total = (int) reportService.saleOrderCount(param);
        if (total == 0) {
            data.put("total", total);
            data.put("items", new ArrayList<>(0));
            return ResponseUtil.ok(data);
        }
        List<SaleOrderReportDTO> result = reportService.saleOrderList(param);
        data.put("total", total);
        data.put("items", result);
        return ResponseUtil.ok(data);
    }

    /**
     * 方法描述  销售执行统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 13:49:44
     */
    @PostMapping("/saleExcuteList")
    public Object saleExcuteList(@LoginAdmin Integer adminId,
                                LitemallReportParam param) {
        if (adminId == null) {
            return ResponseUtil.unlogin();
        }
        Map<String, Object> data = new HashMap<>();
        int total = (int) reportService.saleExcuteCount(param);
        if (total == 0) {
            data.put("total", total);
            data.put("items", new ArrayList<>(0));
            return ResponseUtil.ok(data);
        }
        List<SaleOrderReportDTO> result = reportService.saleExcuteList(param);
        data.put("total", total);
        data.put("items", result);
        return ResponseUtil.ok(data);
    }

    /**
     * 方法描述  用户账户余额统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 13:49:44
     */
    @PostMapping("/accountBanlanceList")
    public Object accountBanlanceList(@LoginAdmin Integer adminId,
                                 LitemallReportParam param) {
        if (adminId == null) {
            return ResponseUtil.unlogin();
        }
        Map<String, Object> data = new HashMap<>();
        int total = (int) reportService.accountBanlanceCount(param);
        if (total == 0) {
            data.put("total", total);
            data.put("items", new ArrayList<>(0));
            return ResponseUtil.ok(data);
        }
        List<AccountBalanceDTO> result = reportService.accountBanlanceList(param);
        data.put("total", total);
        data.put("items", result);
        return ResponseUtil.ok(data);
    }
}
