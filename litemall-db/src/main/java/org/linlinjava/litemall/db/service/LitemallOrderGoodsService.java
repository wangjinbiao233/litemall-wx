package org.linlinjava.litemall.db.service;

import java.util.List;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallOrderGoodsMapper;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallOrderGoodsExample;
import org.linlinjava.litemall.db.domain.LitemallReserve;
import org.linlinjava.litemall.db.domain.LitemallTime;
import org.springframework.stereotype.Service;

@Service
public class LitemallOrderGoodsService {
    @Resource
    private LitemallOrderGoodsMapper orderGoodsMapper;

    public int add(LitemallOrderGoods orderGoods) {
        return orderGoodsMapper.insertSelective(orderGoods);
    }

    public List<LitemallOrderGoods> queryByOid(Integer orderId) {
        LitemallOrderGoods litemallOrderGoods = new LitemallOrderGoods();        
        litemallOrderGoods.setOrderId(orderId);
        return orderGoodsMapper.selectOrderGoodsList(litemallOrderGoods);
    }
    
    public List<LitemallOrderGoods> queryByOidAndStatus(Integer orderId, List<Short> orderStatus) {
        LitemallOrderGoods litemallOrderGoods = new LitemallOrderGoods();        
        litemallOrderGoods.setOrderId(orderId);
        litemallOrderGoods.setOrderStatusList(orderStatus);
        return orderGoodsMapper.selectOrderGoodsList(litemallOrderGoods);
    }
    
    
    public List<LitemallOrderGoods> findByOidAndGid(Integer orderId, Integer goodsId) {
        LitemallOrderGoodsExample example = new LitemallOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }
    
    public LitemallOrderGoods queryById(Integer orderGoodsId) {
        return orderGoodsMapper.selectByPrimaryKey(orderGoodsId);
    }
    
    public int update(LitemallOrderGoods orderGoods) {
        return orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);
    }

	public LitemallTime selectTimeData(String time, String storeId, String currentTime) {
	
		return orderGoodsMapper.selectTimeData(time,storeId,currentTime);
	}

	public String selectMaxServicePeo(String storeId) {
	
		return  orderGoodsMapper.selectMaxServicePeo(storeId);
	}

	public void reserveDetail(LitemallReserve litemallReserve) {
		
		  orderGoodsMapper.reserveDetail(litemallReserve);
	}

	public void updateOrderGoods(LitemallReserve litemallReserve) {
		
		orderGoodsMapper.updateOrderGoods(litemallReserve);
	}


   

}
