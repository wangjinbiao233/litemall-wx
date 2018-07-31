package org.linlinjava.litemall.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallDistributionApply;

public interface LitemallDistributionApplyMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(LitemallDistributionApply record);

    int insertSelective(LitemallDistributionApply record);

    LitemallDistributionApply selectByPrimaryKey(Integer id);
    
    LitemallDistributionApply selectByUserId(@Param("createUserId") Integer createUserId);
    
    List<LitemallDistributionApply>  selectList(LitemallDistributionApply litemallDistributionApply);
    
    int  countList(LitemallDistributionApply litemallDistributionApply);

    int updateByPrimaryKeySelective(LitemallDistributionApply record);

    int updateByPrimaryKey(LitemallDistributionApply record);
}