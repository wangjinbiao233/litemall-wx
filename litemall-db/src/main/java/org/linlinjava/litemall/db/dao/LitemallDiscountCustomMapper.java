package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallCustomDiscount;
import org.linlinjava.litemall.db.domain.LitemallDiscount;

import java.math.BigDecimal;
import java.util.List;

public interface LitemallDiscountCustomMapper {


    List<LitemallCustomDiscount> selectByDiscountNameGroypBy(LitemallCustomDiscount discount);

    List<LitemallCustomDiscount> selectByKeyAndNameGroypBy(@Param("key") String key,@Param("discountName") String discountName,@Param("userId") String userId);

    List<LitemallCustomDiscount> selectUnusedByKey(@Param("key") String key);

    List<LitemallCustomDiscount> selectByUserIdAndName(@Param("userId") String userId,@Param("discountName") String discountName);
    
    List<LitemallCustomDiscount> selectMyUseByUserIdAndName(LitemallCustomDiscount litemallCustomDiscount);
   
    List<LitemallCustomDiscount> selectMyUseByUserId(LitemallCustomDiscount litemallCustomDiscount);

}