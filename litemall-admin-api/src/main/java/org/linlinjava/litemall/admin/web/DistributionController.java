package org.linlinjava.litemall.admin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallReportParam;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.dto.SaleOrderReportDTO;
import org.linlinjava.litemall.db.service.LitemallDistributionProfitService;
import org.linlinjava.litemall.db.service.LitemallRechargeService;
import org.linlinjava.litemall.db.service.LitemallReportService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/distribution")
public class DistributionController {
    private final Log logger = LogFactory.getLog(DistributionController.class);

    @Autowired
    private LitemallDistributionProfitService litemallDistributionProfitService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallDistributionProfitService distributionProfitService;

    @Autowired
    private LitemallRechargeService litemallRechargeService;
    @Autowired
    private LitemallReportService litemallReportService;


    /**
     * 查询我的上级
     *
     * @param userId
     * @return
     */
    @RequestMapping("selectParentUserInfoByUserId")
    public Object selectParentUserInfoByUserId(@RequestParam("id") Integer userId) {
        if (userId == null) {
            return ResponseUtil.fail401();
        }
        List<LitemallUser> userList = userService.findParentUserById(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("items", userList);
        return ResponseUtil.ok(data);
    }

    /**
     * 我的下级
     *
     * @param userId
     * @return
     */
    @RequestMapping("selectSubUserInfoByUserId")
    public Object selectSubUserInfoByUserId(@RequestParam("id") Integer userId) {

        Map<String, Object> map = litemallDistributionProfitService.selectSubUserInfoByUserId(userId, 1, 500);

        return ResponseUtil.ok(map);
    }


    /**
     * 我的下下级
     *
     * @param userId
     * @return
     */
    @RequestMapping("selectSubSubUserInfoByUserId")
    public Object selectSubSubUserInfoByUserId(@RequestParam("id") Integer userId) {

        Map<String, Object> map = litemallDistributionProfitService.selectSubSubUserInfoByUserId(userId, 1, 1000);

        return ResponseUtil.ok(map);
    }

    /**
     * 充值记录
     *
     * @param userId
     * @return
     */
    @RequestMapping("selectTransactionRecord")
    public Object selectTransactionRecord(@RequestParam("id") Integer userId) {
        List<Map> list = litemallRechargeService.findTransactionRecord(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("items", list);
        return ResponseUtil.ok(data);
    }

    /**
     * 充值
     *
     * @param adminId
     * @param userId
     * @return
     */
    @RequestMapping("recharge")
    public Object recharge(
            @LoginAdmin Integer adminId,
            Double money, Integer userId, Integer rechargeType) {
        if (adminId == null) {
            return ResponseUtil.unlogin();
        }
        if (money == null || userId == null) {
            return ResponseUtil.fail401();
        }
        int count = litemallRechargeService.rechargeMoneyByUserId(money, userId, adminId, rechargeType);
        Map<String, Object> data = new HashMap<>();
        data.put("state", count);
        return ResponseUtil.ok(data);
    }

    /**
     * 方法描述  分销报表统计
     *
     * @author huanghaoqi
     * @date 2018年09月27日 13:49:44
     */
    @PostMapping("/distributionReportList")
    public Object distributionReportList(@LoginAdmin Integer adminId,
                                         LitemallReportParam param) {
        if (adminId == null) {
            return ResponseUtil.unlogin();
        }
        Map<String, Object> data = new HashMap<>();

        int total = (int) litemallReportService.distributionReportCount(param);
        if (total == 0) {
            data.put("total", total);
            data.put("items", new ArrayList<>(0));
            return ResponseUtil.ok(data);
        }
        List<SaleOrderReportDTO> result = litemallReportService.distributionReportList(param);
        data.put("total", total);
        data.put("items", result);
        return ResponseUtil.ok(data);
    }
}
