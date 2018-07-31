package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallKCategory;
import org.linlinjava.litemall.db.domain.LitemallKnowledge;
import org.linlinjava.litemall.db.service.LitemallKnowledgeService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/knowledge")
public class KnowledgeController {
	private final Log logger = LogFactory.getLog(KnowledgeController.class);
	
	@Autowired
    private LitemallKnowledgeService knowledgeService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST) 
    public Object list(@LoginAdmin Integer adminId,
    					LitemallKnowledge knowledge, 
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        
        List<LitemallKnowledge> list=knowledgeService.selectKnowledgeList(knowledge, page, limit, sort, order);
        int total = knowledgeService.countKnowledge(knowledge);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", list);
        return ResponseUtil.ok(data);
    }
	
    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallKnowledge knowledge){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        knowledgeService.insert(knowledge);
        return ResponseUtil.ok(knowledge);
    }
	
    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallKnowledge knowledge){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        knowledgeService.updateById(knowledge);
        return ResponseUtil.ok(knowledge);
    }
    
	@PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallKnowledge knowledge){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        knowledgeService.deleteById(knowledge.getId());
        return ResponseUtil.ok();
    }
	
	@GetMapping("/getKCategory")
	public Object getKCategory() {
		Map<String, Object> data = new HashMap<>();
		List<LitemallKCategory> list = knowledgeService.getKCategory();
		Map<Integer,String> map = new HashMap<>();
		for (LitemallKCategory litemallKCategory : list) {
			map.put(litemallKCategory.getId(), litemallKCategory.getName());
		}
		data.put("kCategory", list);
		data.put("kCategoryMap", map);
		
		return ResponseUtil.ok(data);
	}
	
	
}
