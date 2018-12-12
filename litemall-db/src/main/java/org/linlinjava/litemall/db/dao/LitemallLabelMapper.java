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
     * 根据名称获取标签列表
     */

    List<LitemallLabel> selectLabelByName(String name);

    /**
     * 根据userId查询标签
     * @param userId
     * @return
     */
    List<LitemallLabel> selectByUserId(@Param("userId") Integer userId);

    /**
     * 方法描述  根据分销商ID查询
     * @param userId 分销商ID
     * @return List<LitemallLabel>
     *
     * @author huanghaoqi
     * @date 2018年11月28日 15:41:11
     */
    List<LitemallLabel> selectByDistributionId(@Param("userId") Integer userId);
}