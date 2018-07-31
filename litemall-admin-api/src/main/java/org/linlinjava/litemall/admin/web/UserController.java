package org.linlinjava.litemall.admin.web;

import com.github.pagehelper.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallReserve;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallDiscountService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class UserController {
    private final Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallDiscountService discountService;
    
    @Autowired
    private LitemallOrderService orderService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
                       String username, String mobile,String isBuys,String userLevel,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        List<LitemallUser> userList = userService.querySelective(username, mobile,isBuys,userLevel, page, limit, sort, order);
        int total = userService.countSeletive(username, mobile,isBuys,userLevel, page, limit, sort, order);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", userList);

        return ResponseUtil.ok(data);
    }
    
    @RequestMapping("detail")
    public Object detail(@RequestParam(value = "id")Integer id) {
    	if(id == null){
            return ResponseUtil.fail402();
        }
    	LitemallUser user = userService.findById(id);
    	 Map<String, Object> data = new HashMap<>();
         data.put("items", user);

         return ResponseUtil.ok(data);
    }

    @GetMapping("/username")
    public Object username(String username){
        if(StringUtil.isEmpty(username)){
            return ResponseUtil.fail402();
        }

        int total = userService.countSeletive(username, null,null,null, null, null, null, null);
        if(total == 0){
            return ResponseUtil.ok("不存在");
        }
        return ResponseUtil.ok("已存在");
    }


    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallUser user){
        logger.debug(user);

        userService.add(user);
        return ResponseUtil.ok(user);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallUser user){
        logger.debug(user);

        userService.update(user);
        return ResponseUtil.ok(user);
    }
    
    @PostMapping("/findUserOrder")
    public Object findUserOrder(@RequestBody LitemallUser user,
    		@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
    	Map<String, Object> data = new HashMap<>();
    	if(user == null) {
    		return ResponseUtil.badArgument();
    	} else {
    		Integer userId = user.getId();
    		if(userId != null) {
    			List<LitemallOrder> orderList  = userService.queryLitemallOrderByUserId(userId, page, limit);
    			data.put("items", orderList);
    			data.put("total", orderList.size());
    		} else {
    			return ResponseUtil.badArgument();
    		}
    	}
    	return ResponseUtil.ok(data);
    }
    
    @PostMapping("/findUserReserve")
    public Object findUserReserve(@RequestBody LitemallUser user,
    		@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
    	Map<String, Object> data = new HashMap<>();
    	if(user == null) {
    		return ResponseUtil.badArgument();
    	} else {
    		Integer userId = user.getId();
    		if(userId != null) {
    			List<LitemallReserve> reserveList  = userService.queryLitemallReserveByUserId(userId, page, limit);
    			data.put("items", reserveList);
    			data.put("total", reserveList.size());
    		} else {
    			return ResponseUtil.badArgument();
    		}
    	}
    	return ResponseUtil.ok(data);
    }
    
    /**
     * 查询我的优惠卷
     * @return
     */
    @PostMapping("/findUserDiscount")
    public Object findUserDiscount(@RequestBody LitemallUser user,
    		@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
    	Map<String, Object> data = new HashMap<>();
    	if(user == null) {
    		return ResponseUtil.badArgument();
    	} else {
    		Integer userId = user.getId();
    		if(userId != null) {
    			data = discountService.selectMyCoupot(userId.toString(), null, page, limit);
    		} else {
    			return ResponseUtil.badArgument();
    		}
    	}
    	
    	
    	return ResponseUtil.ok(data);
    }
    
    
    @RequestMapping("getDiscountOrderDetail")
    public Object getDiscountOrderDetail(LitemallOrder litemallOrder) {
    	System.out.println("-------------couponid ==="+litemallOrder.getCouponId());
    	LitemallOrder orders = orderService.findDiscountOrderDetails(litemallOrder);
    	if(orders!=null) {
    		orders.setOrderStatusDisp(OrderUtil.orderStatusText(orders));
    	}
    	Map<String, Object> data = new HashMap<>();
        data.put("items", orders);
        return ResponseUtil.ok(data);
    }
    
}
