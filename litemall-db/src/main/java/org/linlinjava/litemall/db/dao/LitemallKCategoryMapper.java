package org.linlinjava.litemall.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallKCategory;

public interface LitemallKCategoryMapper {
	
	List<LitemallKCategory> selectLitemallKCategory(LitemallKCategory litemallKCategory);
	
	int insertSelective(LitemallKCategory litemallKCategory);
	
	int updateByPrimaryKeySelective(LitemallKCategory litemallKCategory);
	
	int deleteByPrimaryKey(@Param("id")Integer id);

	int countSelective(LitemallKCategory litemallKCategory);
}
