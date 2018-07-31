package org.linlinjava.litemall.db.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallRechargeMapper;
import org.linlinjava.litemall.db.dao.LitemallUserMapper;
import org.linlinjava.litemall.db.domain.LitemallRecharge;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 充值金管理
 */
@Service
public class LitemallRechargeService {

    @Autowired
    private LitemallRechargeMapper litemallRechargeMapper;

    @Resource
    private LitemallUserMapper userMapper;   		


    /**
     * 消费
     *
     * @param user_id
     * @param money
     * @return 1成功，0失败
     */
    public int consumptionMoneyByUserId(Integer userId,Double rechargeMoney,String orderSn){

        LitemallUser user = userMapper.selectByPrimaryKey(userId);

        if (user.getRechargeMoney() <=0 ){
            return 0;
        } else if (rechargeMoney > user.getRechargeMoney()) {
            return 0;
        }

        LitemallRecharge litemallRecharge = new LitemallRecharge();
        litemallRecharge.setOperation(2);
        litemallRecharge.setOperationTime(LocalDateTime.now());
        litemallRecharge.setRechargeUserId(userId);
        litemallRecharge.setRechargeMoney(BigDecimal.valueOf(rechargeMoney));
        litemallRecharge.setRemark("消费");
        litemallRecharge.setCreateUserId(userId);
        litemallRecharge.setCreateTime(LocalDateTime.now());
        litemallRecharge.setOrderSn(orderSn);
        litemallRechargeMapper.insertSelective(litemallRecharge);

        user.setRechargeMoney(user.getRechargeMoney()-rechargeMoney);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 退款
     *
     * @param user_id
     * @param money
     * @return
     */
    public int refundMoneyByUserId(Integer userId,Double rechargeMoney,String orderSn){

        LitemallRecharge litemallRecharge = new LitemallRecharge();
        litemallRecharge.setOperation(3);
        litemallRecharge.setOperationTime(LocalDateTime.now());
        litemallRecharge.setRechargeUserId(userId);
        litemallRecharge.setRechargeMoney(BigDecimal.valueOf(rechargeMoney));
        litemallRecharge.setRemark("退款");
        litemallRecharge.setCreateUserId(userId);
        litemallRecharge.setCreateTime(LocalDateTime.now());
        litemallRecharge.setOrderSn(orderSn);
        litemallRechargeMapper.insertSelective(litemallRecharge);
        
        return litemallRechargeMapper.updateRechargeMoneyByUserId(userId,rechargeMoney);

    }
    
    /**
     * 根据userId查询用户的充值和提现记录
     * @param userId
     * @return
     */
	public List<Map> findTransactionRecord(Integer userId) {
		return litemallRechargeMapper.selectRechargeRecord(userId);
	}
	
	
	/**
	 * 充值
	 * @param money
	 * @param userId
	 * @param adminId
	 */
	public int rechargeMoneyByUserId(Double rechargeMoney, Integer userId, Integer adminId,Integer rechargeType) {
		
		LitemallRecharge litemallRecharge = new LitemallRecharge();
        litemallRecharge.setOperation(1);
        litemallRecharge.setOperationTime(LocalDateTime.now());
        litemallRecharge.setRechargeUserId(userId);
        litemallRecharge.setRechargeMoney(BigDecimal.valueOf(rechargeMoney));
        litemallRecharge.setRemark("充值");
        litemallRecharge.setCreateUserId(adminId);
        litemallRecharge.setRechargeType(rechargeType);
        litemallRecharge.setCreateTime(LocalDateTime.now());

        litemallRechargeMapper.insertSelective(litemallRecharge);
        return litemallRechargeMapper.updateRechargeMoneyByUserId(userId,rechargeMoney);
        
	}
	
}
