package org.linlinjava.litemall.db.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class SmsUtil {

	public static final String charset = "utf-8";
	// 用户平台API账号(非登录账号,示例:N1234567)
	public static String account = "N7303135";
	// 用户平台API密码(非登录密码)
	public static String pswd = "xKfuOdsTz6e754";

	/**
	 * post请求发送短信
	 * 
	 * @param phoen
	 *            手机号码
	 * @param content
	 *            发送的内容
	 * @param count
	 *            发送的手机号个数
	 * @return map
	 */
	public static Map<String, Object> sendSms(String phone, String msg, int count) throws IOException {
		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,"true","3");
		String requestJson = JSON.toJSONString(smsSingleRequest);
		System.out.println("before request string is: " + requestJson);
		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		System.out.println("response after request result is :" + response);
		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
		return verifyValue(smsSingleResponse.getMsgId());
	}

	/**
	 * @param 错误返回值
	 * @return
	 */
	public static Map<String, Object> verifyValue(String msgid) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (msgid.equals("-1")) {
			map.put("status", "failure");
			map.put("message", "参数为空。信息、电话号码等有空指针、登陆失败。");
		} else if (msgid.equals("-11")) {
			map.put("status", "failure");
			map.put("message", "电话号码中有非数字字符");
		} else if (msgid.equals("-12")) {
			map.put("status", "failure");
			map.put("message", "有异常电话号码");
		} else if (msgid.equals("-101")) {
			map.put("status", "failure");
			map.put("message", "发送消息等待超时");
		} else if (msgid.equals("-102")) {
			map.put("status", "failure");
			map.put("message", "发送或接收消息失败");
		} else if (msgid.equals("-103")) {
			map.put("status", "failure");
			map.put("message", "接收消息超时");
		} else if (msgid.equals("-200")) {
			map.put("status", "failure");
			map.put("message", "其他错误");
		} else if (msgid.equals("-999")) {
			map.put("status", "failure");
			map.put("message", "服务器内部错误");
		} else if (msgid.equals("-10001")) {
			map.put("status", "failure");
			map.put("message", "用户登陆不成功(帐号密码错误)");
		} else if (msgid.equals("-10002")) {
			map.put("status", "failure");
			map.put("message", "提交格式不正确");
		} else if (msgid.equals("-10003")) {
			map.put("status", "failure");
			map.put("message", "用户余额不足");
		} else if (msgid.equals("-10004")) {
			map.put("status", "failure");
			map.put("message", "手机号码不正确");
		} else if (msgid.equals("-10005")) {
			map.put("status", "failure");
			map.put("message", "计费用户帐号错误");
		} else if (msgid.equals("-10006")) {
			map.put("status", "failure");
			map.put("message", "计费用户密码错");
		} else if (msgid.equals("-10007")) {
			map.put("status", "failure");
			map.put("message", "账号已经被停用");
		} else if (msgid.equals("-10008")) {
			map.put("status", "failure");
			map.put("message", "账号类型不支持该功能");
		} else if (msgid.equals("-10009")) {
			map.put("status", "failure");
			map.put("message", "其它错误");
		} else if (msgid.equals("-10010")) {
			map.put("status", "failure");
			map.put("message", "企业代码不正确");
		} else if (msgid.equals("-10011")) {
			map.put("status", "failure");
			map.put("message", "信息内容超长");
		} else if (msgid.equals("-10012")) {
			map.put("status", "failure");
			map.put("message", "不能发送联通号码");
		} else if (msgid.equals("-10013")) {
			map.put("status", "failure");
			map.put("message", "操作员权限不够");
		} else if (msgid.equals("-10014")) {
			map.put("status", "failure");
			map.put("message", "费率代码不正确");
		} else if (msgid.equals("-10015")) {
			map.put("status", "failure");
			map.put("message", "服务器繁忙");
		} else if (msgid.equals("-10016")) {
			map.put("status", "failure");
			map.put("message", "企业权限不够");
		} else if (msgid.equals("-10017")) {
			map.put("status", "failure");
			map.put("message", "此时间段不允许发送");
		} else if (msgid.equals("-10018")) {
			map.put("status", "failure");
			map.put("message", "经销商用户名或密码错");
		} else if (msgid.equals("-10019")) {
			map.put("status", "failure");
			map.put("message", "手机列表或规则错误");
		} else if (msgid.equals("-10021")) {
			map.put("status", "failure");
			map.put("message", "没有开停户权限");
		} else if (msgid.equals("-10022")) {
			map.put("status", "failure");
			map.put("message", "没有转换用户类型的权限");
		} else if (msgid.equals("-10023")) {
			map.put("status", "failure");
			map.put("message", "没有修改用户所属经销商的权限");
		} else if (msgid.equals("-10024")) {
			map.put("status", "failure");
			map.put("message", "经销商用户名或密码错");
		} else if (msgid.equals("-10025")) {
			map.put("status", "failure");
			map.put("message", "操作员登陆名或密码错误");
		} else if (msgid.equals("-10026")) {
			map.put("status", "failure");
			map.put("message", "操作员所充值的用户不存在");
		} else if (msgid.equals("-10027")) {
			map.put("status", "failure");
			map.put("message", "操作员没有充值商务版的权限");
		} else if (msgid.equals("-10028")) {
			map.put("status", "failure");
			map.put("message", "该用户没有转正不能充值");
		} else if (msgid.equals("-10029")) {
			map.put("status", "failure");
			map.put("message", "此用户没有权限从此通道发送信息");
		} else if (msgid.equals("-10030")) {
			map.put("status", "failure");
			map.put("message", "不能发送移动号码");
		} else if (msgid.equals("-10031")) {
			map.put("status", "failure");
			map.put("message", "手机号码(字段)非法");
		} else if (msgid.equals("-10032")) {
			map.put("status", "failure");
			map.put("message", "用户使用的费率代码错误");
		} else if (msgid.equals("-10033")) {
			map.put("status", "failure");
			map.put("message", "非法关键词");
		} else if (msgid.equals("-10057")) {
			map.put("status", "failure");
			map.put("message", "非法IP地址");// 还有一些暂时不列举出来
		} else {
			map.put("status", "success");
			map.put("message", "发送成功");
		}
		return map;
	}

	/**
	 * 短信验证码
	 * 
	 * @param number
	 * @return
	 */
	public static String generate(int number) {
		StringBuffer vcode = new StringBuffer();
		for (int i = 0; i < number; i++) {
			vcode.append((int) (Math.random() * 9) + "");
		}
		return vcode.toString();
	}

}
