
package org.linlinjava.litemall.db.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

			String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
			InetAddress ia = InetAddress.getLocalHost();
			String ip = ia.getHostAddress(); // 获取本机IP地址
			logger.info("本机IP地址："+ip);
			String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");// 随机获取UUID
			String appid = WeixinConfig.WX_AppId;// 微信分配的公众账号ID（企业号corpid即为此appId）
			String mchid = WeixinConfig.WX_MchId;// 微信支付分配的商户号
			// 设置支付参数
			SortedMap<Object, Object> signParams = new TreeMap<Object, Object>();

			signParams.put("mch_appid", appid); // 微信分配的公众账号ID（企业号corpid即为此appId）
			signParams.put("mchid", mchid);// 微信支付分配的商户号
			signParams.put("nonce_str", uuid); // 随机字符串，不长于32位
			signParams.put("partner_trade_no", partner_trade_no); // 商户订单号，需保持唯一性
			signParams.put("openid", openid); // 商户appid下，某用户的openid
			signParams.put("check_name", "NO_CHECK"); // NO_CHECK：不校验真实姓名
														// FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）
														// OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
			signParams.put("amount", amount); // 企业付款金额，单位为分
			signParams.put("desc", desc); // 企业付款操作说明信息。必填。
			signParams.put("spbill_create_ip", ip); // 调用接口的机器Ip地址

			// 生成支付签名，要采用URLENCODER的原始值进行MD5算法！

			String sign = "";
			sign = createSign("UTF-8", signParams,WeixinConfig.WX_MchKey);
			// System.out.println(sign);
			String data = "<xml><mch_appid>";
			data += appid + "</mch_appid><mchid>"; // APPID
			data += mchid + "</mchid><nonce_str>"; // 商户ID
			data += uuid + "</nonce_str><partner_trade_no>"; // 随机字符串
			data += partner_trade_no + "</partner_trade_no><openid>"; // 订单号
			data += openid + "</openid><check_name>NO_CHECK</check_name><amount>"; // 是否强制实名验证
			data += amount + "</amount><desc>"; // 企业付款金额，单位为分
			data += desc + "</desc><spbill_create_ip>"; // 企业付款操作说明信息。必填。
			data += ip + "</spbill_create_ip><sign>";// 调用接口的机器Ip地址
			data += sign + "</sign></xml>";// 签名
			logger.info("data："+data);
			//System.out.println(data);
			// 获取证书，发送POST请求；
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(WeixinConfig.CERT_PATH)); // 从配置文件里读取证书的路径信息
			keyStore.load(instream, mchid.toCharArray());// 证书密码是商户ID
			instream.close();
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost httpost = new HttpPost(url); //
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
			logger.info("提现返回值："+jsonStr);
			EntityUtils.consume(entity);
			// 把返回的字符串解释成DOM节点
			Document dom = DocumentHelper.parseText(jsonStr);
			Element root = dom.getRootElement();
			String returnCode = root.element("result_code").getText(); // 获取返回代码
            String returnMsg = root.element("return_msg").getText(); // 返回信息，如非空，为错误原因
            logger.info("returnCode："+returnCode);
            logger.info("returnMsg："+returnMsg);
            if (StringUtils.equals(returnCode, "SUCCESS")) { // 判断返回码为成功还是失败
				String partner_trade_no1 = root.element("partner_trade_no").getText();
				String payment_no = root.element("payment_no").getText(); // 获取支付流水号
				String payment_time = root.element("payment_time").getText(); // 获取支付时间
				map.put("state", returnCode);
				map.put("partner_trade_no", partner_trade_no1);
				map.put("payment_no", payment_no);
				map.put("payment_time", payment_time);
				return map;
			} else {
				String err_code = root.element("err_code").getText(); // 获取错误代码
				String err_code_des = root.element("err_code_des").getText();// 获取错误描述
				map.put("state", returnCode);// state
				map.put("err_code", err_code);// err_code
				map.put("err_code_des", err_code_des);// err_code_des
				return map;
			}

		} catch (DocumentException ex) {
			ex.printStackTrace();
			return map;
		} catch (UnrecoverableKeyException ex) {
			ex.printStackTrace();
			return map;
		} catch (KeyManagementException ex) {
			ex.printStackTrace();
			return map;
		} catch (KeyStoreException ex) {
			ex.printStackTrace();
			return map;
		} catch (CertificateException ex) {
			ex.printStackTrace();
			return map;
		} catch (IOException ex) {
			ex.printStackTrace();
			return map;
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
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
}
