package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallCart;
import org.linlinjava.litemall.db.service.LitemallCartService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallProductService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/cart")
public class CartController {
    private final Log logger = LogFactory.getLog(CartController.class);

    @Autowired
    private LitemallCartService cartService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallGoodsService goodsService;
    @Autowired
    private LitemallProductService productService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
    		LitemallCart cart,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
       
        Map<String, Object> data = cartService.querySelective(cart, page, limit, sort, order);
        return ResponseUtil.ok(data);
    }

    /*
     * 目前的逻辑不支持管理员创建
     */
    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallCart cart){
        if(adminId == null){
            return ResponseUtil.fail401();
        }

        return ResponseUtil.fail501();
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.fail401();
        }

        LitemallCart cart = cartService.findById(id);
        return ResponseUtil.ok(cart);
    }

    /*
     * 目前的逻辑不支持管理员创建
     */
    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallCart cart){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        return ResponseUtil.fail501();
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallCart cart){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        cartService.deleteById(cart.getId());
        return ResponseUtil.ok();
    }

}
