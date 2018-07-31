package org.linlinjava.litemall.db.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallOrderMapper;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderExample;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class LitemallOrderService {
    @Resource
    private LitemallOrderMapper orderMapper;

    public int add(LitemallOrder order) {
        return orderMapper.insertSelective(order);
    }

    public List<LitemallOrder> query(LitemallOrder order) {      
        return orderMapper.selectOrderList(order);
    }

    public int count(Integer userId) {
        LitemallOrderExample example = new LitemallOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return (int)orderMapper.countByExample(example);
    }

    public LitemallOrder findById(Integer orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public LitemallOrder queryByOrderSn(Integer userId, String orderSn){
        LitemallOrderExample example = new LitemallOrderExample();
        example.or().andUserIdEqualTo(userId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return orderMapper.selectOneByExample(example);
    }

    public int countByOrderSn(Integer userId, String orderSn){
        LitemallOrderExample example = new LitemallOrderExample();
        example.or().andUserIdEqualTo(userId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return (int)orderMapper.countByExample(example);
    }

    public String generateOrderSn(Integer userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while(countByOrderSn(userId, orderSn) != 0){
            orderSn = getRandomNum(6);
        }
        return orderSn;
    }

    public List<LitemallOrder> queryByOrderStatus(Integer userId, List<Short> orderStatus) {
        LitemallOrderExample example = new LitemallOrderExample();
        example.orderBy(LitemallOrder.Column.addTime.desc());
        LitemallOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if(orderStatus != null) {
            criteria.andOrderStatusIn(orderStatus);
        }
        //criteria.andDeletedEqualTo(false);
        return orderMapper.selectByExample(example);
    }
    
    public List<LitemallOrder> queryByUserId(Integer userId) {
    	LitemallOrderExample example = new LitemallOrderExample();
    	example.orderBy(LitemallOrder.Column.addTime.desc());
    	LitemallOrderExample.Criteria criteria = example.or();
    	criteria.andUserIdEqualTo(userId);
    	//criteria.andDeletedEqualTo(false);
    	return orderMapper.selectByExample(example);
    }

    public int countByOrderStatus(Integer userId, List<Short> orderStatus) {
        LitemallOrderExample example = new LitemallOrderExample();
        LitemallOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if(orderStatus != null) {
            criteria.andOrderStatusIn(orderStatus);
        }
        criteria.andDeletedEqualTo(false);
        return (int)orderMapper.countByExample(example);
    }

    public int update(LitemallOrder order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    public List<LitemallOrder> querySelective(LitemallOrder litemallOrder, Integer page, Integer size, String sort, String order) {
        PageHelper.startPage(page, size);
        return orderMapper.selectOrderList(litemallOrder);
    }

    public int countSelective(LitemallOrder litemallOrder, Integer page, Integer size, String sort, String order) {
        return orderMapper.countOrderList(litemallOrder);
    }

    public void updateById(LitemallOrder order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

    public void deleteById(Integer id) {
        LitemallOrder order = orderMapper.selectByPrimaryKey(id);
        if(order == null){
            return;
        }
        order.setDeleted(true);
        orderMapper.updateByPrimaryKey(order);
    }

    public int count() {
        LitemallOrderExample example = new LitemallOrderExample();
        example.or().andDeletedEqualTo(false);
        return (int)orderMapper.countByExample(example);
    }
    
    
    //校验快递单号重复
    public int countByShipSn(LitemallOrder order){
        return (int)orderMapper.countByShipSn(order);
    }

    /**
     * 根据优惠券查询该优惠券使用的订单详情
     * @param litemallOrder
     * @return
     */
	public LitemallOrder findDiscountOrderDetails(LitemallOrder litemallOrder) {
		List<LitemallOrder> list = orderMapper.selectOrderList(litemallOrder);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
