package org.linlinjava.litemall.wx.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.linlinjava.litemall.db.service.LitemallStoreService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/store")
public class WxStoreController {
    private final Log logger = LogFactory.getLog(WxStoreController.class);

    @Autowired
    private LitemallStoreService storeService;
  
    /**
     * 获取门店列表
     */
  
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Object list(@RequestBody LitemallStore store) {
        if(store.getUserId() == null){
            return ResponseUtil.fail401();
        }
        //获取精选门店   设置门店类型
        // 门店名称 图片 具体地址  计算地点位置
        List<LitemallStore> storeList = storeService.wxqueryStore(store);
        //获取门店包含的服务
        List<LitemallGoods> goodsList = storeService.wxqueryGoods(store);
        
        Map<String, Object> data = new HashMap<>();
        data.put("store", storeList);
        data.put("goods", goodsList);
        return ResponseUtil.ok(data);
    }
    
    
    
    
    @GetMapping("/getGoodsStorelist")
    public Object getGoodsStorelist(LitemallStore store,Integer id) {
 
        
        Map<String, Object> data = new HashMap<>();
        
        List<LitemallStore> storeList = storeService.wxqueryGoodsStore(String.valueOf(id));
        
        data.put("stores", storeList);
       

        return ResponseUtil.ok(data);
    }
    
    //查询所有门店
    @GetMapping("/getAllStore")
    public Object getAllStore(LitemallStore store) {
 
        
        Map<String, Object> data = new HashMap<>();
        
        List<LitemallStore> storeList = storeService.wxqueryAllStore();
        
        data.put("stores", storeList);
       

        return ResponseUtil.ok(data);
    }
    
    
    


  
}