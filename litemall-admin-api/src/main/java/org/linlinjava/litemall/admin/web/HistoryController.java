package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallSearchHistory;
import org.linlinjava.litemall.db.service.LitemallSearchHistoryService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/history")
public class HistoryController {
    private final Log logger = LogFactory.getLog(HistoryController.class);

    @Autowired
    private LitemallSearchHistoryService searchHistoryService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
    		LitemallSearchHistory history,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        
        Map<String, Object> data = searchHistoryService.querySelective(history, page, limit, sort, order);
        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallSearchHistory history){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        return ResponseUtil.fail501();
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        if(id == null){
            return ResponseUtil.badArgument();
        }

        LitemallSearchHistory history = searchHistoryService.findById(id);
        return ResponseUtil.ok(history);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallSearchHistory history){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        searchHistoryService.updateById(history);
        return ResponseUtil.ok();
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallSearchHistory history){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        searchHistoryService.deleteById(history.getId());
        return ResponseUtil.ok();
    }

}
