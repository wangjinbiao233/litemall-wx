package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallKnowledgeGoods;

public interface LitemallKnowledgeGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LitemallKnowledgeGoods record);

    int insertSelective(LitemallKnowledgeGoods record);

    LitemallKnowledgeGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LitemallKnowledgeGoods record);

    int updateByPrimaryKey(LitemallKnowledgeGoods record);

    int deleteLitemallKnowledgeGoods(@Param("goodsId") String goodsSn, @Param("knowledgeId") Integer knowledgeId);
}