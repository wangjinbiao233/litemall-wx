package org.linlinjava.litemall.db.service;

import java.util.List;

import org.linlinjava.litemall.db.dao.LitemallDictionaryMapper;
import org.linlinjava.litemall.db.domain.LitemallDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

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

	public List<LitemallDictionary> selectDicList(LitemallDictionary dicObj, Integer page, Integer limit, String sort,
			String order) {
		PageHelper.startPage(page, limit);
		return litemallDictionaryMapper.selectDictionaryList(dicObj);
	}

	public int countDictionary(LitemallDictionary dicObj) {
		return litemallDictionaryMapper.countDictionary(dicObj);
	}

	public int insert(LitemallDictionary dicObj) {
		return litemallDictionaryMapper.insertSelective(dicObj);
	}

	public int updateById(LitemallDictionary dicObj) {
		return litemallDictionaryMapper.updateByPrimaryKeySelective(dicObj);
	}

	public int deleteById(Integer id) {
		//litemallDictionaryMapper.deleteByPrimaryKey(id);//物理删除
		LitemallDictionary dicobj = new LitemallDictionary();
		dicobj.setId(id);
		dicobj.setDeleted(true);
		return litemallDictionaryMapper.updateByPrimaryKeySelective(dicobj);
	}

	public List<LitemallDictionary> getDicGroupList() {
		return litemallDictionaryMapper.getDicGroupList();
	}
	
}
