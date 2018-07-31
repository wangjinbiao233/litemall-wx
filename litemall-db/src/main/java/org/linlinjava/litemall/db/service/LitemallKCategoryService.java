package org.linlinjava.litemall.db.service;

import java.util.List;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallKCategoryMapper;
import org.linlinjava.litemall.db.domain.LitemallKCategory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class LitemallKCategoryService {
    @Resource
    private LitemallKCategoryMapper kCategoryMapper;


    public List<LitemallKCategory> querySelective(LitemallKCategory litemallKCategory, Integer page, Integer size, String sort, String order) {

        PageHelper.startPage(page, size);
        return kCategoryMapper.selectLitemallKCategory(litemallKCategory);
    }

    public int countSelective(LitemallKCategory litemallKCategory) {
        return kCategoryMapper.countSelective(litemallKCategory);
    }

	public int add(LitemallKCategory kCategory) {
		return kCategoryMapper.insertSelective(kCategory);
		
	}

	public int updateById(LitemallKCategory kCategory) {
		return kCategoryMapper.updateByPrimaryKeySelective(kCategory);
	}

	public int deleteById(Integer id) {
		return kCategoryMapper.deleteByPrimaryKey(id);
	}


}
