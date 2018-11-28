package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallLabel;

import java.util.List;
import java.util.Map;

public interface LitemallLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LitemallLabel record);

    int insertSelective(LitemallLabel record);

    LitemallLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LitemallLabel record);

    int updateByPrimaryKey(LitemallLabel record);

    /**
     * 查询标签列表
     * @param label
     * @return
     */
    List<Map> selectSelective(LitemallLabel label);

    /**
     * 根据userId查询标签
     * @param userId
     * @return
     */
    List<LitemallLabel> selectByUserId(@Param("userId") String userId);
}