package org.linlinjava.litemall.admin.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallReserve;
import org.linlinjava.litemall.db.domain.LitemallReserveExample;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallReserveService;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/padReserve")
public class ReservrPadController {
	
  @Autowired
  private LitemallReserveService litemallReserveService;
  
  @Autowired
  private LitemallOrderGoodsService litemallOrderGoodsService;
  
  @Autowired
  private LitemallOrderService litemallOrderService;
  
  /**
   * 根据管理员所在门店查询当前门店当前天得预约情况
   *  
   * 1、当天所有预约记录详细情况（预约人名称、预约时间点，预约的项目名称）
   *
   * 
   * @param storeId
   * @return
   */
   @GetMapping("/reserveData")
    public Object reserveDetail(String storeId,String time){
	
    	//日期格式转换
		String dateStr = time.replaceAll("-", "/");
		String[] dateStrArr =  dateStr.split("/");
		if(dateStrArr[1].length()==1) {
			dateStrArr[1] = "0"+dateStrArr[1];
		}
		if(dateStrArr[2].length()==1) {
			dateStrArr[2] = "0"+dateStrArr[2];
		}
		
		String todayStr =dateStrArr[0]+"/"+dateStrArr[1]+"/"+dateStrArr[2];
		
		//查询门店当前天的订单
		List<LitemallReserve> resList = litemallReserveService.selectStoreReserve(storeId,todayStr);
	
		Map<String, Object> data = new HashMap<>();    	
		
		data.put("reserveDetail", resList);
	
		return ResponseUtil.ok(data);
    }
   
   /**
    * 根据医生所在门店查询当前门店当前天得预约情况
    *  
    * 1、当天所有预约记录详细情况（预约人名称、预约时间点，预约的项目名称）
    *
    * 
    * @param storeId
    * @return
    */
   
   @GetMapping("/reserveCount")
   public Object reserveCount(String storeId,String time){
	
		//日期格式转换
		String dateStr = time.replaceAll("-", "/");
		String[] dateStrArr =  dateStr.split("/");
		if(dateStrArr[1].length()==1) {
			dateStrArr[1] = "0"+dateStrArr[1];
		}
		if(dateStrArr[2].length()==1) {
			dateStrArr[2] = "0"+dateStrArr[2];
		}
		
		String todayStr =dateStrArr[0]+"/"+dateStrArr[1]+"/"+dateStrArr[2];
		//查询门店当前天的订单
		List<LitemallReserve> resList = litemallReserveService.selectStoreReserve(storeId,todayStr);
		BigDecimal actualSUM = new  BigDecimal("0.00");
		
		//计算服务订单每个疗程的价格
		
		//查询当天收入
		if(resList.size()>0) {
			for (LitemallReserve litemallReserve : resList) {
				BigDecimal actual = litemallReserve.getActualPrice().divide(new BigDecimal(litemallReserve.getTreatmentNum()*litemallReserve.getNumber()),3,BigDecimal.ROUND_HALF_UP);
				actualSUM = actualSUM.add(actual);
			}
		}
		
		//查询到店人数  start_time 不为null
		List<LitemallReserve> resTodCusList = litemallReserveService.selectTodCus(storeId,todayStr);
	
		Map<String, Object> data = new HashMap<>();    	
		data.put("reserveCount", resList.size());//门店预约总数
		data.put("comeCount", resTodCusList.size());//门店某天到店顾客数量
		data.put("orderIncomeCount",actualSUM);//门店某天当天收入
		return ResponseUtil.ok(data);
   }
   
   
   /**
    * 
    * 1、门店医生
    * 
    * @param storeId
    * @return
    */
   
   @GetMapping("/reserveDcotor")
   public Object reserveDcotor(String storeId){
	   
	   	
		//查询门店所属医生
		List<LitemallUser> userList  =  litemallReserveService.selectStorePadDoctor(storeId);//需要修改
		List<LitemallUser> touserList  = new ArrayList<LitemallUser>();
		//根据id 查询用户角色  查询出只有医生角色的用户
		for (int i=0; i<userList.size();i++) {
			LitemallUser litemallUser = userList.get(i);
			//查询出用户拥有哪些角色 如果同时拥有 美疗师 和管理员角色就移除
			List<LitemallRole> roleList =  litemallReserveService.selectUserRole(litemallUser);
			String roleStr="";
			for (LitemallRole litemallRole : roleList) {
				roleStr +=litemallRole.getRoleName()+",";
			}
			//筛选出含有“美疗师”角色且不含有“门店管理员”角色的用户
			if( !(roleStr.contains("美疗师") && !roleStr.contains("门店管理员")) ) {
				//userList.remove(i);
			}else {
				touserList.add(litemallUser);
			}			
		}		
		
		Map<String, Object> data = new HashMap<>();    	
	
		data.put("userList", touserList);
	
		return ResponseUtil.ok(data);
   }
   
   
  
   
   
