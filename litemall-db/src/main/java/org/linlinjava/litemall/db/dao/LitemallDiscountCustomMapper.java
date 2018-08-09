package org.linlinjava.litemall.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallCustomDiscount;

public interface LitemallDiscountCustomMapper {


    List<LitemallCustomDiscount> selectByDiscountNameGroypBy(LitemallCustomDiscount discount);

    List<LitemallCustomDiscount> selectByKeyAndNameGroypBy(@Param("key") String key,@Param("discountName") String discountName,@Param("userId") String userId
    		,@Param("discountType") String discountType);

    List<LitemallCustomDiscount> selectUnusedByKey(@Param("key") String key);

    List<LitemallCustomDiscount> selectByUserIdAndName(@Param("userId") String userId,@Param("discountName") String discountName);
    
    List<LitemallCustomDiscount> selectMyUseByUserIdAndName(LitemallCustomDiscount litemallCustomDiscount);
   
    List<LitemallCustomDiscount> selectMyUseByUserId(LitemallCustomDiscount litemallCustomDiscount);

}