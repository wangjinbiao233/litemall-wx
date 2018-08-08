package org.linlinjava.litemall.wx.web;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import me.chanjar.weixin.common.exception.WxErrorException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallSerialNumber;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallSerialNumberService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.wx.dao.UserInfo;
import org.linlinjava.litemall.wx.dao.UserToken;
import org.linlinjava.litemall.wx.service.UserTokenManager;
import org.linlinjava.litemall.wx.util.IpUtil;
import org.linlinjava.litemall.wx.util.bcrypt.BCryptPasswordEncoder;
import org.linlinjava.litemall.wx.util.weixin.OpenIdUtil;
import org.linlinjava.litemall.wx.util.weixin.WXBizDataCrypt;
import org.linlinjava.litemall.wx.util.weixin.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/auth")
public class WxAuthController {
    private final Log logger = LogFactory.getLog(WxAuthController.class);

    @Autowired
    private LitemallUserService userService;

    @Autowired
    private WxMaService wxService;
    
    @Autowired
    private WeixinUtil weixinUtil;
    
    @Autowired
    private LitemallSerialNumberService serialNumberService;

    /**
     * 账号登录
     */
    @RequestMapping("login")
    public Object login(@RequestBody String body, HttpServletRequest request) {
    	Integer userId = JacksonUtil.parseInteger(body, "userId");
        String username = JacksonUtil.parseString(body, "username");
        String password = JacksonUtil.parseString(body, "password");
        if(username == null || password == null){
            return ResponseUtil.badArgument();
        }

        List<LitemallUser> userList =userService.queryByUsername(username);
        LitemallUser user = null;
        if(userList.size() > 1){
            return ResponseUtil.fail502();
        }
        else if(userList.size() == 0){
            return ResponseUtil.badArgumentValue();
        }
        else {
            user = userList.get(0);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(password, user.getPassword())){
            return ResponseUtil.fail(403, "账号密码不对");
        }

        // userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(username);
        userInfo.setAvatarUrl(user.getAvatar());

        // token
        UserToken userToken = UserTokenManager.generateToken(user.getId());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("isDistributionPartner", user.getDistributionPartner());
        result.put("userId", user.getId());
        result.put("token", userToken.getToken());
        result.put("tokenExpire", userToken.getExpireTime().toString());
        result.put("userInfo", user);
        return ResponseUtil.ok(result);
    }

