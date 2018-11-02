package org.linlinjava.litemall.wx.web;

import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.OrderHandleOption;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.WeixinConfig;
import org.linlinjava.litemall.wx.util.MD5Util;
import org.linlinjava.litemall.wx.util.WXPayUtil;
import org.linlinjava.litemall.wx.util.WeixinPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;

@RestController
@RequestMapping("/wx/pay")
public class WxPayController {
    private Logger logger = Logger.getLogger(WxPayController.class);

    @Autowired
    private LitemallOrderService orderService;
    @Autowired
    private LitemallOrderGoodsService orderGoodsService;
    @Autowired
    private LitemallUserService userService;

    @Autowired
    private WxPayService wxService;


    /**
     * 获取支付的请求参数
     */
    @RequestMapping("prepay")
    public Object payPrepay( @RequestBody String body, HttpServletRequest request) {
    	Integer userId = JacksonUtil.parseInteger(body, "userId");
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        Short payType = JacksonUtil.parseShort(body, "payType");
        if (userId == null) {
            return ResponseUtil.fail401();
        }
        if (orderId == null) {
            return ResponseUtil.fail402();
        }

        LitemallOrder order = orderService.findById(orderId);
        if(payType != null) {
        	order.setPayType(payType);
        	orderService.update(order);
        }
        LitemallUser user = userService.findById(userId);
        if(user.getWeixinOpenid() == null){
            return ResponseUtil.fail(403, "用户openid不存在");
        }
        if(order == null){
            return ResponseUtil.fail(403, "订单不存在");
        }
        
        if(!order.getUserId().equals(userId)){
            return ResponseUtil.badArgumentValue();
        }

        // 检测是否能够付款
        OrderHandleOption handleOption = OrderUtil.build(order);
        if(!handleOption.isPay()){
            return ResponseUtil.fail(403, "订单不能付款");
        }
        
//        if(order.getPayStatus() != 0){
//            return ResponseUtil.fail(403, "订单已支付，请不要重复操作");
//        }
        
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setAppid(WeixinConfig.WX_AppId);
        wxPayUnifiedOrderRequest.setMchId(WeixinConfig.WX_MchId);
        wxPayUnifiedOrderRequest.setNonceStr(new Date().getTime() + "");
        wxPayUnifiedOrderRequest.setBody("微商城-支付");
        wxPayUnifiedOrderRequest.setOutTradeNo(order.getOrderSn());
        Double totalFee = order.getActualPrice().doubleValue() * 100 ;
        wxPayUnifiedOrderRequest.setTotalFee(totalFee.intValue());
        wxPayUnifiedOrderRequest.setSpbillCreateIp(request.getRemoteAddr());
        wxPayUnifiedOrderRequest.setNotifyURL("http://mall-wx.dgtis.com/wx/pay/notify");
        wxPayUnifiedOrderRequest.setTradeType("JSAPI");
        wxPayUnifiedOrderRequest.setOpenid(user.getWeixinOpenid());
        
        WxPayUnifiedOrderResult wxPayUnifiedOrderResult = null;
        try {
            wxPayUnifiedOrderResult = wxService.unifiedOrder(wxPayUnifiedOrderRequest);
        } catch (WxPayException e) {
            e.printStackTrace();
            return ResponseUtil.fail(403, "支付失败");
        }
        Map<String, String> data = new HashMap<String, String>();
        if("SUCCESS".equals(wxPayUnifiedOrderResult.getResultCode())) {
        		String timeStamp = System.currentTimeMillis() + "";
        		String nonceStr = wxPayUnifiedOrderResult.getNonceStr();
        		String packageStr = "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId();
        		data.put("appId", wxPayUnifiedOrderResult.getAppid());
        		data.put("timeStamp", timeStamp);
        		data.put("nonceStr", nonceStr);
        		data.put("package", packageStr);
        		data.put("paySign", createPaySign(data));
        		data.put("signType", "MD5");
        		//支付请求参数
        		return ResponseUtil.ok(data);
        } else 
        		return ResponseUtil.fail(403, "支付失败");
    }

