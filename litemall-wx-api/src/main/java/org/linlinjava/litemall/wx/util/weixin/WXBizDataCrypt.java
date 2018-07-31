package org.linlinjava.litemall.wx.util.weixin;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;

/**
 * 
 * @author 宁世洋
 * 解密微信
 */
public class WXBizDataCrypt {


	/*
	 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
	private static WXBizDataCrypt instance = null;

	private WXBizDataCrypt() {

	}

	public static WXBizDataCrypt getInstance() {
		if (instance == null)
			instance = new WXBizDataCrypt();
		return instance;
	}

	/**
	 * 对于官方加密数据（encryptData）解密说明如下： 加密数据解密算法 接口如果涉及敏感数据（如wx.getUserInfo当中的
	 * openId 和unionId ），接口的明文内容将不包含这些敏感数据。开发者如需要获取敏感数据，需要对接口返回的加密数据(
	 * encryptedData )进行对称解密。 解密算法如下： 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充。
	 * 对称解密的目标密文为 Base64_Decode(encryptedData), 对称解密秘钥 aeskey =
	 * Base64_Decode(session_key), aeskey 是16字节 对称解密算法初始向量 iv 会在数据接口中返回。
	 * 
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param encryptedData
	 * 加密内容
	 * @param sessionKey
	 * 小程序登录sessionKey
	 * @param iv
	 * 解密算法初始向量 iv 会在数据接口中返回。
	 * @param encodingFormat
	 * 编码格式默认UTF-8
	 * @return 返回解密后的字符串
	 * @throws Exception
	 */
	public String decrypt(String encryptedData, String sessionKey, String iv, String encodingFormat) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] _encryptedData = base64Decoder.decodeBuffer(encryptedData);
			byte[] _sessionKey = base64Decoder.decodeBuffer(sessionKey);
			byte[] _iv = base64Decoder.decodeBuffer(iv);
			SecretKeySpec secretKeySpec = new SecretKeySpec(_sessionKey, "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(_iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
			byte[] original = cipher.doFinal(_encryptedData);
			byte[] bytes = PKCS7Encoder.decode(original);
			String originalString = new String(bytes, "UTF-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}
}
