package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.LitemallDistributionProfitMapper;
import org.linlinjava.litemall.db.dao.LitemallEarningsMapper;
import org.linlinjava.litemall.db.domain.LitemallEarnings;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.util.WeixinPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 分销
 *
 *
 */
@Service
public class LitemallDistributionProfitService {

    @Resource
    private LitemallDistributionProfitMapper litemallDistributionProfitMapper;

    @Resource
    private LitemallEarningsMapper litemallEarningsMapper;

    @Autowired
    private LitemallUserService litemallUserService;

    /*********************************** 不可提取收益内容 *****************************************/
    /**
     * 下级消费所得不可提取收益金额
     *
     * @param userId
     * @return
     */
    public Double selectSubByPId(Integer userId){
        return litemallDistributionProfitMapper.selectSubByPId(userId);
    }

    /**
     * 下级的下级消费所得不可提取收益金额
     *
     * @param userId
     * @return
     */
    public Double selectSubSubByPId(Integer userId){
        return litemallDistributionProfitMapper.selectSubSubByPId(userId);
    }


    /**
     * 总不可提取收益金额
     *
     * @param userId
     * @return
     */
    public Double selectSubAllProfitByPId(Integer userId){
        return litemallDistributionProfitMapper.selectSubByPId(userId) + litemallDistributionProfitMapper.selectSubSubByPId(userId);
    }


    /**
     *  查询不可提取收益列表
     *
     * @param userId
     * @return
     */
    public Map<String,Object> selectSubAllProfitListByPId(Integer userId,Integer orderId,String orderSn,Integer page, Integer size){

        PageHelper.startPage(page, size);

        List<Map> list = litemallDistributionProfitMapper.selectSubAllProfitListByPId(userId,orderId,orderSn);

        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());

