package org.linlinjava.litemall.wx.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.linlinjava.litemall.db.domain.LitemallIssue;
import org.linlinjava.litemall.db.service.LitemallIssueService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/issue")
public class WxIssueController {
	
    @Autowired
    private LitemallIssueService goodsIssueService;
    
    /**
     * 帮助中心 
     * @return
     */
    @RequestMapping("helpIssue")
    public Object helpIssue(){
    	// 商品问题，这里是一些通用问题
        List<LitemallIssue> issue = goodsIssueService.query(1);
        
        Map<String, Object> data = new HashMap<>();
        data.put("issue", issue);
        return ResponseUtil.ok(data);
    }

}
