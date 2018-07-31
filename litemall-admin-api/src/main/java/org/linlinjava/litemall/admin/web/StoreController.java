package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallOptions;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallReserveService;
import org.linlinjava.litemall.db.service.LitemallStoreService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/store")
public class StoreController {
	 private final Log logger = LogFactory.getLog(StoreController.class);
	 
	 @Autowired
	 private LitemallStoreService storeService;
	 
	 @Autowired
	 private LitemallUserService userService;
	 
	 @Autowired
	 private LitemallGoodsService goodsService;
	 
	 @Autowired
	 private LitemallReserveService litemallReserveService;
	 
	 
	/**
	 * 门店列表
	 * @param adminId
	 * @param store
	 * @param page
	 * @param limit
	 * @param sort
	 * @param order
	 * @return
	 */
	 @RequestMapping(value = "/list", method = RequestMethod.POST)  
	 public Object list(@LoginAdmin Integer adminId,
			 LitemallStore store,
             @RequestParam(value = "page", defaultValue = "1") Integer page,
             @RequestParam(value = "limit", defaultValue = "10") Integer limit,
             String sort, String order) {
		 if(adminId == null){
	            return ResponseUtil.unlogin();
         }
        List<LitemallStore> adList = storeService.querySelective(store, page, limit, sort, order);
     
        int total = storeService.countSelective(store, page, limit, sort, order);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", adList);
		return ResponseUtil.ok(data);
	 }
	 
		/**
		 * 门店列表
		 * @param adminId
		 * @param store
		 * @param page
		 * @param limit
		 * @param sort
		 * @param order
		 * @return
		 */
		 @RequestMapping(value = "/adminStorelist", method = RequestMethod.POST)  
		 public Object adminStorelist(@LoginAdmin Integer adminId,
				 LitemallStore store,
	             @RequestParam(value = "page", defaultValue = "1") Integer page,
	             @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	             String sort, String order) {
			 if(adminId == null){
		            return ResponseUtil.unlogin();
	         }
	        List<LitemallStore> adList = storeService.queryAdminStorelist(store, page, limit, sort, order);
	        int total =0;
	        if(null !=adList) {
	        	total = adList.size();
	        }
	   
	        Map<String, Object> data = new HashMap<>();
	        data.put("total", total);
	        data.put("items", adList);
			return ResponseUtil.ok(data);
		 }
		 
	 
		/**
		 * 用户列表
		 * @param adminId
		 * @param store
		 * @param page
		 * @param limit
		 * @param sort
		 * @param order
		 * @return
		 */
		 @RequestMapping(value = "/user", method = RequestMethod.POST)  
		 public Object user(@LoginAdmin Integer adminId,
				 LitemallUser user,
	             @RequestParam(value = "page", defaultValue = "1") Integer page,
	             @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	             String sort, String order) {
			 if(adminId == null){
		            return ResponseUtil.unlogin();
	         }
			   	List<LitemallUser> userList = userService.queryUserSelective(user, page, limit, sort);
		        int total = userService.countUserSeletive(user, page, limit, sort);
		        Map<String, Object> data = new HashMap<>();
		        data.put("total", total);
		        data.put("items", userList);
			return ResponseUtil.ok(data);
		 }
		 
		 
	 /**
	  * 查询医生
	  * 
	  */
	 
