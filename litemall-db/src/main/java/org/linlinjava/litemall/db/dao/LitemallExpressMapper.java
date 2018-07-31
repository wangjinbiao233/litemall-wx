package org.linlinjava.litemall.db.dao;

import java.util.List;

import org.linlinjava.litemall.db.domain.LitemallExpress;

public interface LitemallExpressMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(LitemallExpress record);

	int insertSelective(LitemallExpress record);

	LitemallExpress selectByPrimaryKey(Integer id);
	
	List<LitemallExpress> selectExpressList(LitemallExpress record);

	int updateByPrimaryKeySelective(LitemallExpress record);

	int updateByPrimaryKey(LitemallExpress record);
}