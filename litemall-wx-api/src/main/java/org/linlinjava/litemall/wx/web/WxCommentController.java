package org.linlinjava.litemall.wx.web;

import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.db.domain.LitemallComment;
import org.linlinjava.litemall.db.domain.LitemallLikedetails;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.service.LitemallCommentService;
import org.linlinjava.litemall.db.service.LitemallCouponService;
import org.linlinjava.litemall.db.service.LitemallKnowledgeService;
import org.linlinjava.litemall.db.service.LitemallLikedetailsService;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallReserveService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.UserInfoService;
import org.linlinjava.litemall.wx.dao.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wx/comment")
public class WxCommentController {
    @Autowired
    private LitemallCommentService commentService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallCouponService couponService;
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
	private LitemallLikedetailsService likedetailsService;
    
	@Autowired
    private LitemallKnowledgeService knowledgeService;
	
	@Autowired
	private LitemallReserveService litemallReserveService;
	
	@Autowired
	private LitemallOrderGoodsService litemallOrderGoodsService;
	  
	  @Autowired
	  private LitemallOrderService litemallOrderService;

    /**
     * 发表评论
     */
    @RequestMapping("post")
    public Object post(HttpServletRequest request, @RequestBody LitemallComment comment) {
    	
    	  if(comment == null){
              return ResponseUtil.fail402();
          }
    	
    	Integer userId = comment.getUserId();
        if(userId == null){
            return ResponseUtil.fail401();
        }

        comment.setAddTime(LocalDateTime.now());
        comment.setUserId(userId);
        commentService.save(comment);
        //知识表的总评论数(没有删除评论功能)
        if(comment.getTypeId()==2){
        	knowledgeService.updateCommentCountById(comment.getValueId(), 1);
        }
        else if(comment.getTypeId()==0) {//商品评价
        	//getOrderId()就是表litemall_order的id值
        	//comment.getOrdergoodsId()就是表litemall_order_goods表主键id）
        	if(comment.getOrdergoodsId()!=null && !"".equals(comment.getOrdergoodsId())) {
        		//主订单
 			   LitemallOrder order = new LitemallOrder();
 			   order.setId(comment.getOrderId());//主订单id
 			   //子订单
 			   LitemallOrderGoods ogoods = new LitemallOrderGoods();			   
 			   ogoods.setId(comment.getOrdergoodsId());
 			   //这个订单详情的查询
			   LitemallOrderGoods oldOrderGoods = litemallOrderGoodsService.queryById(ogoods.getId());	
			   if(oldOrderGoods.getTreatmentNum()==0) {
				   ogoods.setOrderStatus(OrderUtil.STATUS_FINISHED);	
				   Integer retStr = litemallReserveService.updateOrderGoodsStatus(ogoods);
				   
				   //主订单下的详情订单列表
				   List<LitemallOrderGoods> orderGoodsList = litemallOrderGoodsService.queryByOid(oldOrderGoods.getOrderId());
				   if(orderGoodsList!=null && orderGoodsList.size()>0) {
					   boolean isAllConfirm = true;
					   for(LitemallOrderGoods ordergoods : orderGoodsList) {
						   if(!ordergoods.getOrderStatus().equals(OrderUtil.STATUS_FINISHED)) {
							   isAllConfirm = false;
						   }
					   }
					   if(isAllConfirm) {//确认主订单下的所有详情订单都完成评价
						   order.setOrderStatus(OrderUtil.STATUS_FINISHED);
						   litemallOrderService.update(order);
					   }
				   }
			   }
        	}
        }
        
        return ResponseUtil.ok(comment);
    }

    /**
     */
    @RequestMapping("count")
    public Object count(Byte typeId, Integer valueId) {
        int allCount = commentService.count(typeId, valueId, 0, 0, 0);
        int hasPicCount = commentService.count(typeId, valueId, 1, 0, 0);
        Map<String, Object> data = new HashMap();
        data.put("allCount", allCount);
        data.put("hasPicCount", hasPicCount);
        return ResponseUtil.ok(data);
    }

