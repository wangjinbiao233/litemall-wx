package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.oauth2.common.OAuth;
import org.linlinjava.litemall.admin.dao.AdminToken;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.admin.service.AdminTokenManager;
import org.linlinjava.litemall.admin.service.LitemallOauthService;
import org.linlinjava.litemall.admin.util.bcrypt.BCryptPasswordEncoder;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.service.LitemallAdminService;
import org.linlinjava.litemall.db.service.LitemallRoleService;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/login")
public class AuthController {
    private final Log logger = LogFactory.getLog(AuthController.class);

    @Autowired
    private LitemallAdminService adminService;

    @Autowired
    private LitemallOauthService oauthService;

    @Autowired
    private LitemallRoleService litemallRoleService;

    /*
     *  { username : value, password : value }
     */
    @PostMapping("/login")
    public Object login(@RequestBody String body){
        String username = JacksonUtil.parseString(body, "username");
        String password = JacksonUtil.parseString(body, "password");
        //访问设备（pc、ipad移动端）
        String browserEquipment = JacksonUtil.parseString(body, "browserEquipment");
        System.out.println("------------------------ browserEquipment =="+browserEquipment);

        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResponseUtil.badArgument();
        }

        List<LitemallAdmin> adminList = adminService.findAdmin(username);
        Assert.state(adminList.size() < 2, "同一个用户名存在两个账户");
        if(adminList.size() == 0){
            return ResponseUtil.badArgumentValue();
        }
        LitemallAdmin admin = adminList.get(0);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(password, admin.getPassword())){
            return ResponseUtil.fail(403, "账号密码不对");
        }

        Integer adminId = admin.getId();
        // token
        AdminToken adminToken = AdminTokenManager.generateToken(adminId);


        List<String> list =  litemallRoleService.selectAdminUserMenuByAdminId(adminId);

        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("token",adminToken.getToken());
        resMap.put("menuList",list);
        resMap.put("userId",adminId);
        
        if("pc".equals(browserEquipment)) {//pc端
        	List<String> pclist = new ArrayList<String>();
        	for(String menu:list) {
        		if("pad".equals(menu) || "doctor".equals(menu) || "storeManage".equals(menu) || "store".equals(menu) ) {
        			pclist.add(menu);
        		}
        	}
        	if(pclist.size()==list.size()) {
        		return ResponseUtil.fail(303, "没有权限登录");
        	}else {
        		resMap.put("browserEquipSelect", "isPc");
        	}
        }else {//移动端iPad端
        	List<String> padlist = new ArrayList<String>();
        	for(String menu:list) {
        		if("pad".equals(menu) || "doctor".equals(menu) ) {
        			padlist.add(menu);
        		}
        	}
        	if(padlist.size()<=0) {
        		return ResponseUtil.fail(303, "没有权限登录");
        	}else {
        		resMap.put("browserEquipSelect", "isiPad");
        	}
        }

        return ResponseUtil.ok(resMap);
    }

    /**
     * oAuth 认证登录
     */
    @GetMapping("oauth2Server")
    public void oauth2Server(HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception{

        redirectAttributes.addAttribute(OAuth.OAUTH_CLIENT_ID, oauthService.getClientId());
        redirectAttributes.addAttribute(OAuth.OAUTH_RESPONSE_TYPE, "code");
        redirectAttributes.addAttribute(OAuth.OAUTH_REDIRECT_URI, oauthService.getRedirectUrl());

        String redirect = oauthService.getOauthServerUrl();
        redirect += "?" + OAuth.OAUTH_CLIENT_ID + "=" + oauthService.getClientId();
        redirect += "&" + OAuth.OAUTH_RESPONSE_TYPE + "=code";
        redirect += "&" + OAuth.OAUTH_REDIRECT_URI + "=" + oauthService.getRedirectUrl();

        response.sendRedirect(redirect);
        // return "redirect:" + oauthService.getOauthServerUrl();
    }

    /**
     * oAuth 认证登录回调
     */
    @GetMapping("oauthCallback")
    public void oauthCallback(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("code")String code) throws Exception {

        if(code == null || code.isEmpty()) {
            // 授权未通过跳转到登陆界面
            response.sendRedirect(oauthService.getLitemallAdminPageURL());
        }

        LitemallAdmin litemallAdmin = oauthService.getAdminFormServerByToken(code, oauthService.getRedirectUrl());
        if(litemallAdmin == null) { // 无法获取用户信息 则跳转到登录界面
            response.sendRedirect(oauthService.getLitemallAdminPageURL());
        }

        List<LitemallAdmin> adminList = adminService.findAdmin(litemallAdmin.getUsername());
        if(adminList == null || adminList.size() == 0) { // 用户不存在则添加新用户
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(litemallAdmin.getPassword());
            litemallAdmin.setPassword(encodedPassword);
            adminService.add(litemallAdmin);
            adminList = adminService.findAdmin(litemallAdmin.getUsername());

            List<String> lstAdminRole = new ArrayList<String>();
            lstAdminRole.add("1");
            litemallRoleService.insertAdminRole(litemallAdmin.getId(), lstAdminRole);
        }

        litemallAdmin = adminList.get(0);
        Integer adminId = litemallAdmin.getId();
        // token
        AdminToken adminToken = AdminTokenManager.generateToken(adminId);
        String token = adminToken.getToken();
        String param = "?token=" + token;
        response.sendRedirect(oauthService.getLitemallAdminPageURL() + param);

        //return "redirect:" + oauthService.getLitemallAdminPageURL();
    }

    @RequestMapping("testOAuthLogin")
    public String testOAuthLogin(HttpServletResponse response) throws Exception{

        String username = "admin123";
        List<LitemallAdmin> adminList = adminService.findAdmin(username);

        LitemallAdmin litemallAdmin = adminList.get(0);
        Integer adminId = litemallAdmin.getId();
        System.out.println("------------->" + adminId);
        // token
        AdminToken adminToken = AdminTokenManager.generateToken(adminId);
        String token = adminToken.getToken();

        String litemallAdminPageUrl = "http://localhost:9527/#/oauthredirect?token=" + token;
        response.sendRedirect(litemallAdminPageUrl);

        return "redirect:" + litemallAdminPageUrl;
    }

    /*
     *
     */
    @PostMapping("/logout")
    public Object login(@LoginAdmin Integer adminId){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        return ResponseUtil.ok();
    }
}