    /**
     * 微信订单回调接口
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public Object notify(HttpServletRequest request, HttpServletResponse response) {
    		BufferedReader reader = null;

        try {
			reader = request.getReader();
			String line = "";
	        String xmlString = null;
	        StringBuffer inputString = new StringBuffer();

	        while ((line = reader.readLine()) != null) {
	            inputString.append(line);
	        }
	        xmlString = inputString.toString();
	        request.getReader().close();
	        logger.info("----接收到的数据如下：---" + xmlString);
	        Map<String, String> result = WXPayUtil.xmlToMap(xmlString);
	        if("SUCCESS".equals(result.get("result_code"))) {
	        		//校验成功时，修改订单状态
		        if(checkSign(result)) {
		        		String outTradeNo = result.get("out_trade_no");
		        		LitemallUser user = userService.queryByOid(result.get("openid"));
		        		LitemallOrder order = orderService.queryByOrderSn(user.getId(), outTradeNo);
		        		/**
		        		 * 微信接口返回的金额单位为 分
		        		 */
		        		String totalFee = result.get("total_fee");
		        		Double orderPrice = order.getOrderPrice().doubleValue() * 100;
		        		String priceStr = orderPrice.intValue() + "";
		        		if(priceStr.equals(totalFee) && "101".equals(order.getOrderStatus().toString())) {
		        			LitemallOrder updateOrder = new LitemallOrder();
		        			updateOrder.setId(order.getId());
		        			List<LitemallOrderGoods> litemallOrderGoodsList = orderGoodsService.queryByOid(order.getId());   
		        			for(LitemallOrderGoods litemallOrderGoods :litemallOrderGoodsList) {  
		        				litemallOrderGoods.setOrderStatus(OrderUtil.STATUS_PAY);
		        				orderGoodsService.update(litemallOrderGoods);
		        			}	
		        			updateOrder.setOrderStatus(OrderUtil.STATUS_PAY);
		        			//微信的订单号
		        			updateOrder.setPayId(result.get("transaction_id"));
		        			
		        			orderService.update(updateOrder);
		        			/**
		        			 * 返回微信接口状态码
		        			 */
		        			Map<String, Object> obj = new HashMap<String, Object>();
		        			obj.put("return_code", "SUCCESS");
		        			obj.put("return_msg", "OK");
		        			return obj;
		        		}
		        }
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return ResponseUtil.fail501();
    }
    
    

    /**
     * 订单退款请求
     */
    @RequestMapping("refund")
    public Object refund(@RequestBody String body, HttpServletRequest request) {
    	
    	Integer userId = JacksonUtil.parseInteger(body, "userId");
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (userId == null) {
            return ResponseUtil.fail401();
        }
        if (orderId == null) {
            return ResponseUtil.fail402();
        }
        
        LitemallUser litemallUser = userService.findById(userId);
        String openid = litemallUser.getWeixinOpenid();
        
        LitemallOrder order = orderService.findById(orderId);
        Map<String, String> data = new HashMap<String, String>();
        try {
     		
     		/*向微信API发起提现请求*/
            Double wxMoney = order.getActualPrice().doubleValue() * 100;
            int amount = wxMoney.intValue();
            Map<String, String>  map = WeixinPay.transfer(openid, amount, "退款", order.getOrderSn());
            String state = map.get("state");
            Map<String, Object> obj = new HashMap<String, Object>();
            if("SUCCESS".equals(state)) {
    			obj.put("return_code", "SUCCESS");
    			obj.put("return_msg", "OK");
    			return obj;
            } else 
            	obj.put("return_code", "FAIL");
				obj.put("return_msg", map.get("err_code_des"));
				return obj;
        } catch (Exception e) {
			e.printStackTrace();
		}
        
        return ResponseUtil.ok(data);
        
    }
    
    /**
     * 支付回调 检查 sign
     * @param data
     * @return
     */
    private boolean checkSign(Map<String, String> result) {
    		String signFromWX = result.get("sign");
    		Iterator<Entry<String, String>> it = result.entrySet().iterator();
    		SortedMap<String, String> paramsMap = new TreeMap<String, String>();
	    while(it.hasNext()) {
	        Entry<String, String> entry = it.next();
	        	String key = entry.getKey();
	        String value = entry.getValue();
	        	logger.info("key = " + key + ", value = " + value);
	        	if(null != value && !"".equals(value) && !"sign".equals(key) ) {
	        		paramsMap.put(key, value);
	        	}
	    }  
	    
	    StringBuffer sb = new StringBuffer();
		Set es = paramsMap.entrySet();//字典序
		Iterator iterator = es.iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			//为空不参与签名、参数名区分大小写
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		
		//第二步拼接key，key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
		sb.append("key=" +"r4re3redsw3e5tg9oi87hy65tbvgt5ws");
		String sign = MD5Util.getMD5(sb.toString()).toUpperCase();//MD5加密
		logger.info(" signStr =  " + sb.toString());
		logger.info(" sign =  " + sign);
    		return signFromWX.equals(sign);
    }
    
    /**
     * 生成 发起微信支付时的 签名
     * @param data
     * @return
     */
    private String createPaySign(Map<String, String> data) {
		//根据规则创建可排序的map集合
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appId", data.get("appId"));
		packageParams.put("timeStamp", data.get("timeStamp"));
		packageParams.put("nonceStr", data.get("nonceStr"));
		packageParams.put("package", data.get("package"));
		packageParams.put("signType", "MD5");
		
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();//字典序
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			//为空不参与签名、参数名区分大小写
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		//第二步拼接key，key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
		sb.append("key=" +WeixinConfig.WX_MchKey);
		String sign = MD5Util.getMD5(sb.toString())
				.toUpperCase();//MD5加密
		System.out.println(" signStr =  " + sb.toString());
		System.out.println(" sign =  " + sign);
		return sign;
	}

}