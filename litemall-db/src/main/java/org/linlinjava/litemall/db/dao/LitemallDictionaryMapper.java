package org.linlinjava.litemall.db.dao;

import java.util.List;

import org.linlinjava.litemall.db.domain.LitemallDictionary;

public interface LitemallDictionaryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(LitemallDictionary record);

    int insertSelective(LitemallDictionary record);

    LitemallDictionary selectByPrimaryKey(Integer id);
    
    List<LitemallDictionary> selectDictionaryList(LitemallDictionary record);

    int updateByPrimaryKeySelective(LitemallDictionary record);

    int updateByPrimaryKey(LitemallDictionary record);

	int countDictionary(LitemallDictionary dicObj);

	List<LitemallDictionary> getDicGroupList();
}