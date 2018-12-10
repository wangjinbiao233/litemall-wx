package org.linlinjava.litemall.db.service;

import java.util.List;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallKCategoryMapper;
import org.linlinjava.litemall.db.dao.LitemallKnowledgeGoodsMapper;
import org.linlinjava.litemall.db.dao.LitemallKnowledgeMapper;
import org.linlinjava.litemall.db.domain.LitemallKCategory;
import org.linlinjava.litemall.db.domain.LitemallKnowledge;
import org.linlinjava.litemall.db.domain.LitemallKnowledgeGoods;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class LitemallKnowledgeService {
	 @Resource
	 private LitemallKnowledgeMapper knowledgeMapper;
	 @Resource
	 private LitemallKCategoryMapper knowledgeCategoryMapper;
	 @Resource
	 private LitemallKnowledgeGoodsMapper litemallKnowledgeGoodsMapper;

	public List<LitemallKnowledge> selectKnowledgeList(LitemallKnowledge knowledge, Integer page, Integer limit, String sort, String order) {
		PageHelper.startPage(page, limit);
		return knowledgeMapper.selectknowledgeList(knowledge);
	}

	public int countKnowledge(LitemallKnowledge knowledge) {
		return knowledgeMapper.countKnowledge(knowledge);
	}

	public int deleteById(Integer id) {
		return knowledgeMapper.deleteByPrimaryKey(id);
	}

	public int insert(LitemallKnowledge knowledge) {
		return knowledgeMapper.insertSelective(knowledge);
		
	}

	public int updateById(LitemallKnowledge knowledge) {
		return knowledgeMapper.updateByPrimaryKeySelective(knowledge);
	}

	public List<LitemallKCategory> getKCategory() {
		return knowledgeCategoryMapper.selectLitemallKCategory(null);
	}
	 
	 public int updateCommentCountById(Integer id,Integer one){
		 return knowledgeMapper.updateCommentCountById(id, one);
	 }
	 
	 public int updatePraiseCountById(Integer id,Integer one){
		 return knowledgeMapper.updatePraiseCountById(id, one);
	 }

	/**
	 * 知识关联商品
	 * @param lkg
	 */
	public void addLitemallKnowledgeGoods(LitemallKnowledgeGoods lkg) {
		litemallKnowledgeGoodsMapper.insertSelective(lkg);
	}

	/**
	 * 删除知识商品关联记录
	 * @param goodsId
	 * @param knowledgeId
	 */
    public void deleteLitemallKnowledgeGoods(String goodsId, Integer knowledgeId) {
		litemallKnowledgeGoodsMapper.deleteLitemallKnowledgeGoods(goodsId, knowledgeId);
    }
}
