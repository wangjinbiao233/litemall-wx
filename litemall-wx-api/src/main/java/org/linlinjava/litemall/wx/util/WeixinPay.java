package org.linlinjava.litemall.wx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.linlinjava.litemall.db.util.WeixinConfig;

/**
 * Title: WeixinPay
 * Description: 微信提现-企业付款
 * Author: YangTao
 * Date: 2018年1月10日
 */
public class WeixinPay{
	
	 private static Logger logger = Logger.getLogger(WeixinPay.class);
	/**
	 * @author YangTao
	 * @date 2018年1月11日
	 * @Description：微信支付，企业向个人付款
	 * @param openid
	 *            收款人的openID(微信的openID)
	 * @param amount
	 *            付款金额
	 * @param desc
	 *            付款描述
	 * @param partner_trade_no
	 *            订单号(系统业务逻辑用到的订单号)
	 * @return map{state:SUCCESS/FAIL}{payment_no:
	 *         '支付成功后，微信返回的订单号'}{payment_time:'支付成功的时间'}{err_code:'支付失败后，返回的错误代码'}{err_code_des:'支付失败后，返回的错误描
	 *         述 ' }
	 * @throws ParseException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws DocumentException
	 */
	public static Map<String, String> transfer(String openid, int amount, String desc, String partner_trade_no) {
		
		Map<String, String> map = new HashMap<String, String>(); // 定义一个返回MAP
		try {
			String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");// 随机获取UUID
			String appid = WeixinConfig.WX_AppId;// 微信分配的公众账号ID（企业号corpid即为此appId）
			String mchid = WeixinConfig.WX_MchId;// 微信支付分配的商户号
			// 设置请求参数
			SortedMap<Object, Object> signParams = new TreeMap<Object, Object>();
			signParams.put("appid", appid); // 微信分配的公众账号ID（企业号corpid即为此appId）
			signParams.put("mch_id", mchid);// 微信支付分配的商户号
			signParams.put("nonce_str", uuid); // 
			signParams.put("op_user_id", mchid); // 
			signParams.put("out_refund_no", new Date().getTime() + ""); //
			signParams.put("out_trade_no", partner_trade_no); //
			signParams.put("refund_fee", amount); // 退款金额
			signParams.put("total_fee", amount); // 订单金额
						
					
			// 生成支付签名，要采用URLENCODER的原始值进行MD5算法！
			String sign = "";
			sign = createSign("UTF-8", signParams,WeixinConfig.WX_MchKey);
			// System.out.println(sign);
			
			
			String data = "<xml><appid>";
			data += appid + "</appid><mch_id>"; // APPID
			data += mchid + "</mch_id><nonce_str>"; // 商户ID
			data += uuid + "</nonce_str><op_user_id>"; // 商户ID
			data += mchid + "</op_user_id><out_refund_no>"; // 随机字符串
			data += new Date().getTime() + "" + "</out_refund_no><out_trade_no>"; // 订单号
			data += partner_trade_no + "</out_trade_no><refund_fee>"; // 订单号
			data += amount + "</refund_fee><total_fee>";// 调用接口的机器Ip地址
			data += amount + "</total_fee><sign>";// 调用接口的机器Ip地址
			data += sign + "</sign></xml>";// 签名
			logger.info(data);
			// 获取证书，发送POST请求；
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(WeixinConfig.CERT_PATH)); // 从配置文件里读取证书的路径信息
			keyStore.load(instream, mchid.toCharArray());// 证书密码是商户ID
			instream.close();
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		        
			HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund"); //
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
              
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			EntityUtils.consume(entity);
			// 把返回的字符串解释成DOM节点
			Document dom = DocumentHelper.parseText(jsonStr);
			Element root = dom.getRootElement();
			String returnCode = root.element("result_code").getText(); // 获取返回代码
			System.out.println(returnCode);
			logger.info(returnCode);
			if (StringUtils.equals(returnCode, "SUCCESS")) { // 判断返回码为成功还是失败
				String out_trade_no = root.element("out_trade_no").getText(); //获取商户订单号
				String out_refund_no = root.element("out_refund_no").getText(); // 获取商户退款单号
				map.put("state", returnCode);
				map.put("out_trade_no", out_trade_no);
				map.put("out_refund_no", out_refund_no);
				return map;
			} else {
				String err_code = root.element("err_code").getText(); // 获取错误代码
				String err_code_des = root.element("err_code_des").getText();// 获取错误描述
				map.put("state", returnCode);// state
				map.put("err_code", err_code);// err_code
				map.put("err_code_des", err_code_des);// err_code_des
				return map;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("exception:"+ex.getMessage());
			logger.error("exception happened:",ex);
			return map;
		}
	}

	/**
	 * @author YangTao
	 * @date 2018年1月11日
	 * @Description：sign签名
	 * @param characterEncoding
	 *            编码格式
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	private static String createSign(String characterEncoding, SortedMap<Object, Object> parameters,String key) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<Object, Object>> es = parameters.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	// MD5Util工具类中相关的方法

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	
	public static void main(String[] args) throws DocumentException {
		SortedMap<Object, Object> signParams = new TreeMap<Object, Object>();
		signParams.put("appid", "wxb03c02baabf1e2d3"); // 微信分配的公众账号ID（企业号corpid即为此appId）
		signParams.put("mch_id", "1509007651");// 微信支付分配的商户号
		signParams.put("nonce_str", "5C6D8499F9CC410EBC5F50E9BFE708D1"); // 
		signParams.put("op_user_id", "1509007651"); // 
		signParams.put("out_refund_no", "1531200553011"); //
		signParams.put("out_trade_no", "20180710342172"); //
		signParams.put("refund_fee", 50); // 退款金额
		signParams.put("total_fee", 50); // 订单金额
		
		InputStream configFile = WeixinPay.class.getClassLoader().getResourceAsStream("weixin.config.xml");
		SAXReader reader = new SAXReader();
		Document doc = reader.read(configFile);
		Element config = doc.getRootElement();
					
				
		// 生成支付签名，要采用URLENCODER的原始值进行MD5算法！
		String sign = "";
		sign = createSign("UTF-8", signParams,config.elementTextTrim("key"));
		
		System.out.println(sign);
	}
}
