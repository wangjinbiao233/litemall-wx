package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallKCategory;
import org.linlinjava.litemall.db.service.LitemallKCategoryService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/kCategory")
public class KCategoryController {
    private final Log logger = LogFactory.getLog(KCategoryController.class);

    @Autowired
    private LitemallKCategoryService kCategoryService;

    @RequestMapping(value = "/list", method = RequestMethod.POST) 
    public Object list(@LoginAdmin Integer adminId,
                       LitemallKCategory litemallKCategory,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        List<LitemallKCategory> collectList = kCategoryService.querySelective(litemallKCategory, page, limit, sort, order);
        int total = kCategoryService.countSelective(litemallKCategory);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", collectList);

        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallKCategory kCategory){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        kCategory.setCreateBy(adminId+"");
        kCategoryService.add(kCategory);
        return ResponseUtil.ok(kCategory);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallKCategory kCategory){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        kCategoryService.updateById(kCategory);
        return ResponseUtil.ok(kCategory);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallKCategory kCategory){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        kCategoryService.deleteById(kCategory.getId());
        return ResponseUtil.ok();
    }

}
