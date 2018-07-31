package org.linlinjava.litemall.wx.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Util {
	
	public final static String getMD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		String mdStr = null;
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			mdStr = new String(str);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return mdStr;
	}
	
	//小写字母
	public static String getMD5Lower(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			String md5 = new BigInteger(1, md.digest()).toString(16);
			// BigInteger会把0省略掉，需补全至32位
			return fillMD5(md5);
		} catch (Exception e) {
			throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
		}
	}

	public static String fillMD5(String md5) {
		return md5.length() == 32 ? md5 : fillMD5("0" + md5);
	}
	
}