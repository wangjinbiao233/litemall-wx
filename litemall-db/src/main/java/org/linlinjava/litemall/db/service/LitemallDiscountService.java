package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.LitemallDiscountCustomMapper;
import org.linlinjava.litemall.db.dao.LitemallDiscountMapper;
import org.linlinjava.litemall.db.domain.LitemallCustomDiscount;
import org.linlinjava.litemall.db.domain.LitemallDiscount;
import org.linlinjava.litemall.db.domain.LitemallDiscountExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallDiscountService {

    @Autowired
    LitemallDiscountMapper litemallDiscountMapper;


    @Autowired
    LitemallDiscountCustomMapper litemallDiscountCustomMapper;


    /**
     * 根据优惠卷名称聚合查询优惠卷
     *
     * @param discountName 优惠卷名称
     * @return
     */
    public Map<String,Object> selectByDiscountNameGroypBy(LitemallCustomDiscount discount, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<LitemallCustomDiscount> list = litemallDiscountCustomMapper.selectByDiscountNameGroypBy(discount);
        PageInfo pageinfo = new PageInfo(list);
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("items", list);
        resMap.put("pageNum", pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());


        return resMap;
    }

    /**
     * 添加优惠卷
     *
     * @param record
     * @return
     */
    public int insertSelective(LitemallDiscount record){
        return litemallDiscountMapper.insertSelective(record);
    }


    /**
     * 根据优惠卷名称和key聚合查询优惠卷
     *
     * @param key
     * @param discountName 优惠卷名称
     * @param page
     * @param size
     * @return
     */
    public Map<String,Object> selectByKeyAndNameGroypBy(String key,String discountName, String userId,Integer page, Integer size){

        PageHelper.startPage(page, size);
        List<LitemallCustomDiscount> list = litemallDiscountCustomMapper.selectByKeyAndNameGroypBy(key,discountName,userId);
        PageInfo pageinfo = new PageInfo(list);
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("items", list);
        resMap.put("pageNum", pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());


        return resMap;
    }


    /**
     *领取优惠卷
     *
     * @param key
     * @param userId
     * @return
     */
    public int getCoupon(String key,Integer userId){
        List<LitemallCustomDiscount> list = litemallDiscountCustomMapper.selectUnusedByKey(key);

        if (list.size() > 0){

            LitemallDiscount litemallDiscount = new LitemallDiscount();
            litemallDiscount.setId(list.get(0).getId());
            litemallDiscount.setIsGet(1);
            litemallDiscount.setUserId(userId);
            litemallDiscount.setModifyTime(LocalDateTime.now());
            litemallDiscount.setModifyUserId(userId+"");

            int r = litemallDiscountMapper.updateByPrimaryKeySelective(litemallDiscount);

            return r;

        }

        return 0;
    }

    /**
     * 使用优惠卷
     *
     * @param couponId 优惠卷id
     * @param orderSn
     * @param userId
     * @return
     */
    public int useCoupon(Integer couponId,String orderSn,Integer userId){

        LitemallDiscount litemallDiscount = new LitemallDiscount();
        litemallDiscount.setId(couponId);
        litemallDiscount.setOrderSn(orderSn);
        litemallDiscount.setIsUse(1);
        litemallDiscount.setModifyTime(LocalDateTime.now());
        litemallDiscount.setModifyUserId(userId+"");

        int r = litemallDiscountMapper.updateByPrimaryKeySelective(litemallDiscount);

        return r;

    }

    /**
     * 查询我的优惠卷
     *
     * @param userId
     * @param discountName
     * @param page
     * @param size
     * @return
     */
    public Map<String, Object> selectMyCoupot(String userId,String discountName, Integer page, Integer size){

        PageHelper.startPage(page, size);
        List<LitemallCustomDiscount> list = litemallDiscountCustomMapper.selectByUserIdAndName(userId,discountName);
        PageInfo pageinfo = new PageInfo(list);
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("items", list);
        resMap.put("pageNum", pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());

        return resMap;
    }
    
    /**
     * 查询我的可用优惠卷
     *
     * @param userId
     * @param discountName
     * @param page
     * @param size
     * @return
     */
    public Map<String, Object> selectMyUseCoupot(Integer userId,String goodsTotalPrice,String discountName, Integer page, Integer size){
    	
    	PageHelper.startPage(page, size);
    	LitemallCustomDiscount litemallCustomDiscount  = new LitemallCustomDiscount();
    	litemallCustomDiscount.setUserId(userId);
    	litemallCustomDiscount.setGoodsTotalPrice(new BigDecimal(goodsTotalPrice));
    	litemallCustomDiscount.setDiscountName(discountName);
    	List<LitemallCustomDiscount> list = litemallDiscountCustomMapper.selectMyUseByUserIdAndName(litemallCustomDiscount);
    	PageInfo pageinfo = new PageInfo(list);
    	Map<String, Object> resMap = new HashMap<String, Object>();
    	resMap.put("items", list);
    	resMap.put("pageNum", pageinfo.getPageNum());
    	resMap.put("pageSize", pageinfo.getPageSize());
    	resMap.put("startRow", pageinfo.getStartRow());
    	resMap.put("endRow", pageinfo.getEndRow());
    	resMap.put("total", pageinfo.getTotal());
    	resMap.put("pages", pageinfo.getPages());
    	
    	return resMap;
    }
    
    /**
     * 查询我的可用优惠卷
     *
     * @param userId
     * @param discountName
     * @param page
     * @param size
     * @return
     */
    public List<LitemallCustomDiscount> selectMyUseCoupotList(Integer userId,BigDecimal goodsTotalPrice){
    	LitemallCustomDiscount litemallCustomDiscount  = new LitemallCustomDiscount();
    	litemallCustomDiscount.setUserId(userId);
    	litemallCustomDiscount.setGoodsTotalPrice(goodsTotalPrice);
    	List<LitemallCustomDiscount> list = litemallDiscountCustomMapper.selectMyUseByUserId(litemallCustomDiscount);
    	return list;
    }
  


    /**
     * 根据key和userid查询优惠卷
     *
     * @param key
     * @param userId
     * @return
     */
    public List<LitemallDiscount> selectByKeyAndUserId(String key, Integer userId){
        LitemallDiscountExample example = new LitemallDiscountExample();
        LitemallDiscountExample.Criteria criteria =  example.createCriteria();
        criteria.andKeyEqualTo(key);
        criteria.andUserIdEqualTo(userId);
        List<LitemallDiscount> list = litemallDiscountMapper.selectByExample(example);
        return list;
    }
    

    /**
     * 根据key和userid查询优惠卷
     *
     * @param key
     * @param userId
     * @return
     */
    public LitemallDiscount selectById(Integer id){
        LitemallDiscount  litemallDiscount= litemallDiscountMapper.selectByPrimaryKey(id);
        return litemallDiscount;
    }
    
    
    /**
     * 根据key和userid查询优惠卷
     *
     * @param key
     * @param userId
     * @return
     */
    public void updateById(LitemallDiscount litemallDiscount){
    	litemallDiscountMapper.updateByPrimaryKey(litemallDiscount);
    }

	public int updateSelective(LitemallCustomDiscount discount) {
		return litemallDiscountMapper.updateSelective(discount);
	}



}
