package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/home")
public class WxHomeController {
    private final Log logger = LogFactory.getLog(WxHomeController.class);

    @Autowired
    private LitemallAdService adService;
    @Autowired
    private LitemallGoodsService goodsService;
    @Autowired
    private LitemallBrandService brandService;
    @Autowired
    private LitemallTopicService topicService;
    @Autowired
    private LitemallCategoryService categoryService;
    @Autowired
    private LitemallCartService cartService;
    @Autowired
    private LitemallStoreService storeService;
    @Autowired
    private LitemallKnowledgeService knowledgeService;
    /**
     * app首页
     */
    @RequestMapping("/index")
    public Object index() {
        Map<String, Object> data = new HashMap<>();
        
        LitemallStore store = new LitemallStore();

        List<LitemallAd> banner = adService.queryByApid(1);
        data.put("banner", banner);

        List<LitemallCategory> channel = categoryService.queryChannel();
        data.put("channel", channel);

        List<LitemallGoods> newGoods = goodsService.queryByNew(0, 4);
        data.put("newGoodsList", newGoods);

        List<LitemallGoods> hotGoods = goodsService.queryByHot(0, 3);
        data.put("hotGoodsList", hotGoods);

        List<LitemallBrand> brandList = brandService.query(0,4);
        data.put("brandList", brandList);

        List<LitemallTopic> topicList = topicService.queryList(0, 3);
        data.put("topicList", topicList);
        
        List<LitemallStore> storeList = storeService.wxqueryStore(store);
        data.put("storeList", storeList);
        
        LitemallKnowledge konwledge = new LitemallKnowledge();
        konwledge.setIsShow(true);
        List<LitemallKnowledge> knowledgeList = knowledgeService.selectKnowledgeList(konwledge, 1, 3, null, null);
        data.put("knowledgeList", knowledgeList);
        
        List<LitemallKCategory> kCategoryList = knowledgeService.getKCategory();
        data.put("kCategoryList", kCategoryList);
        
        List<Map> categoryList = new ArrayList<>();
        List<LitemallCategory> catL1List = categoryService.queryL1WithoutRecommend(0, 6);
        for (LitemallCategory catL1 : catL1List) {
            List<LitemallCategory> catL2List = categoryService.queryByPid(catL1.getId());
            List<Integer> l2List = new ArrayList<>();
            for (LitemallCategory catL2 : catL2List) {
                l2List.add(catL2.getId());
            }

            List<LitemallGoods> categoryGoods = goodsService.queryByCategory(l2List, 0, 5);
            Map catGoods = new HashMap();
            catGoods.put("id", catL1.getId());
            catGoods.put("name", catL1.getName());
            catGoods.put("goodsList", categoryGoods);
            categoryList.add(catGoods);
        }
        data.put("floorGoodsList", categoryList);

        return ResponseUtil.ok(data);
    }
}