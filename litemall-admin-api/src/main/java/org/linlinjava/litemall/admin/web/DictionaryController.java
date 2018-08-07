package org.linlinjava.litemall.admin.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallDictionary;
import org.linlinjava.litemall.db.service.LitemallDictionaryService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

}
