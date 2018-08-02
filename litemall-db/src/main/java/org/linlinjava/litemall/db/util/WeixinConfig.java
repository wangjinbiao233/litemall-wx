package org.linlinjava.litemall.db.util;

import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import com.github.binarywang.wxpay.config.WxPayConfig;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeixinConfig {
	
	/*********通誉微商配置参数*********/
    public static final String WX_AppId = "wxacd05a47e7d66d21";
    public static final String WX_Secret = "bc9ddccef0dd1a1a9338b07c5287854d";
    public static final String WX_Token = "";
    public static final String WX_AesKey = "";
    public static final String WX_MsgDataFormat = "JSON";


    public static final String WX_MchId = "1408172302";
    public static final String WX_MchKey = "r4re3redsw3e5tg9oi87hy65tbvgt5ws";
    public static final String WX_KeyPath = "";
    
    //微信消息管理模板ID
    public static final String TEMPLATE_ID = "g0SUhZ9CLRYDVsKn15QqnAiNYA9nazzlu0zN7FZ9fYo";

    public static final String CERT_PATH = "D:\\\\cert\\\\apiclient_cert.p12";
    
    
    
    /*********梵郎微商配置参数*********/
//    public static final String WX_AppId = "wxb03c02baabf1e2d3";
//    public static final String WX_Secret = "59b5318377b7466f97c9f9bc3099b58e";
//    public static final String WX_Token = "";
//    public static final String WX_AesKey = "";
//    public static final String WX_MsgDataFormat = "JSON";
//    
//    
//    public static final String WX_MchId = "1509007651";
//    public static final String WX_MchKey = "qWJpf0M4vg58ebOBTieishINyLJZmygT";
//    public static final String WX_KeyPath = "";
//    //微信消息管理模板ID
//    public static final String TEMPLATE_ID = "b30tbk-p2ie3mAoT9JmGbykODT5K4Iwrj48jpFxvAh0";
//    public static final String CERT_PATH = "/home/steve/cert/apiclient_cert.p12";
    
    
    @Bean
    public WxPayConfig config() {
      WxPayConfig payConfig = new WxPayConfig();
      payConfig.setAppId(WX_AppId);
      payConfig.setMchId(WX_MchId);
      payConfig.setMchKey(WX_MchKey);
      payConfig.setSubAppId("");
      payConfig.setSubMchId("");
      payConfig.setKeyPath("");

      return payConfig;
    }

    @Bean
    public WxMaConfig wxMaConfig() {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(WX_AppId);
        config.setSecret(WX_Secret);
        config.setToken(WX_Token);
        config.setAesKey(WX_AesKey);
        config.setMsgDataFormat(WX_MsgDataFormat);

        return config;
    }


}