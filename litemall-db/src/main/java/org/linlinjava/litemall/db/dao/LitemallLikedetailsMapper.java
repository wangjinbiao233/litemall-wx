package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallLikedetails;


public interface LitemallLikedetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LitemallLikedetails record);

    int insertSelective(LitemallLikedetails record);

    LitemallLikedetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LitemallLikedetails record);

    int updateByPrimaryKey(LitemallLikedetails record);
    
    LitemallLikedetails selectByCommentIdUserId(@Param("commentId")Integer commentId,
    		@Param("userId")Integer userId,@Param("status")Integer status);
    
    int updateByCommentIdUserId(LitemallLikedetails record);

    int updateByCommentIdUserId2(LitemallLikedetails record);
    
    LitemallLikedetails selectByUserIdValueId(@Param("valueId")Integer valueId,
    		@Param("userId")Integer userId,@Param("status")Integer status);
}