package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallCollect;
import org.linlinjava.litemall.db.service.LitemallCollectService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/collect")
public class CollectController {
    private final Log logger = LogFactory.getLog(CollectController.class);

    @Autowired
    private LitemallCollectService collectService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
    		LitemallCollect collect,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        
        Map<String, Object> data = collectService.querySelective(collect, page, limit, sort, order);

        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallCollect collect){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.unsupport();
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        if(id == null){
            return ResponseUtil.badArgument();
        }

        LitemallCollect collect = collectService.findById(id);
        return ResponseUtil.ok(collect);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallCollect collect){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        collectService.updateById(collect);
        return ResponseUtil.ok();
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallCollect collect){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        collectService.deleteById(collect.getId());
        return ResponseUtil.ok();
    }

}
