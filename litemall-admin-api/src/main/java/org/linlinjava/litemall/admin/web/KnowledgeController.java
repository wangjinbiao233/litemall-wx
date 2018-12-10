package org.linlinjava.litemall.admin.web;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.LitemallAdminService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallKnowledgeService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/knowledge")
public class KnowledgeController {
	private final Log logger = LogFactory.getLog(KnowledgeController.class);
	
	@Autowired
    private LitemallKnowledgeService knowledgeService;
    @Autowired
    private LitemallGoodsService goodsService;
    @Autowired
    private LitemallAdminService adminService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST) 
    public Object list(@LoginAdmin Integer adminId,
    					LitemallKnowledge knowledge, 
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        
        List<LitemallKnowledge> list=knowledgeService.selectKnowledgeList(knowledge, page, limit, sort, order);
        int total = knowledgeService.countKnowledge(knowledge);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", list);
        return ResponseUtil.ok(data);
    }
	
    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallKnowledge knowledge){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        LitemallAdmin admin = adminService.findById(adminId);
        if(admin != null){

            knowledgeService.insert(knowledge);

            if (knowledge.getGoodsId() != null && knowledge.getGoodsId().length > 0) {
                for (String googsId : knowledge.getGoodsId()) {
                    LitemallKnowledgeGoods lkg = new LitemallKnowledgeGoods();
                    lkg.setCreator(admin.getUsername());
                    lkg.setFkGoodsId(googsId);
                    lkg.setFkKnowledgeId(knowledge.getId());
                    lkg.setCreateDate(new Date());

                    knowledgeService.addLitemallKnowledgeGoods(lkg);
                }
            }
        }


        return ResponseUtil.ok(knowledge);
    }
	
    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallKnowledge knowledge){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        LitemallAdmin admin = adminService.findById(adminId);
        if(admin != null){
            knowledgeService.updateById(knowledge);
            List<LitemallGoods> list = goodsService.queryLitemallGoodsByKnowledgeId(knowledge.getId());

            if (knowledge.getGoodsId() != null && knowledge.getGoodsId().length > 0) {
                //处理新增的商品
                for (String googsId : knowledge.getGoodsId()) {
                    boolean has = true;
                    for (LitemallGoods goods: list) {
                        String gId = goods.getId().toString();
                        if(googsId.equals(gId)){
                            has = false;
                            break;
                        }
                    }
                    if(has){
                        LitemallKnowledgeGoods lkg = new LitemallKnowledgeGoods();
                        lkg.setCreator(admin.getUsername());
                        lkg.setFkGoodsId(googsId);
                        lkg.setFkKnowledgeId(knowledge.getId());
                        lkg.setCreateDate(new Date());

                        knowledgeService.addLitemallKnowledgeGoods(lkg);
                    }

                }
                //处理删除的商品
                for (LitemallGoods goods: list) {
                    String gId = goods.getId().toString();
                    boolean del = true;
                    for (String googsId : knowledge.getGoodsId()) {
                        if(gId.equals(googsId)){
                            del = false;
                            break;
                        }
                    }
                    if(del){
                        knowledgeService.deleteLitemallKnowledgeGoods(goods.getId().toString(), knowledge.getId());
                    }
                }
            } else {
                knowledgeService.deleteLitemallKnowledgeGoods(null ,knowledge.getId());
            }
        }

        return ResponseUtil.ok(knowledge);
    }
    
	@PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallKnowledge knowledge){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        knowledgeService.deleteById(knowledge.getId());
        knowledgeService.deleteLitemallKnowledgeGoods(null ,knowledge.getId());
        return ResponseUtil.ok();
    }
	
	@GetMapping("/getKCategory")
	public Object getKCategory() {
		Map<String, Object> data = new HashMap<>();
		List<LitemallKCategory> list = knowledgeService.getKCategory();
		Map<Integer,String> map = new HashMap<>();
		for (LitemallKCategory litemallKCategory : list) {
			map.put(litemallKCategory.getId(), litemallKCategory.getName());
		}
		data.put("kCategory", list);
		data.put("kCategoryMap", map);
		
		return ResponseUtil.ok(data);
	}


    /**
     * 查询商品
     * @return
     */
    @GetMapping("/getGoodSn")
	public Object getGoodSn(){
        Map<String,Object> resMap = new HashMap<String,Object>();
        List<LitemallGoods> list = goodsService.selectGoodsList();
        if(list != null && list.size() > 0)
            resMap.put("items",list);
        else
            resMap.put("items",new ArrayList<LitemallGoods>());

        return ResponseUtil.ok(resMap);
    }

    /**
     * 查询知识绑定的商品
     * @param adminId
     * @param knowleId
     * @return
     */
    @GetMapping("getListKnowledgeGoods")
    public Object getListKnowledgeGoods(@LoginAdmin Integer adminId, Integer knowleId) {

        if (adminId == null) {
            return ResponseUtil.unlogin();
        }

        if(knowleId == null){
            return ResponseUtil.fail402();
        }

        List<LitemallGoods> list = goodsService.queryLitemallGoodsByKnowledgeId(knowleId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("items", list);

        return ResponseUtil.ok(data);
    }
	
	
}
