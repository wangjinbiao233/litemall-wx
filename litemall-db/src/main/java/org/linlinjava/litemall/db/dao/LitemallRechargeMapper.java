package org.linlinjava.litemall.db.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallRecharge;

public interface LitemallRechargeMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(LitemallRecharge record);

	int insertSelective(LitemallRecharge record);

	LitemallRecharge selectByPrimaryKey(Integer id);
	
	List<Map> selectRechargeRecord(@Param("userId") Integer userId);

	int updateByPrimaryKeySelective(LitemallRecharge record);

	int updateByPrimaryKey(LitemallRecharge record);
	
	int updateRechargeMoneyByUserId(@Param("userId") Integer userId,@Param("rechargeMoney") Double rechargeMoney);
}