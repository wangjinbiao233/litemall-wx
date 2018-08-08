package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.service.WmwImgMedicalAnalyseService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/analyse")
public class ImgMedicalAnalyseController {

    @Autowired
    private WmwImgMedicalAnalyseService wmwImgMedicalAnalyseService;
    
    @RequestMapping(value = "/analyseList", method = RequestMethod.POST)  
    public Object analyseList(@LoginAdmin Integer adminId,  @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,String sort, String order){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
                
        Map<String,Object> map = new HashMap<String,Object>();
        List<Map<String,Object>> imgMedicalAanalyseList = wmwImgMedicalAnalyseService.selectWmwImgMedicalAnalyList(map, page, limit, sort, order);        
        int total = wmwImgMedicalAnalyseService.countWmwImgMedicalAnalyList(map, page, limit, sort, order);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", imgMedicalAanalyseList);
        return ResponseUtil.ok(data);
    }    
    
    @RequestMapping(value = "/analyseDetailList", method = RequestMethod.POST)  
    public Object analyseDetailList(@LoginAdmin Integer adminId,  @RequestParam(value = "page", defaultValue = "1") Integer page,
    		@RequestParam(value = "limit", defaultValue = "10") Integer limit,String sort, String order,String userId){
    	if(adminId == null){
    		return ResponseUtil.fail401();
    	}
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("userId", userId);
    	List<Map<String,Object>> imgMedicalAanalyseList = wmwImgMedicalAnalyseService.selectWmwImgMedicalAnalyDetailList(map, page, limit, sort, order);        
    	int total = wmwImgMedicalAnalyseService.countWmwImgMedicalAnalyDetailList(map, page, limit, sort, order);
    	
    	Map<String, Object> data = new HashMap<>();
    	data.put("total", total);
    	data.put("items", imgMedicalAanalyseList);
    	return ResponseUtil.ok(data);
    }    
    
    

}