	 @RequestMapping(value = "/doctor", method = RequestMethod.POST)  
	 public Object doctor(@LoginAdmin Integer adminId,
			 LitemallUser user,
             @RequestParam(value = "page", defaultValue = "1") Integer page,
             @RequestParam(value = "limit", defaultValue = "10") Integer limit,
             String sort, String order) {
		 if(adminId == null){
	            return ResponseUtil.unlogin();
         }
		   	List<LitemallUser> userList = userService.queryDoctorSelective(user, page, limit, sort);
	        int total = userService.countDoctorSeletive(user, page, limit, sort);
	        Map<String, Object> data = new HashMap<>();
	        data.put("total", total);
	        data.put("items", userList);
		return ResponseUtil.ok(data);
	 }
		 
	
	/**
	 * 门店新增
	 * @param adminId
	 * @param store
	 * @return
	 */
	@PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId,@RequestBody  LitemallStore store){
	        if(adminId == null){
	            return ResponseUtil.unlogin();
	        }
	        storeService.add(store);
	        return ResponseUtil.ok(store);
	    }
	
    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody  LitemallStore store){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        storeService.updateById(store);
        return ResponseUtil.ok(store);
    }
	 
    
    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallStore store){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        storeService.deleteById(store.getId());
        return ResponseUtil.ok();
    }
    
	/**
	 * 门店店长
	 * @param adminId
	 * @param store
	 * @return
	 */
	@PostMapping("/createManage")
    public Object createManage(@LoginAdmin Integer adminId,@RequestBody  LitemallStore store){
	        if(adminId == null){
	            return ResponseUtil.unlogin();
	        }
	        storeService.deleteManage(store);
	        storeService.addManage(store);
	        return ResponseUtil.ok(store);
    }
	/**
	 * 
	 * @param adminId
	 * @param store
	 * @return
	 */
	@PostMapping("/createDoctor")
    public Object createDoctor(@LoginAdmin Integer adminId,@RequestBody  LitemallStore store){
	        if(adminId == null){
	            return ResponseUtil.unlogin();
	        }
	     
	        //设置医生  
	      
	        
	        //根据门店id 和 服务编号来 确定更新还是 新增  切割goodsSn
	        String userId = store.getUserId();
	        if(userId.contains(",")) {
	        	String[]	UserIdArr =  userId.split(",");
	        	for(int i=0 ; i<UserIdArr.length;i++) {
	        	 /*	store.setUserId(UserIdArr[i]);
		        	Integer count =  storeService.slectStoreGoods(store);//判断医生在当前门店是否已经拥有
		   	       if(count<1) {*/
	        		  store.setUserId(UserIdArr[i]);
		   	    	  storeService.addDoctor(store);
		   	      /* }*/
	        	}
	        	
	        }
	        
	        return ResponseUtil.ok(store);
    }
	
	/**
	 * 门店服务添加
	 * @param adminId
	 * @param store
	 * @return
	 */
	@PostMapping("/createGoods")
    public Object createGoods(@LoginAdmin Integer adminId,@RequestBody  LitemallStore store){
	        if(adminId == null){
	            return ResponseUtil.unlogin();
	        }
	        //根据门店id 和 服务编号来 确定更新还是 新增  切割goodsSn
	        String goodsId = store.getGoodsId();
	        if(goodsId.contains(",")) {
	        	String[]	GoodsIdArr =  goodsId.split(",");
	        	for(int i=0 ; i<GoodsIdArr.length;i++) {
	        	 	store.setGoodsId(GoodsIdArr[i]);
		        	Integer count =  storeService.slectStoreGoods(store);
		   	       if(count<1) {
		   	    	 storeService.addStoreGoods(store);
		   	       }
	        	}
	        	
	        }
	     
	        return ResponseUtil.ok(store);
    }
	

	/**
	 * 获取项目下拉框
	 * @param adminId
	 * @return
	 */
	 @RequestMapping(value = "/goodsOptions", method = RequestMethod.POST)  
	 public Object list(@LoginAdmin Integer adminId) {
		if(adminId == null){
			return ResponseUtil.unlogin();
		}
			List<LitemallOptions> adList = storeService.queryGoodOptions();
			Map<String, Object> data = new HashMap<>();
			data.put("items", adList);
			return ResponseUtil.ok(data);
	 }
	 
	
	
	
	//获取省下拉框
	 @RequestMapping(value = "/provinceOptions", method = RequestMethod.POST)  
	 public Object listProvince(@LoginAdmin Integer adminId) {
		if(adminId == null){
			return ResponseUtil.unlogin();
		}
		List<LitemallOptions> adList = storeService.queryProvinceOptions();
		Map<String, Object> data = new HashMap<>();
		data.put("items", adList);
		return ResponseUtil.ok(data);
	 }
	 
	
	//获取省份下所在城市
	 @RequestMapping(value = "/cityOptions", method = RequestMethod.POST)  
	 public Object listAreaProvince(@LoginAdmin Integer adminId,@RequestBody  LitemallStore store) {
		if(adminId == null){
			return ResponseUtil.unlogin();
		}
		String province=store.getProvince();
		List<LitemallOptions> adList = storeService.queryAreaOptions(province);
		Map<String, Object> data = new HashMap<>();
		data.put("items", adList);
		return ResponseUtil.ok(data);
	 }
	 
	 
	 @RequestMapping(value = "/goodslist", method = RequestMethod.POST)
	    public Object list(@LoginAdmin Integer adminId,
	    					LitemallGoods goods,
	                       @RequestParam(value = "page", defaultValue = "1") Integer page,
	                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                       String sort, String order){
	        if(adminId == null){
	            return ResponseUtil.unlogin();
	        }

	        List<LitemallGoods> goodsList = goodsService.querySelectiveForStore(goods, page, limit, sort, order);
	        int total = goodsService.countForStore(goods, page, limit, sort, order);
	        Map<String, Object> data = new HashMap<>();
	        data.put("total", total);
	        data.put("items", goodsList);

	        return ResponseUtil.ok(data);
	    }
	 
	 
	 
	    @PostMapping("/deleteStoreGoods")
	    public Object deleteStoreGoods(@LoginAdmin Integer adminId, @RequestBody LitemallStore store){
	        if(adminId == null){
	            return ResponseUtil.unlogin();
	        }
	        storeService.deleteStoreGoods(store);
	        return ResponseUtil.ok();
	    }
	    
	    
	    
	    @PostMapping("/deleteStoreUser")
	    public Object deleteStoreUser(@LoginAdmin Integer adminId, @RequestBody LitemallStore store){
	        if(adminId == null){
	            return ResponseUtil.unlogin();
	        }
	        storeService.deleteStoreUser(store);
	        return ResponseUtil.ok();
	    }
	    
	    
	
	  
	 
	    @RequestMapping(value = "/addUserStore", method = RequestMethod.POST)
	    public Object addUserStore(  LitemallStore store){
	        String storeId = store.getStoreId();
	        String roleStr ="";
	        /**
	         * 根据userId 去查询关联用户的角色，
	         * （1）用户同时拥有两种角色 “美。。”“门。。”就设置为门店管理员
	         * （2）用户角色为“美。。”和  非“门。。。”就设置为“美。。。”
	         */
	    
	        LitemallUser litemallUser = new LitemallUser();
	        litemallUser.setId(Integer.parseInt(store.getUserId()));
	        List<LitemallRole> roleList =  litemallReserveService.selectUserRole(litemallUser);
	    	for (LitemallRole litemallRole : roleList) {
				roleStr +=litemallRole.getRoleName()+",";
			}
			//用户只要有“门。。”角色就设置为门店管理员
			if(roleStr.contains("门店管理员") ) {
				//	设置为门店管理员 flag =1
			    if(storeId.contains(",")) {
		        	String[]	storeIdArr =  storeId.split(",");
		        	for(int i=0 ; i<storeIdArr.length;i++) {
		        	      store.setStoreId(storeIdArr[i]);
		        	      store.setFlag("0");
			   	    	  storeService.addManage(store);
			   	   
		        	}
		        	
		        }
			}
	    	//用户角色为“美。。”和  非“门。。。”就设置为“美。。。”
			if( (roleStr.contains("美疗师") && roleStr.length()>=4 && (!roleStr.contains("门店管理员")) )) {
				//	设置为门店管理员 flag =1
			    if(storeId.contains(",")) {
		        	String[]	storeIdArr =  storeId.split(",");
		        	for(int i=0 ; i<storeIdArr.length;i++) {
		        	      store.setStoreId(storeIdArr[i]);
		        	      store.setFlag("1");
			   	    	  storeService.addDoctor(store);
			   	   
		        	}
		        	
		        }
			}
			//角色不包含“美。。”也不包含“门。。。” flag 设置为2  2代表其他
			if( (!( roleStr.contains("美疗师") ))  &&   (!( roleStr.contains("门店管理员") ) ) ) {
				//	设置为门店管理员 flag =1
			    if(storeId.contains(",")) {
		        	String[]	storeIdArr =  storeId.split(",");
		        	for(int i=0 ; i<storeIdArr.length;i++) {
		        	      store.setStoreId(storeIdArr[i]);
		        	      store.setFlag("2");
			   	    	  storeService.addDoctor(store);
			   	   
		        	}
		        	
		        }
			}
	    
	        
	        return ResponseUtil.ok();
	    }
	    
	 /**
	  * 查询所有的门店信息
	  * @return
	  */
     @RequestMapping(value = "/storeDataList", method = RequestMethod.POST)  
	 public Object storeDataList(LitemallStore store) {		
        List<LitemallStore> adList = storeService.queryAllStoreList();
        List<LitemallStore> goodStoreList = null;
        if(store!=null && (StringUtils.isNotBlank(store.getGoodsId()) || StringUtils.isNotBlank(store.getStoreId())) ) {
        	goodStoreList = storeService.queryGoodsStoreList(store);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("allStoreList", adList);
        data.put("goodStoreList", goodStoreList);
		return ResponseUtil.ok(data);
	 }
     
     /**
      * 查询商品所属的门店信息
      * @param store
      * @return
      */
     @RequestMapping(value = "/getGoodsStoreList", method = RequestMethod.POST)  
	 public Object getGoodsStoreList(LitemallStore store) {		
        List<LitemallStore> goodStoreList = storeService.queryGoodsStoreList(store);
        Map<String, Object> data = new HashMap<>();
        data.put("goodStoreList", goodStoreList);
		return ResponseUtil.ok(data);
	 }
    

}
