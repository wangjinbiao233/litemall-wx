package org.linlinjava.litemall.wx.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallBrand;
import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.domain.LitemallComment;
import org.linlinjava.litemall.db.domain.LitemallFootprint;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallGoodsAttribute;
import org.linlinjava.litemall.db.domain.LitemallIssue;
import org.linlinjava.litemall.db.domain.LitemallProduct;
import org.linlinjava.litemall.db.domain.LitemallSearchHistory;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallBrandService;
import org.linlinjava.litemall.db.service.LitemallCartService;
import org.linlinjava.litemall.db.service.LitemallCategoryService;
import org.linlinjava.litemall.db.service.LitemallCollectService;
import org.linlinjava.litemall.db.service.LitemallCommentService;
import org.linlinjava.litemall.db.service.LitemallCouponService;
import org.linlinjava.litemall.db.service.LitemallFootprintService;
import org.linlinjava.litemall.db.service.LitemallGoodsAttributeService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallGoodsSpecificationService;
import org.linlinjava.litemall.db.service.LitemallIssueService;
import org.linlinjava.litemall.db.service.LitemallProductService;
import org.linlinjava.litemall.db.service.LitemallSearchHistoryService;
import org.linlinjava.litemall.db.service.LitemallStoreService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.SortUtil;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.jdbc.StringUtils;

@RestController
@RequestMapping("/wx/goods")
public class WxGoodsController {
    private final Log logger = LogFactory.getLog(WxGoodsController.class);

    @Autowired
    private LitemallGoodsService goodsService;
    @Autowired
    private LitemallStoreService storeService; // add by pengxb @2018-05-21
    @Autowired
    private LitemallProductService productService;
    @Autowired
    private LitemallIssueService goodsIssueService;
    @Autowired
    private LitemallGoodsAttributeService goodsAttributeService;
    @Autowired
    private LitemallBrandService brandService;
    @Autowired
    private LitemallCommentService commentService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallCollectService collectService;
    @Autowired
    private LitemallFootprintService footprintService;
    @Autowired
    private LitemallCategoryService categoryService;
    @Autowired
    private LitemallSearchHistoryService searchHistoryService;
    @Autowired
    private LitemallCouponService apiCouponService;
    @Autowired
    private LitemallCartService cartService;
    @Autowired
    private LitemallGoodsSpecificationService goodsSpecificationService;

    /**
     * 商品详情页数据
     * 用户也是可选登录，如果登录了，则查询是否收藏，以及记录用户的足迹
     */
    @RequestMapping("detail")
    public Object detail(@LoginUser Integer userId, String id) {
        if(id == null){
            return ResponseUtil.fail402();
        }

        // 商品信息
        LitemallGoods info = goodsService.findByGoodsSn(id);

        // 商品属性
        List<LitemallGoodsAttribute> goodsAttributeList = goodsAttributeService.queryByGid(info.getId());

        // 商品规格
        // 返回的是定制的GoodsSpecificationVo
        Object specificationList = goodsSpecificationService.getSpecificationVoList(info.getId());

        // 商品规格对应的数量和价格
        List<LitemallProduct> productList = productService.queryByGid(info.getId());

        // 商品问题，这里是一些通用问题
        List<LitemallIssue> issue = goodsIssueService.query(0);

        // 商品品牌商    ----判断品牌是否存在
        LitemallBrand brand = null;
        
        if(null!=info && null!=info.getBrandId() && !("".equals(info.getBrandId()))   ) {
        	 brand = brandService.findById(info.getBrandId());
        }else {
        	
        }
        // 评论
        List<LitemallComment> comments = commentService.queryGoodsByGid(info.getId(), 0, 3);
        List<Map<String, Object>> commentsVo = new ArrayList<>(comments.size());
        int commentCount = commentService.countGoodsByGid(info.getId(), 0, 3);
        for(LitemallComment comment : comments){
            Map<String, Object> c = new HashMap<>();
            c.put("id", comment.getId());
            c.put("addTime", comment.getAddTime());
            c.put("content", comment.getContent());
            LitemallUser user = userService.findById(comment.getUserId());
            if(null !=user) {
                c.put("nickname", user.getNickname());
                c.put("avatar", user.getAvatar());
            }
            c.put("picList", comment.getPicUrls());
            commentsVo.add(c);
        }
        Map<String, Object> commentList = new HashMap<>();
        commentList.put("count", commentCount);
        commentList.put("data", commentsVo);

        // 用户收藏
        int userHasCollect = 0;
        if(userId != null) {
            userHasCollect = collectService.count(userId, info.getId());
        }

        // 记录用户的足迹
        if(userId != null) {
            LitemallFootprint footprint = new LitemallFootprint();
            footprint.setAddTime(LocalDateTime.now());
            footprint.setUserId(userId);
            footprint.setGoodsId(info.getId());
            footprintService.add(footprint);
        }

        // 商品门店列表 add by pengxb @2018-05-21
        List<LitemallStore> storeList = storeService.wxqueryGoodsStore(String.valueOf(id));

        Map<String, Object> data = new HashMap<>();
        data.put("info", info);
        data.put("userHasCollect", userHasCollect);
        data.put("issue", issue);
        data.put("comment", commentList);
        data.put("specificationList", specificationList);
        data.put("productList", productList);
        data.put("attribute", goodsAttributeList);
        data.put("brand", brand);
        // add by pengxb @2018-05-21
        data.put("stores", storeList);

        return ResponseUtil.ok(data);
    }

