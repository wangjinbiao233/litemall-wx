package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallExpress;
import org.linlinjava.litemall.db.service.LitemallExpressService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/express")
public class ExpressController {
    
    @Autowired
    private LitemallExpressService litemallExpressService;

    @GetMapping("/list")
    public Object list(){    	
    	List<LitemallExpress> litemallExpressList = litemallExpressService.query(null);
    	Map<String, Object> data = new HashMap<>();    	
    	data.put("items", litemallExpressList);    	
    	return ResponseUtil.ok(data);
    }
    

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallExpress litemallExpress){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        litemallExpressService.add(litemallExpress);
        return ResponseUtil.ok(litemallExpress);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallExpress litemallExpress){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        Integer expressId = litemallExpress.getId();
        if(expressId == null){
            return ResponseUtil.badArgument();
        }

        LitemallExpress express = litemallExpressService.findById(expressId);
        if(express == null){
            return ResponseUtil.badArgumentValue();
        }

        LitemallExpress newExpress = new LitemallExpress();        
        newExpress.setId(litemallExpress.getId());
        newExpress.setExpressSn(litemallExpress.getExpressSn());
        newExpress.setExpressName(litemallExpress.getExpressName());
        litemallExpressService.updateById(newExpress);

        express = litemallExpressService.findById(expressId);
        return ResponseUtil.ok(express);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallExpress litemallExpress){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        litemallExpressService.deleteById(litemallExpress.getId());
        return ResponseUtil.ok();
    }
    
   
    
    

}
