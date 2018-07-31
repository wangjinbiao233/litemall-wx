package org.linlinjava.litemall.admin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.linlinjava.litemall.admin.dao.UserInfo;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LitemallOauthService {

    @Value("c1ebe466-1cdc-4bd3-ab69-77c3561b9dee")
    private String clientId;
    @Value("d8346ea2-6017-43ed-ad68-19c0f971738b")
    private String clientSecret;
    @Value("http://localhost:8083/admin/login/oauth2Server")
    private String oauthClientUrl;
    @Value("http://localhost:8083/admin/login/oauthCallback")
    private String redirectUrl;
    @Value("http://localhost:8080/oneportal/oauth2api/authorize.if")
    private String oauthServerUrl;
    @Value("http://localhost:8080/oneportal/oauth2api/accessToken.if")
    private String oauthServerAccessTokenUrl;
    @Value("http://localhost:8080/oneportal/oauth2api/userInfo.if")
    private String oauthServerUserInfoUrl;
    @Value("http://localhost:9527/#/oauthredirect")
    private String litemallAdminPageURL;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOauthClientUrl() {
        return oauthClientUrl;
    }

    public void setOauthClientUrl(String oauthClientUrl) {
        this.oauthClientUrl = oauthClientUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getOauthServerUrl() {
        return oauthServerUrl;
    }

    public void setOauthServerUrl(String oauthServerUrl) {
        this.oauthServerUrl = oauthServerUrl;
    }

    public String getLitemallAdminPageURL() {
        return litemallAdminPageURL;
    }

    public void setLitemallAdminPageURL(String litemallAdminPageURL) {
        this.litemallAdminPageURL = litemallAdminPageURL;
    }

    public LitemallAdmin getAdminFormServerByToken(String authCode, String redirectUrl) {

        LitemallAdmin litemallAdmin = null;

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthResourceResponse oAuthResourceResponse = null;

        try {

            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .tokenLocation(oauthServerAccessTokenUrl)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setCode(authCode)
                    .setRedirectURI(redirectUrl)
                    .buildBodyMessage();

            OAuthAccessTokenResponse oAuthAccessTokenResponse = oAuthClient.accessToken(oAuthClientRequest, OAuth.HttpMethod.POST);
            // 拿到访问资源的token令牌
            String accessToken = oAuthAccessTokenResponse.getAccessToken();

            // 使用令牌开始访问需要的资源
            oAuthClientRequest = new OAuthBearerClientRequest(oauthServerUserInfoUrl).setAccessToken(accessToken).buildQueryMessage();
            oAuthResourceResponse = oAuthClient.resource(oAuthClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            String userInfoJSON = oAuthResourceResponse.getBody();

            // 将json数据转换为指定对象
            UserInfo userInfo = new ObjectMapper().readValue(userInfoJSON, UserInfo.class);

            litemallAdmin = new LitemallAdmin();
            litemallAdmin.setDeleted(false);
            litemallAdmin.setPassword(userInfo.getLoginPwd());
            litemallAdmin.setPhone(userInfo.getUserPhone());
            litemallAdmin.setUsername(userInfo.getLoginId());
            if(userInfo.getHeadImage() != null && !userInfo.getHeadImage().isEmpty()) {
                litemallAdmin.setAvatar(userInfo.getHeadImage());
            } else {
                litemallAdmin.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return litemallAdmin;
    }
}
