package org.linlinjava.litemall.db.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallReserve;
import org.linlinjava.litemall.db.domain.LitemallReserveExample;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.domain.LitemallUser;

public interface LitemallReserveMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(LitemallReserve record);

    int insertSelective(LitemallReserve record);
    
    List<LitemallReserve> selectReserveList(LitemallReserve litemallReserve);
    
    int countReserveList(LitemallReserve litemallReserve);

    LitemallReserve selectByPrimaryKey(Integer id);
    
    List<LitemallReserve>  selectByOrderGoodsId(Integer orderGoodsId);
    
    List<LitemallReserve>  selectFinishByOrderGoodsId(Integer orderGoodsId);

    int updateByPrimaryKeySelective(LitemallReserve record);

    int updateByPrimaryKey(LitemallReserve record);

	List<LitemallReserve> selectStoreReserve(@Param("storeId")String storeId, @Param("todayStr")String todayStr);

	List<LitemallUser> selectStorePadDoctor(@Param("storeId")String storeId);

	Integer updateReserveStatus(LitemallReserveExample litemallReserveExample);

	Integer inertDoctorRes(LitemallReserveExample litemallReserveExample);
 
	BigDecimal selectStoreIncome(@Param("orderSn" )String orderSn);

	List<LitemallReserve> selectTodCus(@Param("storeId")String storeId, @Param("todayStr") String todayStr);
	
	List<LitemallReserve> selectStoreResByDoc(@Param("storeId")String storeId, @Param("todayStr")String todayStr,@Param("doctorId")String doctorId);

	List<LitemallReserve> selectStoreResSer(@Param("storeId")String storeId, @Param("todayStr")String todayStr,@Param("doctorId")String doctorId);

	List<LitemallRole> selectUserRole(LitemallUser litemallUser);

	Integer updateOrderGoodsStatus(LitemallOrderGoods ogoods);

	
}