    /**
     * 微信登录
     */
    @RequestMapping("login_by_weixin")
    public Object loginByWeixin(@RequestBody String body, HttpServletRequest request) {
    	try {
    		String code = JacksonUtil.parseString(body, "code");
    		String iv = JacksonUtil.parseString(body, "iv");
    		String encryptedData = JacksonUtil.parseString(body, "encryptedData");
    		String referrerId = JacksonUtil.parseString(body, "pId");
    		UserInfo userInfo = JacksonUtil.parseObject(body, "userInfo", UserInfo.class);
    		
    		if(code == null || userInfo == null){
 	            return ResponseUtil.badArgument();
 	        }
    		if(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(iv) && StringUtils.isNotBlank(encryptedData)){
    			String res = OpenIdUtil.oauth2GetOpenid(code);
    			JSONObject jsonObject = JSONObject.fromObject(res);
    			if(jsonObject.containsKey("session_key") && jsonObject.containsKey("openid")){
    				String sessionKey = (String) jsonObject.get("session_key");
    				String openid = (String) jsonObject.get("openid");
    			
    				String deString = WXBizDataCrypt.getInstance().decrypt(encryptedData, sessionKey, iv, "utf-8");
    				JSONObject jsonObject1 = JSONObject.fromObject(deString);
    				String unionid = (String) jsonObject1.get("unionId");
    				
    				String userName = userInfo.getNickName();
    				if(StringUtils.isNotBlank(userName))
    					userName = weixinUtil.filterEmoji(userName);
    				else 
    					userName = "";
    				
    	    		LitemallUser user = userService.queryByOid(openid);
    	    		if(user == null){
    	    			user = new LitemallUser();
    	    			
    	    			user.setUsername(userName);  // 其实没有用，因为用户没有真正注册
    	    			user.setPassword(openid);                  // 其实没有用，因为用户没有真正注册
    	    			user.setWeixinOpenid(openid);
    	    			user.setUnionid(unionid);
    	    			user.setAvatar(userInfo.getAvatarUrl());
    	    			user.setNickname(userName);
    	    			user.setGender(userInfo.getGender() == 1 ? "男" : "女");
    	    			user.setUserLevel("普通用户");
    	    			user.setStatus("可用");
    	    			user.setAddTime(LocalDateTime.now());
    	    			user.setLastLoginTime(LocalDateTime.now());
    	    			user.setLastLoginIp(IpUtil.client(request));
    	    			user.setDistributionPartner(false);
    	    			
    	    			user.setMemberId(getSerialNumber());
    	    			//我的推荐人
    	    			if(referrerId != null && !referrerId.equals("")) {
    	    				LitemallUser litemallUser = userService.findById(Integer.valueOf(referrerId));
    	    				if(litemallUser != null)
    	    					user.setpId(litemallUser.getId());
    	    			}
    	    			userService.add(user);
    	    		} else {
    	    			if(StringUtils.isBlank(user.getMemberId())) {
    	    				user.setMemberId(getSerialNumber());
    	    			}
    	    			user.setAvatar(userInfo.getAvatarUrl());
    	    			user.setUsername(userName);
    	    			user.setNickname(userName);
    	    			user.setUnionid(unionid);
    	    			user.setLastLoginTime(LocalDateTime.now());
    	    			user.setLastLoginIp(IpUtil.client(request));
    	    			userService.update(user);
    	    			if("禁用".equals(user.getStatus())) {
    	    				return ResponseUtil.fail(-2, "该用户已被禁用！");
    	    			}
    	    		}
    	    		
    	    		// token
    	    		UserToken userToken = UserTokenManager.generateToken(user.getId());
    	    		
    	    		Map<Object, Object> result = new HashMap<Object, Object>();
    	    		result.put("isDistributionPartner", user.getDistributionPartner());
    	    		result.put("userId", user.getId());
    	    		result.put("token", userToken.getToken());
    	    		result.put("tokenExpire", userToken.getExpireTime().toString());
    	    		result.put("userInfo", user);
    	    		return ResponseUtil.ok(result);
    				
    			}
    		}

    	
    		
    	
    	} catch (Exception e) {
    		logger.error("error : loginByWeixin --》  ");
    		logger.error(e);
		}
		return ResponseUtil.fail();
    }
    
    public String getSerialNumber() {
    	Calendar now = Calendar.getInstance();  
		String year = now.get(Calendar.YEAR)+"";
		String month = (now.get(Calendar.MONTH) + 1)+"";
		String day = now.get(Calendar.DAY_OF_MONTH)+"";
		LitemallSerialNumber serialNumber = serialNumberService.findSerialNumberByType("MEMBER_ID");
		Integer number = serialNumber.getSerialNumber();
		if(number < 9999) {
			serialNumber.setSerialNumber(number + 1);
		} else {
			serialNumber.setSerialNumber(1);
		}
		int length = number.toString().length();
		String sort = "";
		if(length == 1) {
			sort = "000"+number;
		} else if(length == 2) {
			sort = "00"+number;
		} else if(length == 3) {
			sort = "0"+number;
		} else if(length == 4) {
			sort = ""+number;
		} else {
			sort = "0000";
		}
		
		if(month.length() == 1) {
			month = "0" + month;
		}
		
		if(day.length() == 1) {
			day = "0" + day;
		}
		
		serialNumberService.updateSerialNumber(serialNumber);
		String memberId = year + sort + month + day;
		return memberId;
    }
    
    
    /**
     * 账号注册
     */
    @PostMapping("register")
    public Object register(@RequestBody String body, HttpServletRequest request) {
        String username = JacksonUtil.parseString(body, "username");
        String password = JacksonUtil.parseString(body, "password");
        String mobile = JacksonUtil.parseString(body, "mobile");
        String code = JacksonUtil.parseString(body, "code");

        if(username == null || password == null || mobile == null || code == null){
            return ResponseUtil.badArgument();
        }

        List<LitemallUser> userList = userService.queryByUsername(username);
        if(userList.size() > 0){
            return ResponseUtil.fail(403, "用户名已注册");
        }

        userList = userService.queryByMobile(mobile);
        if(userList.size() > 0){
            return ResponseUtil.fail(403, "手机号已注册");
        }
        LitemallUser user = new LitemallUser();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);

