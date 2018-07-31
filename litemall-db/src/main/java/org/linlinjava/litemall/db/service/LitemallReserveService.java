package org.linlinjava.litemall.db.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallReserveMapper;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallReserve;
import org.linlinjava.litemall.db.domain.LitemallReserveExample;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class LitemallReserveService {
    
    @Resource
    private LitemallReserveMapper litemallReserveMapper;

    public List<LitemallReserve>  selectByOrderGoodsId(Integer orderGoodsId) {
    	return litemallReserveMapper.selectByOrderGoodsId(orderGoodsId);
    }
    
    public List<LitemallReserve>  selectFinishByOrderGoodsId(Integer orderGoodsId) {
    	return litemallReserveMapper.selectFinishByOrderGoodsId(orderGoodsId);
    }
    
    public LitemallReserve  selectById(Integer id) {
    	return litemallReserveMapper.selectByPrimaryKey(id);
    }
    
    public int  deleteById(Integer id) {
    	return litemallReserveMapper.deleteByPrimaryKey(id);
    }
    
    public List<LitemallReserve> selectReserveList(LitemallReserve litemallReserve){
    	return litemallReserveMapper.selectReserveList(litemallReserve);    	
    }
    
    public List<LitemallReserve> queryReserve(LitemallReserve litemallReserve, Integer page, Integer size, String sort, String order) {
        PageHelper.startPage(page, size);
        return litemallReserveMapper.selectReserveList(litemallReserve);
    }
    
    public int countReserve(LitemallReserve litemallReserve, Integer page, Integer size, String sort, String order) {
    	return litemallReserveMapper.countReserveList(litemallReserve);
    }
    
    
    public int update(LitemallReserve litemallReserve) {
        return litemallReserveMapper.updateByPrimaryKeySelective(litemallReserve);
    }
    
    public int add(LitemallReserve litemallReserve) {
        return litemallReserveMapper.insertSelective(litemallReserve);
    }

    //管理员登录显示预约信息
	public List<LitemallReserve> selectStoreReserve(String storeId, String todayStr) {
		
		return litemallReserveMapper.selectStoreReserve(storeId,todayStr);
	}

	public List<LitemallUser> selectStorePadDoctor(String storeId) {
		
		return litemallReserveMapper.selectStorePadDoctor(storeId);
	}
	
	//设置更新预定信息
	public Integer updateReserveStatus(LitemallReserveExample litemallReserveExample) {
		
		return  litemallReserveMapper.updateReserveStatus(litemallReserveExample);
	}
	//查询收入
	public BigDecimal selectStoreIncome(String orderSn) {
		
		return litemallReserveMapper.selectStoreIncome(orderSn);
	}
	//查询当天到场客户数
	public List<LitemallReserve> selectTodCus(String storeId, String todayStr) {
	
		return  litemallReserveMapper.selectTodCus(storeId,todayStr);
	}
	
  //医生登录显示预约信息
	public List<LitemallReserve> selectStoreResByDoc(String storeId, String todayStr,String doctorId) {
		
		return litemallReserveMapper.selectStoreResByDoc(storeId,todayStr,doctorId);
	}

	//医生登录后已服务数
	public List<LitemallReserve> selectStoreResSer(String storeId, String todayStr, String doctorId) {
		
		return litemallReserveMapper.selectStoreResSer(storeId,todayStr,doctorId);
	}
	
	//查询用户的角色
	public List<LitemallRole> selectUserRole(LitemallUser litemallUser) {
		
		return litemallReserveMapper.selectUserRole(litemallUser);
	}

	/**
	 * 预约订单项目服务已完成，修改订单详情状态
	 * @param status 
	 * @param orderGoodsId
	 * @return
	 */
	public Integer updateOrderGoodsStatus(LitemallOrderGoods ogoods) {
		return litemallReserveMapper.updateOrderGoodsStatus(ogoods);
	}

	

}