   /**
    * 给预定的数据分配医生
    * @param reserveId 预约信息id 去更新
    * @return
    */
   @RequestMapping("/inertDoctorRes")
   public Object inertDoctorRes(@RequestBody LitemallReserveExample litemallReserveExample){
	   	
	   Integer updateCount = 0;
		//查询门店所属医生
	   updateCount  =  litemallReserveService.updateReserveStatus(litemallReserveExample);//根据状态值判定更新reserve的状态和 开始时间或者结束时间
		
		Map<String, Object> data = new HashMap<>();  
		if(updateCount>0) {
			data.put("res", "ok");
		}else {
			data.put("res", "no");
		}
		return ResponseUtil.ok(data);
   }
   
   
   /**
    * 医生工作的时候 更改订单状态和订单的开始时间或者结束时间
    * @param reserveId 预约信息id 去更新
    * @return
    */
   @RequestMapping("/updateReserveStatus")
   public Object updateReserveStatus(@RequestBody LitemallReserveExample litemallReserveExample){
	
	   Integer updateCount = 0;
		//查询门店所属医生
	   updateCount  =  litemallReserveService.updateReserveStatus(litemallReserveExample);//根据状态值判定更新reserve的状态和 开始时间或者结束时间
	   //暂时定位项目服务开始后订单状态也会变化
	   if("0".equals(litemallReserveExample.getFlag()) ) {
		   //修改预约项目订单详细状态为401
		   if(litemallReserveExample.getOrderGoodsId()!=null && !"".equals(litemallReserveExample.getOrderGoodsId())) {
			   //主订单
			   LitemallOrder order = new LitemallOrder();
			   //子订单
			   LitemallOrderGoods ogoods = new LitemallOrderGoods();			   
			   ogoods.setId(Integer.parseInt(litemallReserveExample.getOrderGoodsId()));
			   //这个订单详情的查询
			   LitemallOrderGoods oldOrderGoods = litemallOrderGoodsService.queryById(ogoods.getId());			   
			   order.setId(oldOrderGoods.getOrderId());//主订单id
			   
			   if(oldOrderGoods.getTreatmentNum()!=0) {
				   //服务商品疗程未用完，此详细订单部分收货
				   //ogoods.setOrderStatus(OrderUtil.STATUS_PART_CONFIRM);	
				   //Integer retStr = litemallReserveService.updateOrderGoodsStatus(ogoods);
				   //主订单状态也改成部分收货				   
				   order.setOrderStatus(OrderUtil.STATUS_PART_CONFIRM);				   
			   }else {
				   /**
				    * 服务商品疗程已用完，确认收货
				    * 需要查询该订单所属的主订单下的所有子订单的状态是否都已经确认收货，若都确认收货则主订单确认收货，否则主订单状态改为部分收货
				    */
				   ogoods.setOrderStatus(OrderUtil.STATUS_CONFIRM);
				   Integer retStr = litemallReserveService.updateOrderGoodsStatus(ogoods);
				   List<LitemallOrderGoods> orderGoodsList = litemallOrderGoodsService.queryByOid(oldOrderGoods.getOrderId());
				   if(orderGoodsList!=null && orderGoodsList.size()>0) {
					   boolean isAllConfirm = true;
					   for(LitemallOrderGoods ordergoods : orderGoodsList) {
						   if(!ordergoods.getOrderStatus().equals(OrderUtil.STATUS_CONFIRM)) {
							   isAllConfirm = false;
						   }
					   }
					   if(isAllConfirm) {//确认收货
						   order.setOrderStatus(OrderUtil.STATUS_CONFIRM);
					   }else {//部分收货
						   order.setOrderStatus(OrderUtil.STATUS_PART_CONFIRM);
					   }
				   }
			   }
			   litemallOrderService.update(order);
			   
		   }
		   
	   }
		
		Map<String, Object> data = new HashMap<>();  
		if(updateCount>0) {
			data.put("res", "ok");
		}else {
			data.put("res", "no");
		}
		return ResponseUtil.ok(data);
   }
   
   //充值接口-----------后面写
   
   
   //医生登录已分配的预约数据
   @GetMapping("/doctorReserveData")
   public Object selectStoreResByDoc(String storeId,String time,String doctorId){
   	//日期格式转换
		String dateStr = time.replaceAll("-", "/");
		String[] dateStrArr =  dateStr.split("/");
		if(dateStrArr[1].length()==1) {
			dateStrArr[1] = "0"+dateStrArr[1];
		}
		if(dateStrArr[2].length()==1) {
			dateStrArr[2] = "0"+dateStrArr[2];
		}
		String todayStr =dateStrArr[0]+"/"+dateStrArr[1]+"/"+dateStrArr[2];
		//查询门店当前天的订单
		List<LitemallReserve> resList = litemallReserveService.selectStoreResByDoc(storeId,todayStr,doctorId);
	
		Map<String, Object> data = new HashMap<>();    	
		
		data.put("doctorReserveDetail", resList);
	
		return ResponseUtil.ok(data);
   }
   
   //查询医生角色分配的预约数据 总预约数
   @GetMapping("/doctorReserveCount")
   public Object doctorReserveCount(String storeId,String time,String doctorId){
   	//日期格式转换
		String dateStr = time.replaceAll("-", "/");
		String[] dateStrArr =  dateStr.split("/");
		if(dateStrArr[1].length()==1) {
			dateStrArr[1] = "0"+dateStrArr[1];
		}
		if(dateStrArr[2].length()==1) {
			dateStrArr[2] = "0"+dateStrArr[2];
		}
		String todayStr =dateStrArr[0]+"/"+dateStrArr[1]+"/"+dateStrArr[2];
		//医生当前天的分配的预约数据
		List<LitemallReserve> resList = litemallReserveService.selectStoreResByDoc(storeId,todayStr,doctorId);
		
		//医生当前天的已服务的预约数据
		List<LitemallReserve> docSerList = litemallReserveService.selectStoreResSer(storeId,todayStr,doctorId);
		Map<String, Object> data = new HashMap<>();    	
		
		data.put("doctorReserveCount", resList.size());
		data.put("serviceCount", docSerList.size());
	
		return ResponseUtil.ok(data);
   }
   
   
   

}
