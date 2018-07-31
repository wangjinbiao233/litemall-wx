package org.linlinjava.litemall.wx.web;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.domain.LitemallComment;
import org.linlinjava.litemall.db.domain.LitemallKCategory;
import org.linlinjava.litemall.db.domain.LitemallKnowledge;
import org.linlinjava.litemall.db.domain.LitemallLikedetails;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallCommentService;
import org.linlinjava.litemall.db.service.LitemallKnowledgeService;
import org.linlinjava.litemall.db.service.LitemallLikedetailsService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/knowledge")
public class WxKnowledgeConctroller {

	@Autowired
    private LitemallKnowledgeService knowledgeService;
	
	@Autowired
	private LitemallCommentService commentService;
	@Autowired
	private LitemallUserService userService;
	@Autowired
	private LitemallLikedetailsService likedetailsService;
	
	/**
	 * 获取知识分类的信息列表
	 * @param kCategoryId
	 * @return
	 */
	@RequestMapping("knowledgeCategory")
	public Object knowledgeCategory(Integer kCategoryId) {
		// 获取知识分类
        List<LitemallKCategory> kCategoryList = knowledgeService.getKCategory();
        if(kCategoryId==-1) {
        	if(!kCategoryList.isEmpty()) {
        		kCategoryId = kCategoryList.get(0).getId();
        	}
        }
        Map<String, Object> data = new HashMap<>();
        data.put("kCategory", kCategoryId);
        data.put("kCategoryList", kCategoryList);
        return ResponseUtil.ok(data);
	}
	/**
	 * 根据知识分类id获取知识详情列表
	 * @param kCategoryId
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("knlist")
    public Object knlist(Integer kCategoryId,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {

        LitemallKnowledge knowledge = null;
        if(-1 != kCategoryId) {
        	knowledge = new LitemallKnowledge();
        	knowledge.setKnowledgeCls(kCategoryId);
        }
        List<LitemallKnowledge> knowledgeList = knowledgeService.selectKnowledgeList(knowledge, page, size, null, null);
        
        Map<String, Object> data = new HashMap<>();
        data.put("kCategory", kCategoryId);
        data.put("knowledgeList", knowledgeList);

        return ResponseUtil.ok(data);
    }
		
    @RequestMapping("list")
    public Object list(Integer kCategoryId,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {

        // 获取知识分类
        List<LitemallKCategory> kCategoryList = knowledgeService.getKCategory();
        LitemallKnowledge knowledge = null;
        String kCategoryName = null;
        if(-1 != kCategoryId) {
        	knowledge = new LitemallKnowledge();
        	knowledge.setKnowledgeCls(kCategoryId);
        }else {
        	if(!kCategoryList.isEmpty()) {
        		kCategoryId = kCategoryList.get(0).getId();
        	}
        }
        List<LitemallKnowledge> knowledgeList = knowledgeService.selectKnowledgeList(knowledge, page, size, null, null);
        if(null == kCategoryId) 
    		kCategoryName = kCategoryList.get(0).getName();
        else {
        	if(null != knowledgeList && !knowledgeList.isEmpty()) 
        		kCategoryName = knowledgeList.get(0).getKnowledgeClsName();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("kCategory", kCategoryId);
        data.put("kCategoryName", kCategoryName);
        data.put("kCategoryList", kCategoryList);
        data.put("knowledgeList", knowledgeList);

        return ResponseUtil.ok(data);
    }
    @RequestMapping("detail")
    public Object detail(Integer id) {
    	LitemallKnowledge knowledge = new LitemallKnowledge();
    	knowledge.setId(id);
    	List<LitemallKnowledge> selectKnowledgeList = knowledgeService.selectKnowledgeList(knowledge, 1, 10, null, null);
    	Map<String, Object> data = new HashMap<>();
    	LitemallKnowledge res = null;
    	if(!selectKnowledgeList.isEmpty()) {
    		res= selectKnowledgeList.get(0);
    		res.setContent(res.getContent().replaceAll("&lt;", ""));
    	}
    	data.put("knowledge", res);
    	
    	return ResponseUtil.ok(data);
    }
    
    @RequestMapping("detailcomment")
    public Object detailcomment(Integer userId,Integer valueId,byte typeId) {
    	Map<String, Object> data = new HashMap<>();
    	
    	List<LitemallComment> comments = commentService.queryValueIdTypeId(valueId, typeId, 0, 5);
    	int datacount=commentService.countValueIdTypeId(valueId, typeId);
    	
    	List<Map<String, Object>> commentsVo = new ArrayList<>(comments.size());
    	List<String> imgList=new ArrayList<>();
    	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	
    	for(LitemallComment comment : comments){
             Map<String, Object> c = new HashMap<>();
             c.put("id", comment.getId());
             c.put("addTime", comment.getAddTime());
             c.put("commnetTime", df.format(comment.getAddTime()));
             c.put("content", comment.getContent());
             LitemallUser user = userService.findById(comment.getUserId());
             c.put("nickname", user!=null?user.getNickname():"");
             c.put("avatar", user!=null?user.getAvatar():"");
             c.put("picList", comment.getPicUrls());
             c.put("likenum", comment.getLikenum());
             c.put("commentId", comment.getId());
             //点赞情况 
             LitemallLikedetails likedetails = likedetailsService
            		 .selectByCommentIdUserid(comment.getId(), userId,0);
             c.put("islike", likedetails!=null?true:false);
             
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
             c.put("subcomment", subcommentsVo);
             
             commentsVo.add(c);
             imgList.addAll(Arrays.asList(comment.getPicUrls()));
         }
    	data.put("count", datacount);
    	data.put("datas", commentsVo);
    	data.put("imgList", imgList);
    	return ResponseUtil.ok(data);
    }
    
   /**
    * 点赞单挑评论
    * @param userId
    * @param commentId
    * @param islike
    * @return
    */
   
