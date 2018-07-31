package org.linlinjava.litemall.wx.web;


import org.linlinjava.litemall.db.domain.LitemallDiscount;
import org.linlinjava.litemall.db.service.LitemallDiscountService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/discount")
public class WxDiscountController {

    @Autowired
    private LitemallDiscountService litemallDiscountService;

    /**
     * 根据优惠卷名称和key聚合查询优惠卷
     *
     * @param key
     * @param discountName
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("selectByKeyAndNameGroypBy")
    public Object selectByKeyAndNameGroypBy(String key, String discountName, String userId,@RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Map<String, Object> map = litemallDiscountService.selectByKeyAndNameGroypBy(key, discountName, userId,page, size);

        return ResponseUtil.ok(map);
    }


    /**
     * 领取优惠卷
     *
     * @param key
     * @param userId
     * @return 1:成功，2:失败
     */
    @RequestMapping("getCoupon")
    public Object getCoupon(String key, Integer userId) {

        List<LitemallDiscount> list = litemallDiscountService.selectByKeyAndUserId(key,userId);

        if (list.size() > 0){
            return ResponseUtil.fail(-1,"领取失败，您已经领取过此优惠卷!");
        }

        int r = litemallDiscountService.getCoupon(key, userId);


        return ResponseUtil.ok(r);
    }

    /**
     * 使用优惠卷
     *
     * @param couponId
     * @param orderSn
     * @param isUse
     * @param userId
     * @return
     */
    @RequestMapping("useCoupon")
    public Object useCoupon(Integer couponId, String orderSn, Integer userId) {
        int r = litemallDiscountService.useCoupon(couponId, orderSn, userId);
        return ResponseUtil.ok(r);
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
    @RequestMapping("selectMyCoupot")
    public Object selectMyCoupot(String userId, String discountName, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> map = litemallDiscountService.selectMyCoupot(userId, discountName, page, size);
        return ResponseUtil.ok(map);
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
    @RequestMapping("selectMyUseCoupot")
    public Object selectMyUseCoupot(String userId, String discountName, String goodsTotalPrice,@RequestParam(value = "page", defaultValue = "1") Integer page,
    		@RequestParam(value = "size", defaultValue = "10") Integer size) {
    	Map<String, Object> map = litemallDiscountService.selectMyUseCoupot(Integer.parseInt(userId),goodsTotalPrice, discountName, page, size);
    	return ResponseUtil.ok(map);
    }
}
