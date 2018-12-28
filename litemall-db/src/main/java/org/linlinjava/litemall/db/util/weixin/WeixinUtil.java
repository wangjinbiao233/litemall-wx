package org.linlinjava.litemall.db.util.weixin;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.service.StorageService;
import org.linlinjava.litemall.db.util.WeixinConfig;
import org.linlinjava.litemall.db.util.entity.AccessToken;
import org.linlinjava.litemall.db.util.entity.MessageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class WeixinUtil {
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
    //创建静态map缓存
    private static Map<String,AccessToken> weixinCacheToken = new HashMap<String, AccessToken>();

	@Autowired
	private StorageService storageService;

	/**
	 * 得到当前系统时间，返回String类型 ,格式为 "yyyyMMMddHH24mmss"
	 * @return
	 */
	public static String getSystemDateStrDetail() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyyMMdd");
		return dateFormat.format(new Date());
	}
	/**
	 * 随机生成8位的数字
	 * @param length
	 * @return
	 */
	public static String buildRandomFileName(int length) {
		StringBuffer r = new StringBuffer(length);
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			r.append(random.nextInt(9));
		}
		return r.toString();
	}
	
	/**  
	 * 发起https请求二维码图片  
	 * @param requestUrl 请求地址  
	 * @param requestMethod 请求方式（GET、POST）  
	 * @param outputStr 提交的数据  
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)  
	 */    
	public String getwxacode(String requestUrl, String requestMethod, String outputStr, int tryCount) {

		String urlPath="";
		try {    
			TrustManager[] tm = { new MyX509TrustManager() };    
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");    
			sslContext.init(null, tm, new java.security.SecureRandom());    
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

			InputStream inputStream = httpUrlConn.getInputStream();
			//获取数据流里有多少个字节
			int size=inputStream.available();
			if( size < 2048 && tryCount < 3){
				return getwxacode( requestUrl, requestMethod, outputStr, ++tryCount);
			} else {
			    if(size < 2048){
                    log.info("用户推广二维码生成失败");
                    log.info("二维码文件大小="+size);
                    urlPath = null;
                } else {
                    String realName = "_"+getSystemDateStrDetail()+"_"+buildRandomFileName(8)+".jpg";
                    LitemallStorage storage = storageService.store(inputStream, realName, "image");
                    urlPath = storage.getUrl();
                    //saveToImgByInputStream(inputStream,"D:\\image",realName);  //保存图片到本地
                }

			}

			inputStream.close();    
			httpUrlConn.disconnect();    
		} catch (Exception e) {
			log.error("https request error:{}", e);    
		}
		return urlPath;    
	}

    /**
     * 将二进制转换成文件保存
     * @param instreams 二进制流
     * @param imgPath 图片的保存路径
     * @param imgName 图片的名称
     * @return
     *      1：保存正常
     *      0：保存失败
     */
    public static int saveToImgByInputStream(InputStream instreams,String imgPath,String imgName){
        int stateInt = 1;
        if(instreams != null){
            try {
                File file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos=new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                stateInt = 0;
                e.printStackTrace();
            } finally {
            }
        }
        return stateInt;
    }

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
            if(rt > millions){  //判断时间是否已经过期  如果过期则重新请求， 否则不做处理
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
                        //log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
                        log.error("获取taken失败");
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
                    //log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
                    log.error("获取taken失败");
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
	 * 生成二维码
	 * @param accessToken
	 * @param scene
	 * @return
	 */
	public String getQRcode(String accessToken, String scene) {
		String userDetail_url ="https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN";
		String request_url = userDetail_url.replace("ACCESS_TOKEN", accessToken);
		JSONObject param =  new JSONObject();
		param.put("scene", scene);
		param.put("page", "pages/ucenter/index/index");
		param.put("width", 430);
		param.put("auto_color", true);
		Map<String,Object> line_color = new HashMap<>();
		line_color.put("r", 0);
		line_color.put("g", 0);
		line_color.put("b", 0);
		param.put("line_color", line_color);
		param.put("is_hyaline", false);

		return getwxacode(request_url, "POST", param.toString(),0);

	}


	/**
	 * 检测是否有emoji字符
	 * @param source 需要判断的字符串
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!notisEmojiCharacter(codePoint)) {
				//判断确认有表情字符
				return true;
			}
		}
		return false;
	}


	/**
	 * 非emoji表情字符判断
	 * @param codePoint
	 * @return
	 */
	private static boolean notisEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || 
				(codePoint == 0x9) ||                            
				(codePoint == 0xA) ||
				(codePoint == 0xD) ||
				((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
				((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
				((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * @param source  需要过滤的字符串
	 * @return
	 */
	public static String filterEmoji(String source) {
		source = source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
		if (!containsEmoji(source)) {
			return source.trim();//如果不包含，直接返回
		}
		StringBuilder buf = null;//该buf保存非emoji的字符
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (notisEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			} 
		}
		if (buf == null) {         
			return "";//如果没有找到非emoji的字符，则返回无内容的字符串
		} else {
			if (buf.length() == len) {
				buf = null;
				return source.trim();
			} else {
				return buf.toString().trim();
			}
		}
	}
}
