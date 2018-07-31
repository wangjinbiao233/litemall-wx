package org.linlinjava.litemall.db.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.dao.LitemallDistributionApplyMapper;
import org.linlinjava.litemall.db.domain.LitemallDistributionApply;
import org.linlinjava.litemall.db.domain.LitemallReserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

/**
 * 分销申请
 */
@Service
public class LitemallDistributionApplyService {

	@Autowired
	private LitemallDistributionApplyMapper litemallDistributionApplyMapper;
	
	
	public LitemallDistributionApply selectByUserId(@Param("createUserId") Integer createUserId) {
		return litemallDistributionApplyMapper.selectByUserId(createUserId);
	}
	
	public List<LitemallDistributionApply>  selectList(LitemallDistributionApply litemallDistributionApply){
		return litemallDistributionApplyMapper.selectList(litemallDistributionApply);
	}
	
	public int update(LitemallDistributionApply litemallDistributionApply) {
		return litemallDistributionApplyMapper.updateByPrimaryKeySelective(litemallDistributionApply);
	}
	
	public int save(LitemallDistributionApply litemallDistributionApply) {
		return litemallDistributionApplyMapper.insertSelective(litemallDistributionApply);
	}
	
   public List<LitemallDistributionApply> queryDistributionApply(LitemallDistributionApply litemallDistributionApply, Integer page, Integer size, String sort, String order) {
        PageHelper.startPage(page, size);
        return litemallDistributionApplyMapper.selectList(litemallDistributionApply);
    }
    
    public int countDistributionApply(LitemallDistributionApply litemallDistributionApply, Integer page, Integer size, String sort, String order) {
    	return litemallDistributionApplyMapper.countList(litemallDistributionApply);
    }

}
