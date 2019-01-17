package org.linlinjava.litemall.db.service;

import java.util.List;

import org.linlinjava.litemall.db.dao.LitemallStoreMapper;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallOptions;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;

@Service
public class LitemallStoreService {

	@Resource
	private LitemallStoreMapper storeAdMapper;

	public List<LitemallStore> querySelective(LitemallStore store, Integer page, Integer limit, String sort,
			String order) {
		


        PageHelper.startPage(page, limit);
        
        // Joiner.on(",").join(a)
        List<LitemallStore> adList =  storeAdMapper.selectByExample(store);
        for (LitemallStore litemallStore : adList) {
        
        	 String res =  storeAdMapper.selectStoreManage(litemallStore);
        	 String doctor = storeAdMapper.selectStoreDoctor(litemallStore);
        	 if(null!=res && !("".equals(res))) {
        		 litemallStore.setManagerName(res);
        	 }
        	 if(null!=doctor && !("".equals(doctor))) {
        		 litemallStore.setDoctor(doctor);
        	 }
        	 
		}
        return adList;
		
	}

	public int countSelective(LitemallStore store, Integer page, Integer limit, String sort, String order) {
		
		return storeAdMapper.countByExample(store);
	}

	public void add(LitemallStore store) {
		storeAdMapper.insertSelective(store);
		
	}

	public void updateById(LitemallStore store) {
		
		storeAdMapper.updateByPrimaryKeySelective(store);
	}

	public void deleteById(Integer id) {
		storeAdMapper.deleteByPrimaryKey(id);
		
	}

	public void addManage(LitemallStore store) {
		storeAdMapper.insertManage(store);
		
	}

	public void deleteManage(LitemallStore store) {
		storeAdMapper.deleteManage(store);
		
	}

	public List<LitemallOptions> queryGoodOptions() {
		
		return storeAdMapper.queryGoodOptions();
	}

	public Integer slectStoreGoods(LitemallStore store) {
		
		return storeAdMapper.slectStoreGoods(store);
	}

	public void updateStoreGoods(LitemallStore store) {
		storeAdMapper.updateStoreGoods(store);
		
	}

	public void addStoreGoods(LitemallStore store) {
		
		storeAdMapper.insertGoods(store);
		
	}

	public List<LitemallOptions> queryProvinceOptions() {
		
		return storeAdMapper.queryProvinceOptions();
	}

	public List<LitemallOptions> queryAreaOptions(String province) {
		
		return storeAdMapper.queryAreaOptions(province);
	}

	public void deleteStoreGoods(LitemallStore store) {
		
		storeAdMapper.deleteStoreGoods(store);
	}
	
	//微信门店列表接口
	public List<LitemallStore> wxqueryStore(LitemallStore store) {
		
		return storeAdMapper.wxqueryStore(store);
	}
	
	
	//微信门店服务
	public List<LitemallGoods> wxqueryGoods(LitemallStore store) {
		
		return storeAdMapper.wxqueryGoods(store);
	}
	
	//商品所属门店
	public List<LitemallStore> wxqueryGoodsStore(String  id) {
	
		return storeAdMapper.wxqueryGoodsStore(id);
	}

	public void addDoctor(LitemallStore store) {
		storeAdMapper.insertDoctor(store);
		
	}

	public void deleteStoreUser(LitemallStore store) {
		storeAdMapper.deleteStoreUser(store);
		
	}
	
	/**
	 * 查询登录用户所属门店信息
	 * @param adminId
	 * @return
	 */
	public List<LitemallStore> selectStoreByUserid(Integer adminId) {
		return storeAdMapper.selectStoreByUserid(adminId+"");
	}

	public List<LitemallStore> queryAdminStorelist(LitemallStore store, Integer page, Integer limit, String sort,
			String order) {
		

        PageHelper.startPage(page, limit);
        
        return storeAdMapper.queryAdminStorelist(store);
	}

	public List<LitemallStore> wxqueryAllStore() {
		
		return storeAdMapper.wxqueryAllStore();
	}

	public List<LitemallStore> queryAllStoreList() {
		return storeAdMapper.wxqueryAllStore();
	}

	public List<LitemallStore> queryGoodsStoreList(LitemallStore store) {
		return storeAdMapper.queryGoodsStoreList(store);
	}

	public List<LitemallStore> getGoodsStoreList(LitemallStore store) {
		return storeAdMapper.getGoodsStoreList(store);
	}

	//根据id查询门店

	public LitemallStore selectStoreById(String storeId){
		return storeAdMapper.selectStoreById(Integer.valueOf(storeId));
	}

	

}
