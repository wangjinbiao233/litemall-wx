package org.linlinjava.litemall.db.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallKnowledge;

public interface LitemallKnowledgeMapper {
	
	List<LitemallKnowledge> selectknowledgeList(LitemallKnowledge record);
	
	int countKnowledge(LitemallKnowledge record);
	
	int deleteByPrimaryKey(Integer id);
	
	int insert(LitemallKnowledge record);
	
	int insertSelective(LitemallKnowledge knowledge);
	
	int updateByPrimaryKeySelective(LitemallKnowledge record);
	
	 /**
     * 点赞  或取消赞
     * @param id
     * @param one  +1 -1
     * @return
     */
    int updateCommentCountById(@Param("id")Integer id,@Param("one")Integer one);
    
    int updatePraiseCountById(@Param("id")Integer id,@Param("one")Integer one);
}