    @RequestMapping("liketag")
    public Object liketag(Integer userId,Integer commentId,Boolean islike) {
    	Integer one=1;
    	
    	LitemallComment comment = commentService.findById(commentId);
    	if(null==comment){
    		 return ResponseUtil.fail();
    	}
    	
    	LitemallLikedetails likedetails = likedetailsService.selectByCommentIdUserid(commentId,userId,null);
    	if(islike){//点赞 可能之前点过
    		if(likedetails==null){
	    		LitemallLikedetails record=new LitemallLikedetails();
	    		record.setCommentId(commentId);
	    		record.setCreatetime(new Date());
	    		record.setStatus(0);
	    		record.setUserId(userId);
	    		record.setValueId(comment.getValueId());
	    		record.setValueType(comment.getTypeId()&0xff);
				//新增
	    		likedetailsService.insertSelective(record);
    		}else{
    			likedetails.setStatus(0);
    			likedetails.setUpdatetime(new Date());
    			//修改
    			likedetailsService.updateByCommentIdUserId(likedetails);
    		}
    		
    	}else{//取消赞  之前一定点过
			one=-1;
			likedetails.setStatus(1);
			likedetails.setUpdatetime(new Date());
			//修改
			likedetailsService.updateByCommentIdUserId(likedetails);
    	}
    	
    	//评论表的单条点赞
		commentService.updateLikenumById(commentId, one);
		return ResponseUtil.ok();
    	 
    }
    /**
     * 点赞文章
     * @param userId
     * @param knowldegeId
     * @param islike
     * @return
     */
    @RequestMapping("likecounttag")
    public Object likeCountTag(Integer userId,Integer knowldegeId,Boolean islike) {
    	 if(knowldegeId == null){
             return ResponseUtil.fail402();
         }
    	 if(userId == null){
           return ResponseUtil.fail401();
    	 }
    	 
    	 Integer one=1;
    	 
    	 LitemallLikedetails likedetails = likedetailsService.selectByUserIdValueId(knowldegeId, userId, null);
    	 if(islike){//点赞 可能之前点过
     		if(likedetails==null){
 	    		LitemallLikedetails record=new LitemallLikedetails();
 	    		//record.setCommentId(commentId);
 	    		record.setCreatetime(new Date());
 	    		record.setStatus(0);
 	    		record.setUserId(userId);
 	    		record.setValueId(knowldegeId);
 	    		record.setValueType(2&0xff);//知识为2
 				//新增
 	    		likedetailsService.insertSelective(record);
     		}else{
     			likedetails.setStatus(0);
     			likedetails.setUpdatetime(new Date());
     			//修改
     			likedetailsService.updateByCommentIdUserId(likedetails);
     		}
     		
     	}else{//取消赞  之前一定点过
     		 one=-1;
 			likedetails.setStatus(1);
 			likedetails.setUpdatetime(new Date());
 			//修改
 			likedetailsService.updateByCommentIdUserId(likedetails);
     	}
    	 
    	 knowledgeService.updatePraiseCountById(knowldegeId, one);
    	 
    	return ResponseUtil.ok();
    }
}
