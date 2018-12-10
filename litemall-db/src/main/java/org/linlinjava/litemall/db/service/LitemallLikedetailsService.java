package org.linlinjava.litemall.db.service;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallLikedetailsMapper;
import org.linlinjava.litemall.db.domain.LitemallLikedetails;
import org.springframework.stereotype.Service;

@Service
public class LitemallLikedetailsService {
	@Resource
    private LitemallLikedetailsMapper likedetailsMapper;
	
	public int insertSelective(LitemallLikedetails record){
		return likedetailsMapper.insertSelective(record);
	}
	
	public LitemallLikedetails selectByCommentIdUserid(Integer commentid,Integer userid,Integer status){
		return likedetailsMapper.selectByCommentIdUserId(commentid, userid,status);
	}
	
	public  int updateByCommentIdUserId(LitemallLikedetails record){
		return likedetailsMapper.updateByCommentIdUserId(record);
	}

	public  int updateByCommentIdUserId2(LitemallLikedetails record){
		return likedetailsMapper.updateByCommentIdUserId2(record);
	}
	
	public LitemallLikedetails selectByUserIdValueId(Integer valueId,Integer userId, Integer status){
		return likedetailsMapper.selectByUserIdValueId(valueId, userId, status);
	}
}
