package org.linlinjava.litemall.db.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LitemallDistributionProfitMapper {

    Double selectSubByPId(@Param("user_id") Integer user_id);

    Double selectSubSubByPId(@Param("user_id") Integer user_id);

    List<Map> selectSubAllProfitListByPId(@Param("user_id") Integer user_id,@Param("orderId") Integer orderId,@Param("orderSn") String orderSn);

    List<Map> selectProfitListByUserId(@Param("user_id") Integer user_id);

    List<Map>  selectExtractMoneyByUserId(@Param("user_id") Integer user_id);

    List<Map> selectEarningsMoneyListByUserId(@Param("user_id") Integer user_id);

    String selectSumProfitMoney(@Param("user_id") Integer user_id);

    List<Map> selectSubUserInfoByUserId(@Param("user_id") Integer user_id);

    List<Map> selectSubUserOrderGoodsInfoByUserId(@Param("userId") Integer userId,@Param("sUserId") Integer sUserId);

    List<Map> selectSubSubUserInfoByUserId(@Param("user_id") Integer user_id);

    List<Map> selectSubSubUserOrderGoodsByUserId(@Param("userId") Integer userId,@Param("sUserId") Integer sUserId);

    int getMoneyByUserId(@Param("user_id") Integer user_id,@Param("money") Double money);

    int updatePlusMoneyByUserId(@Param("user_id") Integer user_id,@Param("money") Double money);

    int topUpMoneyByUserId(@Param("user_id") Integer user_id,@Param("money") Double money);

    List<Map> selectTransactionRecord(@Param("userId") Integer userId);
}