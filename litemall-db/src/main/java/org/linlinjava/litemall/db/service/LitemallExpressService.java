package org.linlinjava.litemall.db.service;

import java.util.List;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallExpressMapper;
import org.linlinjava.litemall.db.domain.LitemallExpress;
import org.springframework.stereotype.Service;

@Service
public class LitemallExpressService {
	@Resource
	private LitemallExpressMapper litemallExpressMapper;

	public int add(LitemallExpress litemallExpress) {
		return litemallExpressMapper.insertSelective(litemallExpress);
	}

	public List<LitemallExpress> query(LitemallExpress litemallExpress) {
		return litemallExpressMapper.selectExpressList(litemallExpress);
	}

	public LitemallExpress findById(Integer orderId) {
		return litemallExpressMapper.selectByPrimaryKey(orderId);
	}

	public void updateById(LitemallExpress litemallExpress) {
		litemallExpressMapper.updateByPrimaryKeySelective(litemallExpress);
	}

	public void deleteById(Integer id) {
		LitemallExpress litemallExpress = litemallExpressMapper.selectByPrimaryKey(id);
		if (litemallExpress == null) {
			return;
		}
		litemallExpress.setDeleted(true);
		litemallExpressMapper.updateByPrimaryKey(litemallExpress);
	}

}
