package org.linlinjava.litemall.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallFaceUserdata;



public interface LitemallFaceUserdataMapper {
    int deleteByPrimaryKey(Integer id);

    Integer insert(LitemallFaceUserdata record);

    int insertSelective(LitemallFaceUserdata record);

    LitemallFaceUserdata selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LitemallFaceUserdata record);

    int updateByPrimaryKey(LitemallFaceUserdata record);
    
    LitemallFaceUserdata selectById(@Param("id")Integer id);
    
    List<LitemallFaceUserdata> selectByUserId(@Param("userId")Integer userId);
    
    Integer selectCountByUserId(@Param("userId")Integer userId);
    
    Integer selectFirstCountByUserId(@Param("userId")Integer userId);
}