package org.linlinjava.litemall.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallOptions;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.linlinjava.litemall.db.domain.LitemallStoreGoods;

public interface LitemallStoreMapper {

	List<LitemallStore> selectByExample(LitemallStore example);
	
    int deleteByPrimaryKey(Integer id);

    int insert(LitemallStore record);

    int insertSelective(LitemallStore record);

    LitemallStore selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LitemallStore record);

    int updateByPrimaryKey(LitemallStore record);

	int countByExample(LitemallStore store);

	int insertManage(LitemallStore store);

	int deleteManage(LitemallStore store);

	List<LitemallOptions> queryGoodOptions();

	Integer slectStoreGoods(LitemallStore store);

	int updateStoreGoods(LitemallStore store);

	int insertGoods(LitemallStore store);

	List<LitemallOptions> queryProvinceOptions();

	List<LitemallOptions> queryAreaOptions(@Param("province") String province);

	int deleteStoreGoods(LitemallStore store);
	//微信小程序 门店列表
	List<LitemallStore> wxqueryStore(LitemallStore store);
	
	//微信小程序 门店服务列表
	List<LitemallGoods> wxqueryGoods(LitemallStore store);
	
	//商品所属门店
	List<LitemallStore> wxqueryGoodsStore(@Param("id")String id);

	//设置门店医生
	void insertDoctor(LitemallStore store);
	
	//删除医生
	void deleteStoreUser(LitemallStore store);

	List<LitemallStore> selectStoreByUserid(@Param("id")String id);

	//查询用界面所需的门店信息
	List<LitemallStore> queryAdminStorelist(LitemallStore store);

	//查询门店的管理员
	String selectStoreManage(LitemallStore store);
	
	//查询门店美疗师
	
	String selectStoreDoctor(LitemallStore store);

	List<LitemallStore> wxqueryAllStore();

	List<LitemallStore> queryGoodsStoreList(LitemallStore store);

	List<LitemallStore> getGoodsStoreList(LitemallStore store);

	/*
	List<LitemallStore> wxqueryGoodsStore( String goodsSn);
*/


}
