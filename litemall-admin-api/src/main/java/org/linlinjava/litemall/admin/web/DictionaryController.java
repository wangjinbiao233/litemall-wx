package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallDictionary;
import org.linlinjava.litemall.db.domain.LitemallKCategory;
import org.linlinjava.litemall.db.domain.LitemallKnowledge;
import org.linlinjava.litemall.db.service.LitemallDictionaryService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典
 * @author lhf
 *
 */
@RestController
@RequestMapping("/admin/dictionary")
public class DictionaryController {
	
	private final Log logger = LogFactory.getLog(DictionaryController.class);
	
	@Autowired
	private LitemallDictionaryService litemallDictionaryService;
	
	/**
	 * 根据字典code查询所属字典列表
	 * @param litemallDictionary
	 * @return
	 */
	@RequestMapping(value = "/selectDictionaryTypeList", method = RequestMethod.POST)  
	public Object selectDictionaryTypeList(LitemallDictionary litemallDictionary) {
		List<LitemallDictionary> list = litemallDictionaryService.selectDictionaryList(litemallDictionary);
		return ResponseUtil.ok(list);
	}
	
	/**
	 * 字典列表查询
	 * @param adminId
	 * @param dicObj
	 * @param page
	 * @param limit
	 * @param sort
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST) 
    public Object list(@LoginAdmin Integer adminId,
    					LitemallDictionary dicObj, 
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        
        List<LitemallDictionary> list=litemallDictionaryService.selectDicList(dicObj, page, limit, sort, order);
        int total = litemallDictionaryService.countDictionary(dicObj);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", list);
        return ResponseUtil.ok(data);
    }
	
	@PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallDictionary dicObj){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        litemallDictionaryService.insert(dicObj);
        return ResponseUtil.ok(dicObj);
    }
	
    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallDictionary dicObj){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        litemallDictionaryService.updateById(dicObj);
        return ResponseUtil.ok(dicObj);
    }
    
	@PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallDictionary dicObj){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        litemallDictionaryService.deleteById(dicObj.getId());
        return ResponseUtil.ok();
    }
	
	@GetMapping("/getDictionaryGoupList")
	public Object getDictionaryGoupList() {
		Map<String, Object> data = new HashMap<>();
		List<LitemallDictionary> list = litemallDictionaryService.getDicGroupList();
		Map<Integer,String> map = new HashMap<>();
		
		data.put("items", list);		
		return ResponseUtil.ok(data);
	}

}
