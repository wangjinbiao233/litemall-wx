package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.linlinjava.litemall.db.dao.LitemallAddressMapper;
import org.linlinjava.litemall.db.domain.LitemallAddress;
import org.linlinjava.litemall.db.domain.LitemallAddressExample;
import org.linlinjava.litemall.db.domain.LitemallCart;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallAddressService {
    @Resource
    private LitemallAddressMapper addressMapper;

    public List<LitemallAddress> queryByUid(Integer uid) {
        LitemallAddressExample example = new LitemallAddressExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        return addressMapper.selectByExample(example);
    }

    public LitemallAddress findById(Integer id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    public int add(LitemallAddress address) {
        return addressMapper.insertSelective(address);
    }

    public int update(LitemallAddress address) {
        return addressMapper.updateByPrimaryKeySelective(address);
    }

    public void delete(Integer id) {
        LitemallAddress address = addressMapper.selectByPrimaryKey(id);
        if(address == null){
            return;
        }
        address.setDeleted(true);
        addressMapper.updateByPrimaryKey(address);
    }

    public LitemallAddress findDefault(Integer userId) {
        LitemallAddressExample example = new LitemallAddressExample();
        example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    public void resetDefault(Integer userId) {
        LitemallAddress address = new LitemallAddress();
        address.setIsDefault(false);
        LitemallAddressExample example = new LitemallAddressExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        addressMapper.updateByExampleSelective(address, example);
    }

    public List<LitemallAddress> querySelective(Integer userId, String name, Integer page, Integer limit, String sort, String order) {
        LitemallAddressExample example = new LitemallAddressExample();
        LitemallAddressExample.Criteria criteria = example.createCriteria();

        if(userId !=  null){
            criteria.andUserIdEqualTo(userId);
        }
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        PageHelper.startPage(page, limit);
        return addressMapper.selectByExample(example);
    }

    public int countSelective(Integer userId, String name, Integer page, Integer limit, String sort, String order) {
        LitemallAddressExample example = new LitemallAddressExample();
        LitemallAddressExample.Criteria criteria = example.createCriteria();

        if(userId !=  null){
            criteria.andUserIdEqualTo(userId);
        }
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        return (int)addressMapper.countByExample(example);
    }

    public void updateById(LitemallAddress address) {
        addressMapper.updateByPrimaryKeySelective(address);
    }

	public PageInfo<LitemallAddress> querySelective(LitemallAddress address, Integer page, Integer limit, String sort,
			String order) {
		PageHelper.startPage(page, limit);
		List<LitemallAddress> list = addressMapper.selectByAddress(address);
        PageInfo<LitemallAddress> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
}
