package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallFootprint;
import org.linlinjava.litemall.db.service.LitemallFootprintService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/footprint")
public class FootprintController {
    private final Log logger = LogFactory.getLog(FootprintController.class);

    @Autowired
    private LitemallFootprintService footprintService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
    		LitemallFootprint footprint,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        Map<String, Object> data = footprintService.querySelective(footprint, page, limit, sort, order);

        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallFootprint footprint){
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

        LitemallFootprint footprint = footprintService.findById(id);
        return ResponseUtil.ok(footprint);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallFootprint footprint){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        footprintService.updateById(footprint);
        return ResponseUtil.ok();
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallFootprint footprint){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        footprintService.deleteById(footprint.getId());
        return ResponseUtil.ok();
    }

}