        return resMap;
    }

    /*********************************** 可提取收益内容 *****************************************/

    /**
     * 查询可提取收益列表
     *
     * @param userId
     * @return
     */
    public Map<String,Object> selectProfitListByUserId(Integer userId, Integer page, Integer size){

        PageHelper.startPage(page, size);

        List<Map> list = litemallDistributionProfitMapper.selectProfitListByUserId(userId);

        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());


        return resMap;
    }


    /**
     * 提现明细列表
     *
     * @param userId
     * @return
     */
    public Map<String,Object> selectExtractMoneyByUserId(Integer userId, Integer page, Integer size){

        PageHelper.startPage(page, size);

        List<Map> list = litemallDistributionProfitMapper.selectExtractMoneyByUserId(userId);

        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());

        return resMap;
    }

    /**
     * 查询可提取收益列表 +  提现明细列表
     *
     * @param userId
     * @return
     */
    public Map<String,Object> selectEarningsMoneyListByUserId(Integer userId, Integer page, Integer size){

        PageHelper.startPage(page, size);

        List<Map> list = litemallDistributionProfitMapper.selectEarningsMoneyListByUserId(userId);

        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());

        return resMap;
    }

    /**
     *提现金额汇总
     *
     * @param userId
     * @return
     */
    public String selectSumProfitMoney(Integer userId){
        return litemallDistributionProfitMapper.selectSumProfitMoney(userId);
    }


    /*********************************** 我的团队成员 *****************************************/


    /**
     * 我的下级
     *
     * @param userId
     * @return
     */
    public Map<String,Object> selectSubUserInfoByUserId(Integer userId, Integer page, Integer size){

        PageHelper.startPage(page, size);

        List<Map> list = litemallDistributionProfitMapper.selectSubUserInfoByUserId(userId);

        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());

        return resMap;
    }

    /**
     *
     * 查询下级用户收益订单详情
     *
     * @param userId 用户id
     * @param sUserId 下级用户id
     * @param page
     * @param size
     * @return
     */
    public Map<String,Object> selectSubUserOrderGoodsInfoByUserId(Integer userId,Integer sUserId, Integer page, Integer size){

        PageHelper.startPage(page, size);

        List<Map> list = litemallDistributionProfitMapper.selectSubUserOrderGoodsInfoByUserId(userId,sUserId);

        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());

        return resMap;
    }

    /**
     * 我的下下级
     *
     * @param userId
     * @return
     */
    public Map<String,Object> selectSubSubUserInfoByUserId(Integer userId, Integer page, Integer size){

        PageHelper.startPage(page, size);

        List<Map> list = litemallDistributionProfitMapper.selectSubSubUserInfoByUserId(userId);

        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());

        return resMap;
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
    public Map<String,Object> selectSubSubUserOrderGoodsByUserId(Integer userId,Integer sUserId, Integer page, Integer size){

        PageHelper.startPage(page, size);

        List<Map> list = litemallDistributionProfitMapper.selectSubSubUserOrderGoodsByUserId(userId,sUserId);

        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());

        return resMap;
    }




    /**
     * 提现
     *
     * @param user_id
     * @param money
     * @return
     */
    public int getMoneyByUserId(Integer user_id,Double money){

        LitemallEarnings litemallEarnings = new LitemallEarnings();
        litemallEarnings.setOperation(2);
        litemallEarnings.setOperationTime(LocalDateTime.now());
        litemallEarnings.setProfitUserId(user_id);
        litemallEarnings.setProfitMoney(BigDecimal.valueOf(money));
        litemallEarnings.setRemark("提现");
        litemallEarnings.setCreateUserId(user_id);
        litemallEarnings.setCreateTime(LocalDateTime.now());

        litemallEarningsMapper.insertSelective(litemallEarnings);
        
        
        /*向微信API发起提现请求*/
        Double wxMoney = money * 100;
        int amount = wxMoney.intValue();
        LitemallUser litemallUser = litemallUserService.findById(user_id);
        String openid = litemallUser.getWeixinOpenid();
        String username = litemallUser.getUsername() != null ? litemallUser.getUsername() : "";
        /*
        Map<String, String>  map = WeixinPay.transfer(openid, amount, "提现", litemallEarnings.getId().toString());*/
        Map<String, String>  map = WeixinPay.transferWithdraw(openid, username, amount, "提现", litemallEarnings.getId().toString());
        String state = map.get("state");

        if("SUCCESS".equals(state)) {
            return litemallDistributionProfitMapper.getMoneyByUserId(user_id, money);
        } else {
            litemallEarnings.setOperation(6);
            litemallEarnings.setRemark("提现失败");
            litemallEarningsMapper.updateByPrimaryKeySelective(litemallEarnings);
            return 0;
        }
    }


    /**
     * 充值
     *
     * @param user_id
     * @param money
     * @return
     */
    public int topUpMoneyByUserId(Integer user_id,Double money){
        LitemallEarnings litemallEarnings = new LitemallEarnings();
        litemallEarnings.setOperation(3);
        litemallEarnings.setOperationTime(LocalDateTime.now());
        litemallEarnings.setProfitUserId(user_id);
        litemallEarnings.setProfitMoney(BigDecimal.valueOf(money));
        litemallEarnings.setRemark("充值");
        litemallEarnings.setCreateUserId(user_id);
        litemallEarnings.setCreateTime(LocalDateTime.now());

        litemallEarningsMapper.insertSelective(litemallEarnings);

        return litemallDistributionProfitMapper.topUpMoneyByUserId(user_id,money);
    }


    /**
     * 消费
     *
     * @param user_id
     * @param money
     * @return 1成功，0失败
     */
    public int consumptionMoneyByUserId(Integer user_id,Double money,String orderSn){


        LitemallUser user = litemallUserService.findById(user_id);

        if (user.getMoney() <=0 ){
            return 0;
        } else if (money > user.getMoney()) {
            return 0;
        }

        LitemallEarnings litemallEarnings = new LitemallEarnings();
        litemallEarnings.setOperation(4);
        litemallEarnings.setOperationTime(LocalDateTime.now());
        litemallEarnings.setProfitUserId(user_id);
        litemallEarnings.setProfitMoney(BigDecimal.valueOf(money));
        litemallEarnings.setRemark("消费");
        litemallEarnings.setCreateUserId(user_id);
        litemallEarnings.setOrderSn(orderSn);
        litemallEarnings.setCreateTime(LocalDateTime.now());

        litemallEarningsMapper.insertSelective(litemallEarnings);

        return litemallDistributionProfitMapper.getMoneyByUserId(user_id,money);
    }

    /**
     * 退款
     *
     * @param user_id
     * @param money
     * @return
     */
    public int refundMoneyByUserId(Integer user_id,Double money,String orderSn){
        LitemallEarnings litemallEarnings = new LitemallEarnings();
        litemallEarnings.setOperation(5);
        litemallEarnings.setOperationTime(LocalDateTime.now());
        litemallEarnings.setProfitUserId(user_id);
        litemallEarnings.setProfitMoney(BigDecimal.valueOf(money));
        litemallEarnings.setRemark("退款");
        litemallEarnings.setCreateUserId(user_id);
        litemallEarnings.setCreateTime(LocalDateTime.now());
        litemallEarnings.setOrderSn(orderSn);

        litemallEarningsMapper.insertSelective(litemallEarnings);

        return litemallDistributionProfitMapper.topUpMoneyByUserId(user_id,money);
    }

    /**
     * 根据userId查询用户的充值和提现记录
     * @param userId
     * @return
     */
	public List<Map> findTransactionRecord(Integer userId) {
		return litemallDistributionProfitMapper.selectTransactionRecord(userId);
	}

	/**
	 * 充值
	 * @param money
	 * @param userId
	 * @param adminId
	 */
	public int rechargeMoneyByUserId(Double money, Integer userId, Integer adminId) {
		LitemallEarnings litemallEarnings = new LitemallEarnings();
        litemallEarnings.setOperation(3);
        litemallEarnings.setOperationTime(LocalDateTime.now());
        litemallEarnings.setProfitUserId(userId);
        litemallEarnings.setProfitMoney(BigDecimal.valueOf(money));
        litemallEarnings.setRemark("充值");
        litemallEarnings.setCreateUserId(adminId);
        litemallEarnings.setCreateTime(LocalDateTime.now());

        litemallEarningsMapper.insertSelective(litemallEarnings);
        return litemallDistributionProfitMapper.topUpMoneyByUserId(userId,money);
	}

}
