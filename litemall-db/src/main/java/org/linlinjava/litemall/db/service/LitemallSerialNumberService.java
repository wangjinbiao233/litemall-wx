package org.linlinjava.litemall.db.service;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallSerialNumberMapper;
import org.linlinjava.litemall.db.domain.LitemallSerialNumber;
import org.springframework.stereotype.Service;

@Service
public class LitemallSerialNumberService {

	@Resource
	private LitemallSerialNumberMapper litemallSerialNumberMapper;
	
	public LitemallSerialNumber findSerialNumberByType (String type) {
		return litemallSerialNumberMapper.selectSerialNumberByType(type);
	}

	public void updateSerialNumber(LitemallSerialNumber serialNumber) {
		litemallSerialNumberMapper.updateByPrimaryKeySelective(serialNumber);
	}
	
}
