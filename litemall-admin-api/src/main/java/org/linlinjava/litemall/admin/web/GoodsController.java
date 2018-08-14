package org.linlinjava.litemall.admin.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallSerialNumber;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallSerialNumberService;
import org.linlinjava.litemall.db.service.LitemallStoreService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/goods")
public class GoodsController {
    private final Log logger = LogFactory.getLog(GoodsController.class);

    @Autowired
    private LitemallGoodsService goodsService;
    
    @Autowired
	private LitemallStoreService storeService;
    
    
    @Autowired
    private LitemallSerialNumberService serialNumberService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
                       String goodsSn, String name, Integer categoryId,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        List<LitemallGoods> goodsList = goodsService.querySelective(goodsSn, name,categoryId, page, limit, sort, order);
        int total = goodsService.countSelective(goodsSn, name,categoryId, page, limit, sort, order);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", goodsList);

        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallGoods goods){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        String prefix = "";
        if(goods.getFlag().equals("1")) {
         //实物商品
        	prefix = "G";
        	
        }else {
         //服务商品	
        	prefix = "S";
        }
        goods.setGoodsSn(prefix+getSerialNumber());
        goodsService.add(goods);
        
        //商品和所属门店id关联数据的保存操作
        LitemallStore storegood = new LitemallStore();
        storegood.setGoodsId(goods.getId().toString());
        if(goods.getStoreIds()!=null && goods.getStoreIds().length>0) {
        	for(String id : goods.getStoreIds()) {
        		storegood.setStoreId(id);
        		Integer count = storeService.slectStoreGoods(storegood);
        		if(count==0) {//数据库中不存在插入
        			storeService.addStoreGoods(storegood);
        		}else {
        			System.out.println(id+"门店id和 "+goods.getId()+"商品id组合数据在数据库中已经存在");
        		}        		
        	}
        }        
        
        return ResponseUtil.ok(goods);
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        if(id == null){
            return ResponseUtil.badArgument();
        }

        LitemallGoods goods = goodsService.findById(id);
        return ResponseUtil.ok(goods);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallGoods goods){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        goodsService.updateById(goods);
        
        //商品和所属门店id关联数据的保存操作
        LitemallStore storegood = new LitemallStore();
        storegood.setGoodsId(goods.getId().toString());//商品id
        List<LitemallStore> oldGoodStoreList = storeService.queryGoodsStoreList(storegood);//商品的旧关联数据集合
        
		if(oldGoodStoreList!=null && oldGoodStoreList.size()>0) {
        //旧关联数据存在，需要新旧数据比对修改或删除
			List otherlist = null;
			if(goods.getStoreIds()!=null && goods.getStoreIds().length>0) {
				List<String> arrlist = Arrays.asList(goods.getStoreIds());//新获取的关联数据集合
    			otherlist = new ArrayList(arrlist);
				for(LitemallStore oldData : oldGoodStoreList) {
					//新关联数据不为空，旧关联数据和老关联数据比较
        			boolean oldisDele = true;        			
        			for(int i=0;i<otherlist.size();i++) {
        				//新关联数据集合包含某个旧关联数据，则新关联数据集合中这个数据删掉，不需要增加或修改这个关联数据
        				if(otherlist.get(i).equals(oldData.getStoreId())) {
        					otherlist.remove(i);
        					oldisDele = false;
        				}
        			}
        			if(oldisDele) {
        				//新关联数据集合不包含某个旧的关联数据，则这个旧关联数据需要删除
        				storeService.deleteStoreGoods(oldData);
        			} 
				}
			}else {
        		//新关联数据为空，不用比较，旧关联数据全部清除
				for(LitemallStore oldData2 : oldGoodStoreList) {
					storeService.deleteStoreGoods(oldData2);
				}
    		}
    		//剩余的新关联数据保存在数据库中
			if(otherlist!=null && otherlist.size()>0) {
				for(int a=0;a<otherlist.size();a++) {
					System.out.println("----------------- arrlist.get(a) =="+otherlist.get(a));
					storegood.setStoreId(otherlist.get(a).toString());
    				storeService.addStoreGoods(storegood);
				}        				
			}        	
        }else {
        //旧数据不存在，则新数据添加
        	if(goods.getStoreIds()!=null && goods.getStoreIds().length>0) {
        		for(String storeid : goods.getStoreIds()) {
        			storegood.setStoreId(storeid);
    				storeService.addStoreGoods(storegood);
        		}
        	}
        }
        
        return ResponseUtil.ok(goods);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallGoods goods){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        goodsService.deleteById(goods.getId());
        return ResponseUtil.ok();
    }
    
    public String getSerialNumber() {
		LitemallSerialNumber serialNumber = serialNumberService.findSerialNumberByType("GOODS_ID");
		Integer number = serialNumber.getSerialNumber();
		if(number < 9999) {
			serialNumber.setSerialNumber(number + 1);
		} else {
			serialNumber.setSerialNumber(1);
		}
		int length = number.toString().length();
		String sort = "";
		if(length == 1) {
			sort = "000"+number;
		} else if(length == 2) {
			sort = "00"+number;
		} else if(length == 3) {
			sort = "0"+number;
		} else if(length == 4) {
			sort = ""+number;
		}  else {
			sort = "0000";
		}
		
		serialNumberService.updateSerialNumber(serialNumber);
		return sort;
    }
    
}
