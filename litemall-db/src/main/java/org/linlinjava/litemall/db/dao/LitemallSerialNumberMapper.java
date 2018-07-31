package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallSerialNumber;

public interface LitemallSerialNumberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LitemallSerialNumber record);

    int insertSelective(LitemallSerialNumber record);

    LitemallSerialNumber selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LitemallSerialNumber record);

    int updateByPrimaryKey(LitemallSerialNumber record);

	LitemallSerialNumber selectSerialNumberByType(@Param("type") String type);
}