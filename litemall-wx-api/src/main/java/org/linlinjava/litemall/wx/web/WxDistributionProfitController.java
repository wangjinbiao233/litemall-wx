package org.linlinjava.litemall.wx.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.linlinjava.litemall.db.domain.LitemallDictionary;
import org.linlinjava.litemall.db.domain.LitemallDistributionApply;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallDictionaryService;
import org.linlinjava.litemall.db.service.LitemallDistributionApplyService;
import org.linlinjava.litemall.db.service.LitemallDistributionProfitService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.wx.util.weixin.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.binarywang.wx.miniapp.api.WxMaService;
import me.chanjar.weixin.common.exception.WxErrorException;

@RestController
@RequestMapping("/wx/profit")
public class WxDistributionProfitController {

	@Autowired
	private LitemallDistributionProfitService litemallDistributionProfitService;

	@Autowired
	private LitemallUserService litemallUserService;

	@Autowired
	private LitemallDistributionApplyService litemallDistributionApplyService;

	@Autowired
	private WxMaService wxService;

	@Autowired
	private WeixinUtil weixinUtil;
	
	@Autowired
	private LitemallDictionaryService litemallDictionaryService;

	/***********************************
	 * 不可提取收益内容
	 *****************************************/

	/**
	 * 下级消费所得不可提取收益金额
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectSubByPId")
	public Object selectSubByPId(Integer userId) {

		Double d = litemallDistributionProfitService.selectSubByPId(userId);

		return ResponseUtil.ok(d);
	}

	/**
	 * 下级的下级消费所得不可提取收益金额
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectSubSubByPId")
	public Object selectSubSubByPId(Integer userId) {
		Double d = litemallDistributionProfitService.selectSubSubByPId(userId);
		return ResponseUtil.ok(d);
	}

	/**
	 * 总不可提取收益金额
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectSubAllProfitByPId")
	public Object selectSubAllProfitByPId(Integer userId) {
		Double d = litemallDistributionProfitService.selectSubAllProfitByPId(userId);
		return ResponseUtil.ok(d);
	}

	/**
	 * 查询不可提取收益列表
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectSubAllProfitListByPId")
	public Object selectSubAllProfitListByPId(Integer userId, Integer orderId, String orderSn,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {

		Map<String, Object> map = litemallDistributionProfitService.selectSubAllProfitListByPId(userId, orderId,
				orderSn, page, size);

		return ResponseUtil.ok(map);
	}

	/***********************************
	 * 可提取收益内容
	 *****************************************/

	/**
	 * 查询可提取收益金额
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectExtractProfitByUserId")
	public Object selectExtractProfitByUserId(Integer userId) {
		LitemallUser user = litemallUserService.findById(userId);
		return ResponseUtil.ok(user.getMoney());
	}

	/**
	 * 查询可提取收益列表
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectProfitListByUserId")
	public Object selectProfitListByUserId(Integer userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {

		Map<String, Object> list = litemallDistributionProfitService.selectProfitListByUserId(userId, page, size);

		return ResponseUtil.ok(list);
	}

	/**
	 * 提现明细列表
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectExtractMoneyByUserId")
	public Object selectExtractMoneyByUserId(Integer userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		Map<String, Object> list = litemallDistributionProfitService.selectExtractMoneyByUserId(userId, page, size);

		return ResponseUtil.ok(list);
	}

	/**
	 * 查询可提取收益列表 + 提现明细列表
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectEarningsMoneyListByUserId")
	public Object selectEarningsMoneyListByUserId(Integer userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {

		Map<String, Object> list = litemallDistributionProfitService.selectEarningsMoneyListByUserId(userId, page,
				size);
		return ResponseUtil.ok(list);
	}

	/**
	 * 提现金额汇总
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectSumProfitMoney")
	public Object selectSumProfitMoney(Integer userId) {
		String str = litemallDistributionProfitService.selectSumProfitMoney(userId);
		return ResponseUtil.ok(str);
	}

	/***********************************
	 * 我的团队成员
	 *****************************************/

