package org.linlinjava.litemall.wx.util.weixin;

import org.linlinjava.litemall.db.util.WeixinConfig;

/**
 * 
 * @author 宁世洋
 * 根据code请求微信的openid和session_key
 *
 */
public class OpenIdUtil {
	public static String oauth2GetOpenid(String code) {
        String appid=WeixinConfig.WX_AppId;
        String appsecret=WeixinConfig.WX_Secret;
        //授权（必填）
        String grant_type = "authorization_code";
        //URL
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        //请求参数
        String params = "appid=" + appid + "&secret=" + appsecret + "&js_code=" + code + "&grant_type=" + grant_type;
        //发送请求
        String str = HttpUtil.get(requestUrl, params);
        
        return str;
	}
	
}
