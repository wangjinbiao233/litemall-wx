package org.linlinjava.litemall.admin.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.linlinjava.litemall.db.util.WeixinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**  
 * weixin接口工具类  
 * @author ningshiyang
 */    
public class WeixinUtil {    
    private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);    
    //创建静态map缓存  
    private static Map<String,AccessToken> weixinCacheToken = new HashMap<String, AccessToken>();
    
    /**  
     * 发起https请求并获取结果  
     * @param requestUrl 请求地址  
     * @param requestMethod 请求方式（GET、POST）  
     * @param outputStr 提交的数据  
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)  
     */    
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {    
        JSONObject jsonObject = null;    
        StringBuffer buffer = new StringBuffer();    
        try {    
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化    
            TrustManager[] tm = { new MyX509TrustManager() };    
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");    
            sslContext.init(null, tm, new java.security.SecureRandom());    
            // 从上述SSLContext对象中得到SSLSocketFactory对象    
            SSLSocketFactory ssf = sslContext.getSocketFactory();    
    
            URL url = new URL(requestUrl);    
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();    
            httpUrlConn.setSSLSocketFactory(ssf);    
    
            httpUrlConn.setDoOutput(true);    
            httpUrlConn.setDoInput(true);    
            httpUrlConn.setUseCaches(false);    
            // 设置请求方式（GET/POST）    
            httpUrlConn.setRequestMethod(requestMethod);    
    
            if ("GET".equalsIgnoreCase(requestMethod))    
                httpUrlConn.connect();    
    
            // 当有数据需要提交时    
            if (null != outputStr) {    
                OutputStream outputStream = httpUrlConn.getOutputStream();    
                // 注意编码格式，防止中文乱码    
                outputStream.write(outputStr.getBytes("UTF-8"));    
                outputStream.close();    
            }    
    
            // 将返回的输入流转换成字符串    
            InputStream inputStream = httpUrlConn.getInputStream();    
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
    
            String str = null;    
            while ((str = bufferedReader.readLine()) != null) {    
                buffer.append(str);    
            }    
            bufferedReader.close();    
            inputStreamReader.close();    
            // 释放资源    
            inputStream.close();    
            inputStream = null;    
            httpUrlConn.disconnect();    
            jsonObject = JSONObject.fromObject(buffer.toString());    
        } catch (ConnectException ce) {    
            log.error("Weixin server connection timed out.");    
        } catch (Exception e) {    
            log.error("https request error:{}", e);    
        }    
        return jsonObject;    
    }
    /**  
     * 获取access_token,调用接口凭证
     * @return  
     */    
    public static AccessToken getAccessToken() {    
        AccessToken accessToken = weixinCacheToken.get("token");  
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
        		+"&appid="+WeixinConfig.WX_AppId
        		+"&secret="+WeixinConfig.WX_Secret;   
        if(accessToken != null) {
        	long time = accessToken.getTime();
        	int expiresIn = accessToken.getExpiresIn();  
            long millions = new Long(expiresIn).longValue()*1000;
            long newDate = new Date().getTime();
            long rt = newDate - time;//获取当前时间跟存入时间的差值  
            if(true){  //判断时间是否已经过期  如果过期则重新请求， 否则不做处理  
            	JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  //调用通用的https请求方法  
                // 如果请求成功    
                if (null != jsonObject) {
                    try {
                    	if(jsonObject.containsKey("access_token") && jsonObject.containsKey("expires_in")){
                    		weixinCacheToken.remove("token");
                    		accessToken = new AccessToken();    
                    		accessToken.setToken(jsonObject.getString("access_token"));    
                    		accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                    		accessToken.setTime(new Date().getTime());
                    		weixinCacheToken.put("token", accessToken);
                    	} else 
                    		 accessToken = null;    
                    } catch (JSONException e) {    
                        accessToken = null;    
                        // 获取token失败    
                        log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));    
                    }    
                }    
            }
        } else {
        	JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  //调用通用的https请求方法  
            // 如果请求成功    
            if (null != jsonObject) {
            	 try {
            		 if(jsonObject.containsKey("access_token") && jsonObject.containsKey("expires_in")){
            			 accessToken = new AccessToken();    
            			 accessToken.setToken(jsonObject.getString("access_token"));    
            			 accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            			 accessToken.setTime(new Date().getTime());
            			 weixinCacheToken.put("token", accessToken);
            		 } else 
            			 accessToken = null;
                 } catch (JSONException e) {    
                     accessToken = null;    
                     // 获取token失败    
                     log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));    
                 }    
            }
        }
        return accessToken;    
    }
    
    /**
     * 发送微信模板消息
     */
    public static String sendWXMessage(String accessToken, String openId, String formId, MessageData msgData){
    	
    	String sendMessage_url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
		String request_url = sendMessage_url.replace("ACCESS_TOKEN", accessToken);
		System.out.println(request_url);
		
		String text = "{" +
			  "\"touser\":\"" + openId + "\"," +
			  "\"template_id\":\""+WeixinConfig.TEMPLATE_ID+"\"," +
			  "\"page\":\"pages/ucenter/index/index\"," +
			  "\"form_id\":\""+formId+"\"," +
			  "\"data\":{" +
			      "\"keyword1\":{\"value\":\""+msgData.getKeyword1()+"\",\"color\":\"#173177\"}," +
			      "\"keyword2\":{\"value\":\""+msgData.getKeyword2()+"\",\"color\":\"#173177\"}," +
			      "\"keyword3\":{\"value\":\""+msgData.getKeyword3()+"\",\"color\":\"#173177\"}," +
			      "\"keyword4\":{\"value\":\""+msgData.getKeyword4()+"\",\"color\":\"#173177\"}" +
			      "}" +
			"}";
		
		try {
			JSONObject jsonObject = httpRequest(request_url, "POST", text.toString());
			if (null != jsonObject) { 
	    		try {
	    			System.out.println(jsonObject.toString());
	    			if(jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")){
	    				String errcode = jsonObject.getString("errcode");
	    				String errmsg = jsonObject.getString("errmsg");
	    				if("0".equals(errcode) && "ok".equals(errmsg)) {
	    					return "发送成功";
	    				} else 
	    					return "发送失败"+jsonObject.toString();
	    			} else 
	    				return "发送失败";
	    		} catch (JSONException e) {  
	    			log.error("获取失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));    
	    			return null;
	    		}    
	    	} else 
	    		return null;
		} catch (Exception e1) {
			log.error("获取失败");  
			return null;
		}
    }
    
    
   

	/**
	 * 读取成员详情
	 * 权限说明：需要有对应应用的使用权限，且成员必须在授权应用的可见范围内。
	 * 
	 * @param accessToken	调用接口凭证
	 * @param userTicket	成员票据
	 * @return
	 
	public static SsoUserTemp getUserDetail(String accessToken, UserTicket userTicket) {
		String userDetail_url = Const.WECHAT_GET_USER_DETAIL_URL + "?"
				+ "access_token=ACCESS_TOKEN";
		String request_url = userDetail_url.replace("ACCESS_TOKEN", accessToken);
		JSONObject json =  new JSONObject();
		json.put("user_ticket", userTicket.getUserTicket());
		try {
			JSONObject jsonObject = httpRequest(request_url, "POST", json.toString());
			if (null != jsonObject) { 
	    		try {
	    			if(jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")){
	    				String errcode = jsonObject.getString("errcode");
	    				String errmsg = jsonObject.getString("errmsg");
	    				if("0".equals(errcode) && "ok".equals(errmsg)) {
	    					SsoUserTemp user = new SsoUserTemp();
	    					user.setWechatUserId(jsonObject.getString("userid"));
	    					user.setUserName(jsonObject.getString("name"));
	    					String department = jsonObject.getString("department");
	    					if(StringUtils.isNotBlank(department))
	    						user.setDepartment(department);
	    					String position = jsonObject.getString("position");
	    					if(StringUtils.isNotBlank(position))
	    						user.setPosition(position);
	    					String mobile = jsonObject.getString("mobile");
	    					if(StringUtils.isNotBlank(mobile))
	    						user.setMobile(mobile);
	    					int gender = jsonObject.getInt("gender");
	    					if(1 == gender) 
	    						user.setGender("MALE");//0表示未定义，1表示男性，2表示女性
	    					else if(2 == gender)
	    						user.setGender("FEMALE");//0表示未定义，1表示男性，2表示女性
	    					String avatar = jsonObject.getString("avatar");
	    					if(StringUtils.isNotBlank(avatar))
	    						user.setAvatar(avatar);
	    					String email = jsonObject.getString("email");
	    					if(StringUtils.isNotBlank(email))
	    						user.setEmail(email);
	    					user.setDevice(userTicket.getDeviceId());
	    					return user;
	    				} else 
	    					return null;
	    			} else 
	    				return null;
	    		} catch (JSONException e) {  
	    			log.error("获取失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));    
	    			return null;
	    		}    
	    	} else 
	    		return null;
		} catch (Exception e1) {
			log.error("获取失败");  
			return null;
		}
		
    	
	}  
	
	*//**
	 * 获取部门列表
	 * 权限说明：只能拉取token对应的应用的权限范围内的部门列表
	 * @param accessToken 调用接口凭证
	 * @param deptId 部门id。获取指定部门及其下的子部门。 如果不填，默认获取全量组织架构（非必填）
	 *//*
	public static void getDepartmentList(String accessToken, int deptId) {
		String userDetail_url = Const.WECHAT_GET_DEPARTMENT_LIST_URL + "?"
				+ "access_token=ACCESS_TOKEN";
		String request_url = userDetail_url.replace("ACCESS_TOKEN", accessToken);
		if(deptId != -1) {
			request_url += "&id="+deptId;
		}
		JSONObject jsonObject = httpRequest(request_url, "GET", null);
    	if (null != jsonObject) {    
    		try {
    			if(jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")){
    				String errcode = jsonObject.getString("errcode");
    				String errmsg = jsonObject.getString("errmsg");
    				if("0".equals(errcode) && "ok".equals(errmsg)) {
    					System.out.println(jsonObject.toString());
    				}
    			} else {
    				
    			}
    		} catch (JSONException e) {  
    			// 获取jsApiTicket失败    
    			log.error("获取jsApiTicket失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));    
    		}    
    	}
	}
	
	*//**
	 * 获取部门成员
	 * 权限说明： 应用须拥有指定部门的查看权限。
	 * @param accessToken 调用接口凭证
	 * @param deptId 获取的部门id
	 * @param fetchChild 1/0：是否递归获取子部门下面的成员（非必填）
	 *//*
	public static void getDepartmentUserList(String accessToken,int deptId, int fetchChild) {
		String userDetail_url = Const.WECHAT_GET_DEPARTMENT_USER_LIST_URL + "?"
				+ "access_token=ACCESS_TOKEN&department_id="+deptId;
		String request_url = userDetail_url.replace("ACCESS_TOKEN", accessToken);
		if(fetchChild != -1 && fetchChild == 0 || fetchChild == 1) {
			request_url += "&fetch_child="+fetchChild;
		}
		JSONObject jsonObject = httpRequest(request_url, "GET", null);
    	if (null != jsonObject) {    
    		try {
    			if(jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")){
    				String errcode = jsonObject.getString("errcode");
    				String errmsg = jsonObject.getString("errmsg");
    				if("0".equals(errcode) && "ok".equals(errmsg)) {
    					System.out.println(jsonObject.toString());
    					String userArray = jsonObject.getString("userlist");
    					System.out.println(userArray.toString());
    					JSONArray userList = JSONArray.fromObject(userArray);
    					if(userList.size() > 0) {
    						for (int i = 0; i < userList.size(); i++) {
    							JSONObject job = (JSONObject) userList.get(i);
    							System.out.println("userid = " + job.getString("userid"));
    							System.out.println("name = " + job.getString("name"));
    							System.out.println("department = " + job.getString("department"));
							}
    					}
    				}
    			} else {
    				
    			}
    		} catch (JSONException e) {  
    			// 获取jsApiTicket失败    
    			log.error("获取jsApiTicket失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));    
    		}    
    	}
	}*/
}      