        user = new LitemallUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setMobile(mobile);
        user.setWeixinOpenid("");
        user.setAvatar("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
        user.setNickname(username);
        user.setGender("未知");
        user.setUserLevel("普通用户");
        user.setStatus("可用");
        user.setAddTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.client(request));
        user.setDistributionPartner(false);
        userService.add(user);


        // userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(username);
        userInfo.setAvatarUrl(user.getAvatar());

        // token
        UserToken userToken = UserTokenManager.generateToken(user.getId());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("isDistributionPartner", false);
        result.put("userId", user.getId());
        result.put("token", userToken.getToken());
        result.put("tokenExpire", userToken.getExpireTime().toString());
        result.put("userInfo", user);
        return ResponseUtil.ok(result);
    }

    /**
     * 账号密码重置
     */
    @PostMapping("reset")
    public Object reset(@RequestBody String body, HttpServletRequest request) {
        String password = JacksonUtil.parseString(body, "password");
        String mobile = JacksonUtil.parseString(body, "mobile");
        String code = JacksonUtil.parseString(body, "code");

        if(mobile == null || code == null || password == null){
            return ResponseUtil.badArgument();
        }

        List<LitemallUser> userList = userService.queryByMobile(mobile);
        LitemallUser user = null;
        if(userList.size() > 1){
            return ResponseUtil.serious();
        }
        else if(userList.size() == 0){
            return ResponseUtil.fail(403, "手机号未注册");
        }
        else{
            user = userList.get(0);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);

        userService.update(user);

        return ResponseUtil.ok();
    }
    
    /**
     * 给用户添加分销权限
     * @param body
     * @param request
     * @return
     */
    @PostMapping("addDistribution")
    public Object addDistribution(@RequestBody String body, HttpServletRequest request) {
        Integer userId = JacksonUtil.parseInteger(body, "userId");
        if(userId == null || userId == 0){
            return ResponseUtil.badArgument();
        }
        LitemallUser user = userService.findById(userId);
        Map<Object, Object> result = new HashMap<Object, Object>();
        if(user != null) {
        	try {
        		String accessToken = wxService.getAccessToken();
        		String qrcodeUrl = weixinUtil.getQRcode(accessToken, userId.toString());
        		if(StringUtils.isNotBlank(qrcodeUrl)) {
        			user.setDistributionPartner(true);
        			user.setQrcodeUrl(qrcodeUrl);
        			userService.update(user);
        			result.put("isDistributionPartner", true);
        		} else {
        			result.put("isDistributionPartner", false);
        		}
        		result.put("userInfo", user);
        	} catch (WxErrorException e) {
        		e.printStackTrace();
        	}
        } else 
        	result.put("isDistributionPartner", false);
			
        
        return ResponseUtil.ok(result);
    }
    
    /**
     * 获取我的一级团队人数
     * @param body
     * @param request
     * @return
     */
    @RequestMapping("getSubUserCount")
    public Object getSubUserCount(@RequestBody String body, HttpServletRequest request) {
    	String userId = JacksonUtil.parseString(body, "userId");
		if(userId == null){
	        return ResponseUtil.badArgument();
	    }
		int count = userService.findSubUserCount(Integer.valueOf(userId));
		Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("subUserCount", count);
    	return ResponseUtil.ok(result);
    }
    
    /**
     * 获取我的二级团队人数
     * @param body
     * @param request
     * @return
     */
    @RequestMapping("getSubSubUserCount")
    public Object getSubSubUserCount(@RequestBody String body, HttpServletRequest request) {
    	String userId = JacksonUtil.parseString(body, "userId");
		if(userId == null){
	        return ResponseUtil.badArgument();
	    }
		int count = userService.findSubSubUserCount(Integer.valueOf(userId));
		Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("subSubUserCount", count);
    	return ResponseUtil.ok(result);
    }
    
    /**
     * 获取我的团队人数
     */
    @RequestMapping(value = "getSubUserTotalCount")
    public Object getSubUserTotalCount(@RequestBody String body, HttpServletRequest request) {
    	String userId = JacksonUtil.parseString(body, "userId");
		if(userId == null){
	        return ResponseUtil.badArgument();
	    }
		int subCount = userService.findSubUserCount(Integer.valueOf(userId));
		int subSubCount = userService.findSubSubUserCount(Integer.valueOf(userId));
		
		Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("subUserCount", subCount + subSubCount);
		return ResponseUtil.ok(result);
    }
}
