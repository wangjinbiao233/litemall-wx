package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.util.entity.MessageData;
import org.linlinjava.litemall.db.domain.LitemallDictionary;
import org.linlinjava.litemall.db.domain.LitemallDistributionApply;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallDictionaryService;
import org.linlinjava.litemall.db.service.LitemallDistributionApplyService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.entity.AccessToken;
import org.linlinjava.litemall.db.util.weixin.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/distributionApply")
public class DistributionApplyController {
	private final Log logger = LogFactory.getLog(DistributionApplyController.class);

	@Autowired
    private LitemallDistributionApplyService litemallDistributionApplyService;
	
	@Autowired
    private LitemallUserService userService;
	
	@Autowired
	private LitemallDictionaryService litemallDictionaryService;

	
	 @RequestMapping(value = "/distributionList", method = RequestMethod.POST)  
	    public Object reserveList(@LoginAdmin Integer adminId,LitemallDistributionApply litemallDistributionApply, 
	    		           @RequestParam(value = "page", defaultValue = "1") Integer page,
	                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,String sort, String order){
	        if(adminId == null){
	            return ResponseUtil.fail401();
	        }
	                
	        List<LitemallDistributionApply> litemallDistributionApplyList = litemallDistributionApplyService.queryDistributionApply(litemallDistributionApply, page, limit, sort, order);        
	        int total = litemallDistributionApplyService.countDistributionApply(litemallDistributionApply, page, limit, sort, order);

	        Map<String, Object> data = new HashMap<>();
	        data.put("total", total);
	        data.put("items", litemallDistributionApplyList);
	        return ResponseUtil.ok(data);
	    }    
	    
	    
	    /*
	     * 目前仅仅支持管理员设置发货相关的信息
	     */
	    @PostMapping("/update")
	    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallDistributionApply litemallDistributionApply){
	        if(adminId == null){
	            return ResponseUtil.unlogin();
	        }
	        litemallDistributionApplyService.update(litemallDistributionApply);
	        
	        LitemallUser user = userService.findById(litemallDistributionApply.getCreateUserId());
	        if(litemallDistributionApply.getAuditStatus() == 1) {
	        	user.setDistributionPartner(true);
  				userService.update(user);
	        }
	      
			String result = "";
			String content = "";
			
			if(litemallDistributionApply.getAuditStatus() == 1) {
				//审批通过
				result = "审核通过";
				content = "恭喜您通过分销申请";	
			}else {
				//审批驳回
				result = "审核未通过";
				content = "驳回理由为"+litemallDistributionApply.getRemark();	
			}
			
			if(user.getFormId() != null) {
				MessageData msgData = new MessageData();
				msgData.setKeyword1(user.getUsername());
				msgData.setKeyword2("分销审核认证");
				msgData.setKeyword3(result);
				msgData.setKeyword4(content);
				AccessToken accessToken = WeixinUtil.getAccessToken();
				if(accessToken != null) {
					WeixinUtil.sendWXMessage(accessToken.getToken(),user.getWeixinOpenid(), user.getFormId(), msgData);
				}
			}
			
			
	        return ResponseUtil.ok(litemallDistributionApply);
	    }
	    
	    

		/**
		 * 查询可提取收益列表 + 提现明细列表
		 *
		 * @param litemallDictionary
		 * @return
		 */
	    @RequestMapping(value = "/selectDistributionTypeList", method = RequestMethod.POST)  
		public Object selectDistributionTypeList(LitemallDictionary litemallDictionary) {
			List<LitemallDictionary> list = litemallDictionaryService.selectDictionaryList(litemallDictionary);
			return ResponseUtil.ok(list);
		}
		

}