	/**
	 * 我的下级
	 *
	 * @param userId
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("selectSubUserInfoByUserId")
	public Object selectSubUserInfoByUserId(Integer userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {

		Map<String, Object> map = litemallDistributionProfitService.selectSubUserInfoByUserId(userId, page, size);

		return ResponseUtil.ok(map);
	}

	/**
	 * 查询下级用户收益订单详情
	 *
	 * @param userId
	 *            用户id
	 * @param sUserId
	 *            下级用户id
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("selectSubUserOrderGoodsInfoByUserId")
	public Object selectSubUserOrderGoodsInfoByUserId(Integer userId, Integer sUserId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		Map<String, Object> map = litemallDistributionProfitService.selectSubUserOrderGoodsInfoByUserId(userId, sUserId,
				page, size);

		return ResponseUtil.ok(map);
	}

	/**
	 * 我的下下级
	 *
	 * @param userId
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("selectSubSubUserInfoByUserId")
	public Object selectSubSubUserInfoByUserId(Integer userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {

		Map<String, Object> map = litemallDistributionProfitService.selectSubSubUserInfoByUserId(userId, page, size);

		return ResponseUtil.ok(map);
	}

	/**
	 * 查询下下级用户收益订单详情
	 *
	 * @param userId 用户id
	 * @param sUserId 下下级用户id
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("selectSubSubUserOrderGoodsByUserId")
	public Object selectSubSubUserOrderGoodsByUserId(Integer userId, Integer sUserId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {

		Map<String, Object> map = litemallDistributionProfitService.selectSubSubUserOrderGoodsByUserId(userId, sUserId,
				page, size);

		return ResponseUtil.ok(map);
	}

	/**
	 * 提现
	 *
	 * @param userId
	 * @param money
	 * @return
	 */
	@RequestMapping("getMoneyByUserId")
	public Object getMoneyByUserId(Integer userId, Double money) {

		LitemallUser user = litemallUserService.findById(userId);

		if (user.getMoney() <= 0) {
			return ResponseUtil.fail(-1, "余额为零！");
		} else if (money > user.getMoney()) {
			return ResponseUtil.fail(-1, "提现金额大余额！");
		}

		int r = litemallDistributionProfitService.getMoneyByUserId(userId, money);

		return ResponseUtil.ok(r);
	}

	/**
	 * 查询是否提交分销申请
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectApplyByUserId")
	public Object selectApplyByUserId(@RequestBody String body) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		LitemallDistributionApply litemallDistributionApply = litemallDistributionApplyService.selectByUserId(userId);
		return ResponseUtil.ok(litemallDistributionApply);
	}

	/**
	 * 提交分销申请
	 */
	@RequestMapping("saveDistributionApply")
	public Object saveDistributionApply(HttpServletRequest request,
			@RequestBody LitemallDistributionApply litemallDistributionApply) {
		if (litemallDistributionApply == null) {
			return ResponseUtil.fail402();
		}

		Integer userId = litemallDistributionApply.getCreateUserId();
		if (userId == null) {
			return ResponseUtil.fail401();
		}

		try {
			
			LitemallDistributionApply oldDistributionApply = litemallDistributionApplyService.selectByUserId(userId);
			if(oldDistributionApply != null) {
				oldDistributionApply.setAuditStatus(0);
				oldDistributionApply.setModifyTime(LocalDateTime.now());
				oldDistributionApply.setModifyUserId(userId);
				oldDistributionApply.setNickName(litemallDistributionApply.getNickName());
				oldDistributionApply.setDistributionType(litemallDistributionApply.getDistributionType());
				oldDistributionApply.setPicUrls(litemallDistributionApply.getPicUrls());
				litemallDistributionApplyService.update(oldDistributionApply);
			}else {
				litemallDistributionApply.setCreateTime(LocalDateTime.now());
				litemallDistributionApplyService.save(litemallDistributionApply);
			}
			
			
			LitemallUser user = litemallUserService.findById(userId);
			String accessToken = wxService.getAccessToken();
			String qrcodeUrl = weixinUtil.getQRcode(accessToken, userId.toString());
			user.setFormId(litemallDistributionApply.getFormId());
			if (StringUtils.isNotBlank(qrcodeUrl)) {
				user.setQrcodeUrl(qrcodeUrl);
				litemallUserService.update(user);
			}
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseUtil.ok(litemallDistributionApply);
	}
	
	
	/**
	 * 查询可提取收益列表 + 提现明细列表
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("selectDistributionTypeList")
	public Object selectDistributionTypeList(HttpServletRequest request,@RequestBody LitemallDictionary litemallDictionary) {
		List<LitemallDictionary> list = litemallDictionaryService.selectDictionaryList(litemallDictionary);
		return ResponseUtil.ok(list);
	}
	

	
	
}
