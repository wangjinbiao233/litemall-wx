package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallFaceFourdata;



public interface LitemallFaceFourdataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LitemallFaceFourdata record);

    int insertSelective(LitemallFaceFourdata record);

    LitemallFaceFourdata selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LitemallFaceFourdata record);

    int updateByPrimaryKey(LitemallFaceFourdata record);
    
    LitemallFaceFourdata selectByTypePercentage(@Param("facetype")Integer type,@Param("percentage")Integer percentage);
}