package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.linlinjava.litemall.db.domain.LitemallCollect;
import org.linlinjava.litemall.db.dao.LitemallCollectMapper;
import org.linlinjava.litemall.db.domain.LitemallCollectExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallCollectService {
    @Resource
    private LitemallCollectMapper collectMapper;

    public int count(int uid, Integer gid) {
        LitemallCollectExample example = new LitemallCollectExample();
        example.or().andUserIdEqualTo(uid).andValueIdEqualTo(gid).andDeletedEqualTo(false);
        return (int)collectMapper.countByExample(example);
    }

    public List<LitemallCollect> queryByType(Integer userId, Integer typeId, Integer page, Integer size) {
        LitemallCollectExample example = new LitemallCollectExample();
        example.or().andUserIdEqualTo(userId).andTypeIdEqualTo(typeId).andDeletedEqualTo(false);
        example.setOrderByClause(LitemallCollect.Column.addTime.desc());
        PageHelper.startPage(page, size);
        return collectMapper.selectByExample(example);
    }

    public int countByType(Integer userId, Integer typeId) {
        LitemallCollectExample example = new LitemallCollectExample();
        example.or().andUserIdEqualTo(userId).andTypeIdEqualTo(typeId).andDeletedEqualTo(false);
        return (int)collectMapper.countByExample(example);
    }

    public LitemallCollect queryByTypeAndValue(Integer userId, Integer typeId, Integer valueId) {
        LitemallCollectExample example = new LitemallCollectExample();
        example.or().andUserIdEqualTo(userId).andValueIdEqualTo(valueId).andTypeIdEqualTo(typeId).andDeletedEqualTo(false);
        return collectMapper.selectOneByExample(example);
    }

    public void deleteById(Integer id) {
        LitemallCollect collect = collectMapper.selectByPrimaryKey(id);
        if(collect == null){
            return;
        }
        collect.setDeleted(true);
        collectMapper.updateByPrimaryKey(collect);
    }

    public int add(LitemallCollect collect) {
        return collectMapper.insertSelective(collect);
    }

    public List<LitemallCollect> querySelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        LitemallCollectExample example = new LitemallCollectExample();
        LitemallCollectExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(userId)){
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if(!StringUtils.isEmpty(valueId)){
            criteria.andValueIdEqualTo(Integer.valueOf(valueId));
        }
        criteria.andDeletedEqualTo(false);

        PageHelper.startPage(page, size);
        return collectMapper.selectByExample(example);
    }

    public int countSelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        LitemallCollectExample example = new LitemallCollectExample();
        LitemallCollectExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(userId)){
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if(!StringUtils.isEmpty(valueId)){
            criteria.andValueIdEqualTo(Integer.valueOf(valueId));
        }
        criteria.andDeletedEqualTo(false);

        return (int)collectMapper.countByExample(example);
    }

    public void updateById(LitemallCollect collect) {
        collectMapper.updateByPrimaryKeySelective(collect);
    }

    public LitemallCollect findById(Integer id) {
        return collectMapper.selectByPrimaryKey(id);
    }

	public Map<String, Object> querySelective(LitemallCollect collect, Integer page, Integer limit, String sort,
			String order) {
		Map<String, Object> data = new HashMap<>();
		PageHelper.startPage(page, limit);
	    List<LitemallCollect> list = collectMapper.selectByLitemallCollect(collect);
	    PageInfo<LitemallCollect> pageInfo = new PageInfo<>(list);
	    data.put("total", pageInfo.getTotal());
        data.put("items", pageInfo.getList());
		return data;
	}
}
