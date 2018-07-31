package org.linlinjava.litemall.admin.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallGoodsAttribute;
import org.linlinjava.litemall.db.service.LitemallGoodsAttributeService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin/goods-attribute")
public class GoodsAttributeController {
    private final Log logger = LogFactory.getLog(GoodsAttributeController.class);

    @Autowired
    private LitemallGoodsAttributeService goodsAttributeService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
                       Integer goodsId,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        List<LitemallGoodsAttribute> goodsAttributeList = goodsAttributeService.querySelective(goodsId, page, limit, sort, order);
        int total = goodsAttributeService.countSelective(goodsId, page, limit, sort, order);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", goodsAttributeList);

        /* 构建table rowspan/colspan 信息，用于前端合并单元格 */
        List<Map<String, Integer>> lstSpanInfo = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> spanAtRow = null; // 用于保存在某一行信息 rospan/colspan 的设置
        int gId = 0;
        int rows = 1;
        for(LitemallGoodsAttribute litemallGoodsAttribute : goodsAttributeList) {

            Map<String, Integer> mapSpanInfo = new HashMap<String, Integer>();
            mapSpanInfo.put("rowspan", 1);
            mapSpanInfo.put("colspan", 1);
            lstSpanInfo.add(mapSpanInfo);

            if(gId == 0){
               gId = litemallGoodsAttribute.getGoodsId();
                spanAtRow = mapSpanInfo;
           } else if(gId == litemallGoodsAttribute.getGoodsId()){
                rows++;
                mapSpanInfo.put("rowspan", 0);
                mapSpanInfo.put("colspan", 0);
           } else {
                spanAtRow.put("rowspan", rows);

                spanAtRow = mapSpanInfo;
                gId = litemallGoodsAttribute.getGoodsId();
                rows = 1;
           }
        }
        if(spanAtRow != null){
            spanAtRow.put("rowspan", rows);
        }
        data.put("spanInfo", lstSpanInfo);
        /* end */


        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallGoodsAttribute goodsAttribute){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        goodsAttributeService.add(goodsAttribute);
        return ResponseUtil.ok(goodsAttribute);
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        if(id == null){
            return ResponseUtil.badArgument();
        }

        LitemallGoodsAttribute goodsAttribute = goodsAttributeService.findById(id);
        return ResponseUtil.ok(goodsAttribute);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallGoodsAttribute goodsAttribute){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        goodsAttributeService.updateById(goodsAttribute);
        return ResponseUtil.ok(goodsAttribute);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallGoodsAttribute goodsAttribute){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        goodsAttributeService.deleteById(goodsAttribute.getId());
        return ResponseUtil.ok();
    }

    @PostMapping("/save")
    public Object save(@LoginAdmin Integer adminId, @RequestBody RequestBodyForSave requestBodyForSave) {

        if(adminId == null) {
            return ResponseUtil.unlogin();
        }

        Integer goodsId = requestBodyForSave.getGoodsId();
        LitemallGoodsAttribute[] goodsAttributes = requestBodyForSave.getGoodsAttributes();

        List<LitemallGoodsAttribute> goodsAttributeList = goodsAttributeService.querySelective(goodsId, 1, 1000, null, null);
        // 找出用户已删除掉的
        List<LitemallGoodsAttribute> removeGoodsAttributeList = new ArrayList<LitemallGoodsAttribute>();
        for(LitemallGoodsAttribute litemallGoodsAttribute : goodsAttributeList) {
            int attributeId = litemallGoodsAttribute.getId();
            boolean isHave = false;

            for(int i = 0; i < goodsAttributes.length; i++) {

                LitemallGoodsAttribute litemallGoodsAttribute1 = goodsAttributes[i];
                Integer attributeId1 = litemallGoodsAttribute1.getId();

                if(attributeId1 != null && attributeId1 != 0 && attributeId == attributeId1) {
                    isHave = true;
                    break;
                }
            }

            if(!isHave) {
                removeGoodsAttributeList.add(litemallGoodsAttribute);
            }
        }
        if(removeGoodsAttributeList.size() > 0){
            goodsAttributeList.removeAll(removeGoodsAttributeList);

            // 进行删除操作
            for(LitemallGoodsAttribute litemallGoodsAttribute : removeGoodsAttributeList) {
                goodsAttributeService.deleteById(litemallGoodsAttribute.getId());
            }
        }

        List<LitemallGoodsAttribute> updateGoodsAttributeList = new ArrayList<LitemallGoodsAttribute>(Arrays.asList(goodsAttributes));

        // 找出用户新增的
        List<LitemallGoodsAttribute> addGoodsAttributeList = new ArrayList<LitemallGoodsAttribute>();
        for(LitemallGoodsAttribute litemallGoodsAttribute : updateGoodsAttributeList) {
            Integer attributeId = litemallGoodsAttribute.getId();
            if(attributeId != null && attributeId != 0) {
                continue;
            } else {
                // 只处理填写了值的项
                if(StringUtils.isNotBlank(litemallGoodsAttribute.getAttribute())
                        && StringUtils.isNotBlank(litemallGoodsAttribute.getValue())) {
                    litemallGoodsAttribute.setGoodsId(goodsId);
                    addGoodsAttributeList.add(litemallGoodsAttribute);
                }
            }
        }

        // 找出被修改的
        if(addGoodsAttributeList.size() > 0) {
            // 进行新增操作
            for(LitemallGoodsAttribute litemallGoodsAttribute : addGoodsAttributeList) {
                goodsAttributeService.add(litemallGoodsAttribute);
            }

            updateGoodsAttributeList.removeAll(addGoodsAttributeList);
        }
        // 进行更新操作
        if(updateGoodsAttributeList.size() > 0) {
            for(LitemallGoodsAttribute litemallGoodsAttribute : updateGoodsAttributeList) {
                Integer attributeId = litemallGoodsAttribute.getId();

                if(attributeId != null && attributeId != 0) {
                    goodsAttributeService.updateById(litemallGoodsAttribute);
                }
            }
        }

        return ResponseUtil.ok();
    }

    static class RequestBodyForSave {

        private Integer goodsId;
        private LitemallGoodsAttribute[] goodsAttributes;

        public Integer getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Integer goodsId) {
            this.goodsId = goodsId;
        }

        public LitemallGoodsAttribute[] getGoodsAttributes() {
            return goodsAttributes;
        }

        public void setGoodsAttributes(LitemallGoodsAttribute[] goodsAttributes) {
            this.goodsAttributes = goodsAttributes;
        }
    }

}
