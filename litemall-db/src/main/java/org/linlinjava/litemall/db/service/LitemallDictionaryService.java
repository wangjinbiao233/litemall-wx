package org.linlinjava.litemall.db.service;

import java.util.List;

import org.linlinjava.litemall.db.dao.LitemallDictionaryMapper;
import org.linlinjava.litemall.db.domain.LitemallDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典表
 */
@Service
public class LitemallDictionaryService {

	@Autowired
	private LitemallDictionaryMapper litemallDictionaryMapper;
	
	public List<LitemallDictionary> selectDictionaryList(LitemallDictionary record) {
		return litemallDictionaryMapper.selectDictionaryList(record);
	}
	
}
