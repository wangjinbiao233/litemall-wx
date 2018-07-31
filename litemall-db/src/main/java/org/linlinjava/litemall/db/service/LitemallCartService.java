package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.linlinjava.litemall.db.dao.LitemallCartMapper;
import org.linlinjava.litemall.db.domain.LitemallCart;
import org.linlinjava.litemall.db.domain.LitemallCartExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallCartService {
    @Resource
    private LitemallCartMapper cartMapper;

    public LitemallCart queryExist(Integer goodsId, Integer productId, Integer userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andProductIdEqualTo(productId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectOneByExample(example);
    }

    public void add(LitemallCart cart) {
        cartMapper.insertSelective(cart);
    }

    public void update(LitemallCart cart) {
        cartMapper.updateByPrimaryKey(cart);
    }

    public List<LitemallCart> queryByUid(int userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId);
        return cartMapper.selectListByExample(example);
    }

    public List<LitemallCart> queryByUidAndChecked(Integer userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }

    public List<LitemallCart> queryByUidAndSid(int userId, String sessionId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }

    public int delete(List<Integer> productIdList, int userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(productIdList);
        LitemallCart cart = new LitemallCart();
        cart.setDeleted(true);
        return cartMapper.updateByExampleSelective(cart, example);
    }

    public LitemallCart findById(Integer id) {
        return cartMapper.selectByPrimaryKey(id);
    }

    public int updateCheck(Integer userId, List<Integer> idsList, Boolean checked) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(idsList).andDeletedEqualTo(false);
        LitemallCart cart = new LitemallCart();
        cart.setChecked(checked);
        return cartMapper.updateByExampleSelective(cart, example);
    }

    public void clearGoods(Integer userId) {
        LitemallCartExample example = new LitemallCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true);
        LitemallCart cart = new LitemallCart();
        cart.setDeleted(true);
        cartMapper.updateByExampleSelective(cart, example);
    }

    public List<LitemallCart> querySelective(Integer userId, Integer goodsId, Integer page, Integer limit, String sort, String order) {
        LitemallCartExample example = new LitemallCartExample();
        LitemallCartExample.Criteria criteria = example.createCriteria();

        if(userId != null){
            criteria.andUserIdEqualTo(userId);
        }
        if(goodsId != null){
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);

        PageHelper.startPage(page, limit);
        return cartMapper.selectByExample(example);
    }

    public int countSelective(Integer userId, Integer goodsId, Integer page, Integer limit, String sort, String order) {
        LitemallCartExample example = new LitemallCartExample();
        LitemallCartExample.Criteria criteria = example.createCriteria();

        if(userId != null){
            criteria.andUserIdEqualTo(userId);
        }
        if(goodsId != null){
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);

        return (int)cartMapper.countByExample(example);
    }

    public void deleteById(Integer id) {
        LitemallCart cart = cartMapper.selectByPrimaryKey(id);
        if(cart == null){
            return;
        }
        cart.setDeleted(true);
        cartMapper.updateByPrimaryKey(cart);
    }

	public Map<String, Object> querySelective(LitemallCart cart, Integer page, Integer limit, String sort,
			String order) {
		Map<String, Object> data = new HashMap<>();
		PageHelper.startPage(page, limit);
    	List<LitemallCart> list = cartMapper.selectByCart(cart);
        PageInfo<LitemallCart> pageInfo = new PageInfo<>(list);
        data.put("total", pageInfo.getTotal());
        data.put("items", pageInfo.getList());
        return data;
	}
}
