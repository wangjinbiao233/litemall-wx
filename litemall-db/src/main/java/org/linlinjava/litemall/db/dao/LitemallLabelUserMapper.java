package org.linlinjava.litemall.db.dao;

import org.linlinjava.litemall.db.domain.LitemallLabelUser;

public interface LitemallLabelUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LitemallLabelUser record);

    int insertSelective(LitemallLabelUser record);

    LitemallLabelUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LitemallLabelUser record);

    int updateByPrimaryKey(LitemallLabelUser record);
}