    /**
     * 获取分类下的商品
     */
    @RequestMapping("category")
    public Object category(Integer id) {
        /*if(id == null){
            return ResponseUtil.fail402();
        }
        LitemallCategory cur = categoryService.findById(id);
        LitemallCategory parent = null;
        List<LitemallCategory> children = null;

        if(cur.getParentId() == 0){
            parent = cur;
            children = categoryService.queryByPid(cur.getId());
            if(!children.isEmpty()) 
            	cur = children.get(0);
        }
        else{
            parent = categoryService.findById(cur.getParentId());
            children = categoryService.queryByPid(cur.getParentId());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("currentCategory", cur);
        data.put("parentCategory", parent);
        data.put("brotherCategory", children);*/
    	
    	//获取所有可用商品分类
    	List<LitemallCategory> categoryList = categoryService.queryCategoryValid();
    	//设置显示的商品分类
        LitemallCategory category = null;
        if(id != -1){
        	category = categoryService.findById(id);
        }
        else{
        	category = categoryList.get(0);
        }
        //根据商品分类获取分类下对应的商品
        

    	Map<String, Object> data = new HashMap<>();
    	data.put("category", category);
    	data.put("categoryList", categoryList);
        return ResponseUtil.ok(data);
    }

    /**
     * 　　获取商品列表
     * 1. 这里的前五个参数都是可选的，甚至都是空
     * 2. 用户也是可选登录，如果登录了，则记录用户的搜索关键字
     *
     */
    @RequestMapping("list")
    public Object list(Integer categoryId, Integer brandId, String keyword, Integer isNew, Integer isHot,
                       @LoginUser Integer userId,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       String sort, String order) {

        String sortWithOrder = SortUtil.goodsSort(sort, order);

        //添加到搜索历史
        if (userId != null && !StringUtils.isNullOrEmpty(keyword)) {
            LitemallSearchHistory searchHistoryVo = new LitemallSearchHistory();
            searchHistoryVo.setAddTime(LocalDateTime.now());
            searchHistoryVo.setKeyword(keyword);
            searchHistoryVo.setUserId(userId);
            searchHistoryVo.setFrom("wx");
            searchHistoryService.save(searchHistoryVo);
        }

        //查询列表数据
        List<LitemallGoods> goodsList = goodsService.querySelective(categoryId, brandId, keyword, isHot, isNew, page, size, sortWithOrder);
        int total = goodsService.countSelective(categoryId, brandId, keyword, isHot, isNew, page, size, sortWithOrder);

        // 查询商品所属类目列表。
        List<Integer> goodsCatIds = goodsService.getCatIds(brandId, keyword, isHot, isNew);
        List<LitemallCategory> categoryList = null;
        if(goodsCatIds.size() != 0) {
            categoryList = categoryService.queryL2ByIds(goodsCatIds);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("goodsList", goodsList);
        data.put("filterCategoryList", categoryList);
        data.put("count", total);
        return ResponseUtil.ok(data);
    }

    /**
     * 　　新品首发
     */
    @RequestMapping("new")
    public Object newGoods() {
        Map<String,String> bannerInfo = new HashMap<>();
        bannerInfo.put("url", "");
        bannerInfo.put("name", "坚持初心，为你寻觅世间好物");
        bannerInfo.put("imgUrl", "http://yanxuan.nosdn.127.net/8976116db321744084774643a933c5ce.png");
        Map<String, Object> data = new HashMap<>();
        data.put("bannerInfo", bannerInfo);
        return ResponseUtil.ok(data);
    }

    /**
     * 　　人气推荐
     */
    @RequestMapping("hot")
    public Object hotGoods() {
        Map<String,String> bannerInfo = new HashMap<>();
        bannerInfo.put("url", "");
        bannerInfo.put("name", "大家都在买的严选好物");
        bannerInfo.put("imgUrl", "http://yanxuan.nosdn.127.net/8976116db321744084774643a933c5ce.png");
        Map<String, Object> data = new HashMap<>();
        data.put("bannerInfo", bannerInfo);
        return ResponseUtil.ok(data);
    }

    /**
     * 大家都在看的商品
     */
    @RequestMapping("related")
    public Object related(String id) {
        if(id == null){
            return ResponseUtil.fail402();
        }

        LitemallGoods goods = goodsService.findByGoodsSn(id);
        int cid = goods.getCategoryId();

        // 查找六个相关商品
        int related = 6;
        List<LitemallGoods> goodsList = goodsService.queryByCategory(cid, 0, related);
        Map<String, Object> data = new HashMap<>();
        data.put("goodsList", goodsList);
        return ResponseUtil.ok(data);
    }

    /**
     * 　　在售的商品总数
     */
    @RequestMapping("count")
    public Object count() {
        Integer goodsCount = goodsService.queryOnSale();
        Map<String, Object> data = new HashMap<>();
        data.put("goodsCount", goodsCount);
        return ResponseUtil.ok(data);
    }

}