package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;

import org.linlinjava.litemall.db.dao.LitemallOrderMapper;
import org.linlinjava.litemall.db.dao.LitemallReserveMapper;
import org.linlinjava.litemall.db.dao.LitemallUserMapper;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallReserve;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.LitemallUserExample;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallUserService {
    @Resource
    private LitemallUserMapper userMapper;
    @Resource
    private LitemallOrderMapper orderMapper;
    @Resource
    private LitemallReserveMapper litemallReserveMapper;

    public LitemallUser findById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public LitemallUser queryByOid(String openId) {
        LitemallUserExample example = new LitemallUserExample();
        example.or().andWeixinOpenidEqualTo(openId).andDeletedEqualTo(false);
        return userMapper.selectOneByExample(example);
    }

    public void add(LitemallUser user) {
    	Integer count = userMapper.selectByOpenId(user.getWeixinOpenid());
    	if(count == 0) {
    		userMapper.insertSelective(user);
    	}
    }

    public void update(LitemallUser user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    public List<LitemallUser> querySelective(String username, String mobile, String isBuys, String userLevel, Integer page, Integer size, String sort, String order) {
        LitemallUserExample example = new LitemallUserExample();
        LitemallUserExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(username)){
            criteria.andUsernameLike("%" + username + "%");
        }
        if(!StringUtils.isEmpty(mobile)){
            criteria.andMobileLike("%" +mobile+ "%");
        }
        if(!StringUtils.isEmpty(userLevel)){
            criteria.andUserLevelEqualTo(userLevel);
        }
        criteria.andDeletedEqualTo(false);
        if(!StringUtils.isEmpty(isBuys)) {
			example.setIsBuysParams(isBuys);
        }
        PageHelper.startPage(page, size);
        return userMapper.selectByExample(example);
    }

    public int countSeletive(String username, String mobile, String isBuys, String userLevel, Integer page, Integer size, String sort, String order) {
        LitemallUserExample example = new LitemallUserExample();
        LitemallUserExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(username)){
            criteria.andUsernameLike("%" + username + "%");
        }
        if(!StringUtils.isEmpty(mobile)){
        	criteria.andMobileLike("%" +mobile+ "%");
        }
        if(!StringUtils.isEmpty(userLevel)){
            criteria.andUserLevelEqualTo(userLevel);
        }
        criteria.andDeletedEqualTo(false);
        if(!StringUtils.isEmpty(isBuys)) {
			example.setIsBuysParams(isBuys);
        }

        return (int) userMapper.countByExample(example);
    }

    public int count() {
        LitemallUserExample example = new LitemallUserExample();
        example.or().andDeletedEqualTo(false);

        return (int)userMapper.countByExample(example);
    }

    public List<LitemallUser> queryByUsername(String username) {
        LitemallUserExample example = new LitemallUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public List<LitemallUser> queryByMobile(String mobile) {
        LitemallUserExample example = new LitemallUserExample();
        example.or().andMobileEqualTo(mobile).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        LitemallUser user = userMapper.selectByPrimaryKey(id);
        if(user == null){
            return;
        }
        user.setDeleted(true);
        userMapper.updateByPrimaryKey(user);
    }

	public List<LitemallUser> queryUserSelective(LitemallUser user, Integer page, Integer limit, String sort) {
	
		PageHelper.startPage(page, limit);
		return userMapper.selectUserByExample(user);
	}

	public int countUserSeletive(LitemallUser user, Integer page, Integer limit, String sort) {

		 PageHelper.startPage(page, limit);
	     return userMapper.selectUserCount(user);
	}

	public int findSubUserCount(Integer userId) {
		
		return userMapper.selectSubUserCount(userId);
	}

	public int findSubSubUserCount(Integer userId) {
		return userMapper.selectSubSubUserCount(userId);
	}

	/**
	 * 查询用户的所有订单记录
	 * @param userId
	 * @return
	 */
	public List<LitemallOrder> queryLitemallOrderByUserId(Integer userId, Integer page, Integer limit) {
		LitemallOrder litemallOrder = new LitemallOrder();
		litemallOrder.setUserId(userId);
		PageHelper.startPage(page, limit);
		List<LitemallOrder> orderList = orderMapper.selectOrderList(litemallOrder);
		for (LitemallOrder order : orderList) {
			order.setOrderStatusDisp(OrderUtil.orderStatusText(order));
		}
		return orderList;
	}

	/**
	 * 查询用户的所有预约记录
	 * @param userId
	 * @return
	 */
	public List<LitemallReserve> queryLitemallReserveByUserId(Integer userId, Integer page, Integer limit) {
		LitemallReserve litemallReserve = new LitemallReserve();
		litemallReserve.setUserId(userId);
		PageHelper.startPage(page, limit);
		List<LitemallReserve> reserveList = litemallReserveMapper.selectReserveList(litemallReserve);
		return reserveList;
	}

	/**
	 * 查询上级(我的推荐人)
	 * @param userId
	 * @return
	 */
	public List<LitemallUser> findParentUserById(Integer userId) {
		
		return userMapper.selectParentUserInfoById(userId);
	}

	
	public List<LitemallUser> queryDoctorSelective(LitemallUser user, Integer page, Integer limit, String sort) {
		
		PageHelper.startPage(page, limit);
		return userMapper.selectDoctorByExample(user);
	}

	public int countDoctorSeletive(LitemallUser user, Integer page, Integer limit, String sort) {

		 PageHelper.startPage(page, limit);
	     return userMapper.selectDoctorCount(user);
	}
	
}
