package org.linlinjava.litemall.wx.util.weixin;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class WeixinUtil {
	
@Autowired
private StorageService storageService;

private static Log log = LogFactory.getLog(WeixinUtil.class);   
	
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
	public String httpRequest(String requestUrl, String requestMethod, String outputStr) {
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

			// 将返回的输入流转换成字符串    
			InputStream inputStream = httpUrlConn.getInputStream();  
			int size=inputStream.available();
			if( size == 0 ){
				return httpRequest( requestUrl, requestMethod, outputStr);
			} else {
				String realName = "_"+getSystemDateStrDetail()+"_"+buildRandomFileName(8)+".jpg";
				LitemallStorage storage = storageService.store(inputStream, realName, "image");
				urlPath = storage.getUrl();
			}

			inputStream.close();    
			httpUrlConn.disconnect();    
		} catch (Exception e) {
			log.error("https request error:{}", e);    
		}
		return urlPath;    
	}

	/**
	 * 生成二维码
	 * @param accessToken
	 * @param userId
	 * @return
	 */
	public String getQRcode(String accessToken, String userId) {
		String filePath = "";
		String userDetail_url ="https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN";
		String request_url = userDetail_url.replace("ACCESS_TOKEN", accessToken);
		JSONObject param =  new JSONObject();
		param.put("scene", userId);
		param.put("page", "pages/ucenter/index/index");
		param.put("width", 430);
		param.put("auto_color", true);
		Map<String,Object> line_color = new HashMap<>();
		line_color.put("r", 0);
		line_color.put("g", 0);
		line_color.put("b", 0);
		param.put("line_color", line_color);
		param.put("is_hyaline", false);

		filePath = httpRequest(request_url, "POST", param.toString());
		return filePath;

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