    /**
     * @param typeId
     * @param valueId
     * @param showType 选择评论的类型 0 全部， 1 只显示图片
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("list")
    public Object list(Byte typeId, Integer valueId, Integer showType,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if(typeId == null || valueId == null || showType == null){
            return ResponseUtil.badArgument();
        }

        List<LitemallComment> commentList = commentService.query(typeId, valueId, showType, page, size);
        int count = commentService.count(typeId, valueId, showType, page, size);

        List<Map<String, Object>> commentVoList = new ArrayList<>(commentList.size());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(LitemallComment comment : commentList){
            Map<String, Object> commentVo = new HashMap<>();
            UserInfo userInfo = userInfoService.getInfo(comment.getUserId());
            commentVo.put("userInfo", userInfo);
            commentVo.put("addTime", comment.getAddTime());
            commentVo.put("commentTime", df.format(comment.getAddTime()));
            commentVo.put("content",comment.getContent());
            commentVo.put("picList", comment.getPicUrls());

            commentVoList.add(commentVo);
        }
        Map<String, Object> data = new HashMap();
        data.put("data", commentVoList);
        data.put("count", count);
        data.put("currentPage", page);
        return ResponseUtil.ok(data);
    }
    
    /**
     * @param typeId
     * @param valueId
     * @param showType 选择评论的类型 0 全部， 1 只显示图片
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("listdata")
    public Object listData(Byte typeId, Integer valueId, Integer showType,Integer userId,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if(typeId == null || valueId == null || showType == null||userId==null){
            return ResponseUtil.badArgument();
        }

        List<LitemallComment> commentList = commentService.query(typeId, valueId, showType, page, size);
        int count = commentService.count(typeId, valueId, showType, page, size);

    	List<String> imgList=new ArrayList<>();
        List<Map<String, Object>> commentVoList = new ArrayList<>(commentList.size());
        
        for(LitemallComment comment : commentList){
            Map<String, Object> commentVo = new HashMap<>();
            UserInfo userInfo = userInfoService.getInfo(comment.getUserId());
            commentVo.put("id", comment.getId());
            commentVo.put("userInfo", userInfo);
            commentVo.put("addTime", comment.getAddTime());
            commentVo.put("content",comment.getContent());
            commentVo.put("picList", comment.getPicUrls());
            commentVo.put("likenum", comment.getLikenum());
            commentVo.put("commentId", comment.getId());
          //点赞情况 
            LitemallLikedetails likedetails = likedetailsService
           		 .selectByCommentIdUserid(comment.getId(), userId,0);
            commentVo.put("islike", likedetails!=null?true:false);

            //子评论
            List<LitemallComment> subcomment = commentService.selectBycommentId(comment.getId());
            List<Map<String, Object>> subcommentsVo = new ArrayList<>(subcomment.size());
            for(LitemallComment sb:subcomment){
           	 Map<String, Object> subc = new HashMap<>();
           	 subc.put("id", sb.getId());
           	 subc.put("addTime", sb.getAddTime());
           	 subc.put("content", sb.getContent());
             subc.put("nickname", sb.getRusername());
             subc.put("znickname", sb.getZusername()==null?"": "回复："+sb.getZusername());
             subc.put("likenum", sb.getLikenum());
             subc.put("valueId", sb.getValueId());
            
             LitemallLikedetails lkum = likedetailsService
           		 .selectByCommentIdUserid(sb.getId(), userId,0);
             subc.put("islike", lkum!=null?true:false);
             subc.put("commentId", comment.getId());//(页面需要)单条评论的父id
             subc.put("replayId", sb.getId());//(页面需要)当下个人回复这条评论,就是当前id
             subcommentsVo.add(subc);
            }
            commentVo.put("subcomment", subcommentsVo);
            
            commentVoList.add(commentVo);
            imgList.addAll(Arrays.asList(comment.getPicUrls()));//因为只有父级可以发表图片
        }
        Map<String, Object> data = new HashMap();
        data.put("data", commentVoList);
        data.put("count", count);
        data.put("currentPage", page);
    	data.put("imgList", imgList);
        return ResponseUtil.ok(data);
    }
    
}