package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.dao.LitemallCommentMapper;
import org.linlinjava.litemall.db.domain.LitemallComment;
import org.linlinjava.litemall.db.domain.LitemallCommentExample;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.List;

@Service
public class LitemallCommentService {
    @Resource
    private LitemallCommentMapper commentMapper;

    public List<LitemallComment> queryGoodsByGid(Integer id, int offset, int limit) {
        LitemallCommentExample example = new LitemallCommentExample();
        example.setOrderByClause(LitemallComment.Column.addTime.desc());
        example.or().andValueIdEqualTo(id).andTypeIdEqualTo((byte)0).andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return commentMapper.selectByExample(example);
    }

    public int countGoodsByGid(Integer id, int offset, int limit) {
        LitemallCommentExample example = new LitemallCommentExample();
        example.or().andValueIdEqualTo(id).andTypeIdEqualTo((byte)0).andDeletedEqualTo(false);
        return (int)commentMapper.countByExample(example);
    }

    public List<LitemallComment> query(Byte typeId, Integer valueId, Integer showType, Integer offset, Integer limit) {
       // LitemallCommentExample example = new LitemallCommentExample();
       // example.setOrderByClause(LitemallComment.Column.addTime.desc());
        
        //
        LitemallComment lt=new LitemallComment();
        lt.setValueId(valueId);
        lt.setTypeId(typeId);
        if(showType == 0) {
        	 lt.setHasPicture(false);
            //example.or().andValueIdEqualTo(valueId).andTypeIdEqualTo(typeId).andDeletedEqualTo(false);
        }
        else if(showType == 1){
        	 lt.setHasPicture(true);
           // example.or().andValueIdEqualTo(valueId).andTypeIdEqualTo(typeId).andHasPictureEqualTo(true).andDeletedEqualTo(false);
        }
        else{
            Assert.state(false, "showType不支持");
        }
        PageHelper.startPage(offset, limit);
        return commentMapper.selectByIdTypeId(lt);
    }

    public int count(Byte typeId, Integer valueId, Integer showType, Integer offset, Integer size){
        LitemallComment lt=new LitemallComment();
        lt.setValueId(valueId);
        lt.setTypeId(typeId);
       
//        LitemallCommentExample example = new LitemallCommentExample();
        if(showType == 0) {
        	 lt.setHasPicture(false);
           // example.or().andValueIdEqualTo(valueId).andTypeIdEqualTo(typeId).andDeletedEqualTo(false);
        }
        else if(showType == 1){
        	lt.setHasPicture(true);
           // example.or().andValueIdEqualTo(valueId).andTypeIdEqualTo(typeId).andHasPictureEqualTo(true).andDeletedEqualTo(false);
        }
        else{
            Assert.state(false, "");
        }
        return (int)commentMapper.countValueIdTypeId(lt);
    }

    public Integer save(LitemallComment comment) {
        return commentMapper.insertSelective(comment);
    }


    public void update(LitemallComment comment) {
        commentMapper.updateByPrimaryKeySelective(comment);
    }


    public List<LitemallComment> querySelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        LitemallCommentExample example = new LitemallCommentExample();
        example.setOrderByClause(LitemallComment.Column.addTime.desc());
        LitemallCommentExample.Criteria criteria = example.createCriteria();

        LitemallComment litemallComment = new LitemallComment();
        if(!StringUtils.isEmpty(userId)){
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
            litemallComment.setUserId(Integer.valueOf(userId));
        }
        if(!StringUtils.isEmpty(valueId)){
            criteria.andValueIdEqualTo(Integer.valueOf(valueId)).andTypeIdEqualTo((byte)0);
            litemallComment.setValueId(Integer.valueOf(valueId));
        }
        criteria.andDeletedEqualTo(false);
        litemallComment.setDeleted(false);
        PageHelper.startPage(page, size);
//        commentMapper.selectByExample(example);
        return commentMapper.selectCommentList(litemallComment);
//        return commentMapper.selectByExample(example);
    }

    public int countSelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        LitemallCommentExample example = new LitemallCommentExample();
        LitemallCommentExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(userId)){
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if(!StringUtils.isEmpty(valueId)){
            criteria.andValueIdEqualTo(Integer.valueOf(valueId)).andTypeIdEqualTo((byte)0);
        }
        criteria.andDeletedEqualTo(false);

        return (int)commentMapper.countByExample(example);
    }

    public void updateById(LitemallComment comment) {
        commentMapper.updateByPrimaryKeySelective(comment);
    }

    public void deleteById(Integer id) {
        LitemallComment comment = commentMapper.selectByPrimaryKey(id);
        if(comment == null){
            return;
        }
        comment.setDeleted(true);
        commentMapper.updateByPrimaryKey(comment);
    }

    public void add(LitemallComment comment) {
        commentMapper.insertSelective(comment);
    }

    public LitemallComment findById(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }
    
    /**
     * 查询comment表 typeid为2表示知识
     * @param valueid
     * @param typeid
     * @param offset
     * @param limit
     * @return
     */
    public List<LitemallComment> queryValueIdTypeId(Integer valueid,byte typeid,int offset, int limit) {
//        LitemallCommentExample example = new LitemallCommentExample();
//        example.setOrderByClause(LitemallComment.Column.addTime.desc());
//        example.or().andValueIdEqualTo(valueid).andTypeIdEqualTo(typeid).andDeletedEqualTo(false);
    	LitemallComment litemallComment=new LitemallComment();
    	litemallComment.setValueId(valueid);
    	litemallComment.setTypeId(typeid);
    	PageHelper.startPage(offset, limit);
        return commentMapper.selectByIdTypeId(litemallComment);
    }

    public int countValueIdTypeId(Integer valueid,byte typeid ) {
//        LitemallCommentExample example = new LitemallCommentExample();
//        example.or().andValueIdEqualTo(valueid).andTypeIdEqualTo(typeid).andTypeIdEqualTo(typeid).andDeletedEqualTo(false);
    	LitemallComment litemallComment=new LitemallComment();
    	litemallComment.setValueId(valueid);
    	litemallComment.setTypeId(typeid);
    	return (int)commentMapper.selectByTypeCount(litemallComment);
    }
    
    public int updateLikenumById(Integer id,Integer one){
    	return commentMapper.updateLikenumById(id, one);
    }
    
    public List<LitemallComment> selectBycommentId(Integer commentId){
    	return commentMapper.selectBycommentId(commentId);
    }
}
