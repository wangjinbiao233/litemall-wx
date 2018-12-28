package org.linlinjava.litemall.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallExpress;

public interface LitemallExpressMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(LitemallExpress record);

	int insertSelective(LitemallExpress record);

	LitemallExpress selectByPrimaryKey(Integer id);
	
	List<LitemallExpress> selectExpressList(LitemallExpress record);

	int updateByPrimaryKeySelective(LitemallExpress record);

	int updateByPrimaryKey(LitemallExpress record);

	/**
	 * 方法描述  根据快递公司编号查询
	 *
	 * @author huanghaoqi
	 * @date 2018年12月27日 10:01:13
	 */
	List<LitemallExpress> selectByExpressSn(@Param("expressSn") String expressSn);

}