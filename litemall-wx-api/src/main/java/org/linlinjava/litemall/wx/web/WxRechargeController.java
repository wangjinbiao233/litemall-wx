package org.linlinjava.litemall.wx.web;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallRechargeService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/recharge")
public class WxRechargeController {
	
	@Autowired
    private LitemallUserService userService;

	@Autowired
	private LitemallRechargeService litemallRechargeService;
	
	
	 /**
     * 充值记录
     * @param userId
     * @return
     */
    @RequestMapping("selectRechargeRecord")
    public Object selectRechargeRecord(@RequestBody String body, HttpServletRequest request) {
    	Integer userId = JacksonUtil.parseInteger(body, "userId");
    	LitemallUser litemallUser = userService.findById(userId);
    	List<Map> list = litemallRechargeService.findTransactionRecord(userId);
		Map<String, Object> data = new HashMap<>();
		data.put("items", list);
		data.put("rechargeMoney", litemallUser.getRechargeMoney());
    	return ResponseUtil.ok(data);
    }
	

}
