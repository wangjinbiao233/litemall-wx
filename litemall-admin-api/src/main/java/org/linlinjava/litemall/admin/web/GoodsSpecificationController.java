package org.linlinjava.litemall.admin.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallGoodsSpecification;
import org.linlinjava.litemall.db.domain.LitemallProduct;
import org.linlinjava.litemall.db.service.LitemallGoodsSpecificationService;
import org.linlinjava.litemall.db.service.LitemallProductService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin/goods-specification")
public class GoodsSpecificationController {
    private final Log logger = LogFactory.getLog(GoodsSpecificationController.class);

    @Autowired
    private LitemallGoodsSpecificationService goodsSpecificationService;

    @Autowired
    private LitemallProductService productService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
                       Integer goodsId,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        List<LitemallGoodsSpecification> goodsSpecificationList = goodsSpecificationService.querySelective(goodsId, page, limit, sort, order);
        int total = goodsSpecificationService.countSelective(goodsId, page, limit, sort, order);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", goodsSpecificationList);

        /* 构建table rowspan/colspan 信息，用于前端合并单元格 */
        List<Map<String, Integer>> lstSpanInfo = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> spanAtRow = null; // 用于保存在某一行信息 rospan/colspan 的设置
        int gId = 0;
        int rows = 1;
        List<Map<String, Integer>> lstSpecSpanInfo = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> specSpanAtRow = null; // 用于规格名称列 保存在某一行进行 rowspan/colspan 的设置
        int specRows = 1;
        String specName = null;
        for(LitemallGoodsSpecification litemallGoodsSpecification : goodsSpecificationList) {

            Map<String, Integer> mapSpanInfo = new HashMap<String, Integer>();
            mapSpanInfo.put("rowspan", 1);
            mapSpanInfo.put("colspan", 1);
            lstSpanInfo.add(mapSpanInfo);

            // 合并规格名称
            Map<String, Integer> mapSpecSpanInfo = new HashMap<String, Integer>();
            mapSpecSpanInfo.put("rowspan", 1);
            mapSpecSpanInfo.put("colspan", 1);
            lstSpecSpanInfo.add(mapSpecSpanInfo);

            if(gId == 0){
                gId = litemallGoodsSpecification.getGoodsId();
                spanAtRow = mapSpanInfo;

                specName = litemallGoodsSpecification.getSpecification();
                specSpanAtRow = mapSpecSpanInfo;
            } else if(gId == litemallGoodsSpecification.getGoodsId()){
                rows++;
                mapSpanInfo.put("rowspan", 0);
                mapSpanInfo.put("colspan", 0);

                if(specName.equals(litemallGoodsSpecification.getSpecification())){
                    specRows++;
                    mapSpecSpanInfo.put("rowspan", 0);
                    mapSpecSpanInfo.put("colspan", 0);
                } else {
                    specSpanAtRow.put("rowspan", specRows);

                    specSpanAtRow = mapSpecSpanInfo;
                    specName = litemallGoodsSpecification.getSpecification();
                    specRows = 1;
                }
            } else {
                spanAtRow.put("rowspan", rows);

                spanAtRow = mapSpanInfo;
                gId = litemallGoodsSpecification.getGoodsId();
                rows = 1;

                specSpanAtRow.put("rowspan", specRows);

                specSpanAtRow = mapSpecSpanInfo;
                specName = litemallGoodsSpecification.getSpecification();
                specRows = 1;
            }

        }
        if(spanAtRow != null){
            spanAtRow.put("rowspan", rows);
        }
        data.put("spanInfo", lstSpanInfo);

        if(specSpanAtRow != null){
            specSpanAtRow.put("rowspan", specRows);
        }
        data.put("specSpanInfo", lstSpecSpanInfo);
        /* end */

        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallGoodsSpecification goodsSpecification){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        goodsSpecificationService.add(goodsSpecification);
        return ResponseUtil.ok(goodsSpecification);
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        if(id == null){
            return ResponseUtil.badArgument();
        }

        LitemallGoodsSpecification goodsSpecification = goodsSpecificationService.findById(id);
        return ResponseUtil.ok(goodsSpecification);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallGoodsSpecification goodsSpecification){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        goodsSpecificationService.updateById(goodsSpecification);
        return ResponseUtil.ok(goodsSpecification);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallGoodsSpecification goodsSpecification){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        goodsSpecificationService.deleteById(goodsSpecification.getId());
        return ResponseUtil.ok();
    }

    @GetMapping("/volist")
    public Object volist(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        if(id == null){
            return ResponseUtil.badArgument();
        }

        Object goodsSpecificationVoList = goodsSpecificationService.getSpecificationVoList(id);
        return ResponseUtil.ok(goodsSpecificationVoList);
    }

    @PostMapping("/save")
    public Object save(@LoginAdmin Integer adminId, @RequestBody RequestBodyForSave requestBodyForSave) {

        if(adminId == null) {
            return ResponseUtil.unlogin();
        }

        Integer goodsId = requestBodyForSave.getGoodsId();
        LitemallGoodsSpecification[] litemallGoodsSpecifications = requestBodyForSave.getGoodsSpecifications();

        List<LitemallGoodsSpecification> goodsSpecificationList = goodsSpecificationService.querySelective(goodsId, 1, 1000, null, null);
        List<LitemallGoodsSpecification> goodsSpecificationList_request = new ArrayList<LitemallGoodsSpecification>(Arrays.asList(litemallGoodsSpecifications));

        // 找出用户已删除的项
        List<LitemallGoodsSpecification> removeGoodsSpecification = new ArrayList<LitemallGoodsSpecification>();
        for(LitemallGoodsSpecification goodsSpecification : goodsSpecificationList) {
            int specificationId = goodsSpecification.getId();
            boolean isHave = false;
            for(LitemallGoodsSpecification goodsSpecification_req : goodsSpecificationList_request) {

                Integer specificationId_req = goodsSpecification_req.getId();
                if(specificationId_req != null && specificationId_req != 0 && specificationId_req == specificationId) {
                    isHave = true;
                    break;
                }
            }

            if(!isHave) {
                removeGoodsSpecification.add(goodsSpecification);
            }
        }

        if(removeGoodsSpecification.size() > 0) {
            // 进行删除操作
            for(LitemallGoodsSpecification goodsSpecification : removeGoodsSpecification) {

                //此处应该同步删除销售价格中的包含商品规格的销售价格---2018-10-19修改
                //根据goodIds查找所有的销售价格
                List<LitemallProduct> productList = productService.queryByGid(goodsSpecification.getGoodsId());
                for(LitemallProduct litemallProduct : productList){
                    Integer[] goodsSpecificationIds=litemallProduct.getGoodsSpecificationIds();
                    List<Integer> lists= Arrays.asList(goodsSpecificationIds);
                    List<Integer> aLists=new ArrayList<>(lists);
                    for(int i=0;i<aLists.size();i++){
                        if(aLists.get(i) == goodsSpecification.getId()){
                            //销售价格包含删除商品规格,此处删除销售价格中的商品规格
                            aLists.remove(i);
                            Integer[] arrayInteger=aLists.toArray(new Integer[aLists.size()]);
                            litemallProduct.setGoodsSpecificationIds(arrayInteger);
                            productService.updateById(litemallProduct);
                        }
                    }

                }


                goodsSpecificationService.deleteById(goodsSpecification.getId());


            }
        }

        // 找出用户新添加的项
        List<LitemallGoodsSpecification> addGoodsSpecification = new ArrayList<LitemallGoodsSpecification>();
        for(LitemallGoodsSpecification goodsSpecification : goodsSpecificationList_request) {
            Integer specificationId = goodsSpecification.getId();
            if(specificationId != null && specificationId != 0) {
                continue;
            } else {
                // 只处理填写了值的项
                if(StringUtils.isNotBlank(goodsSpecification.getSpecification())
                        && StringUtils.isNotBlank(goodsSpecification.getValue())) {
                    goodsSpecification.setGoodsId(goodsId);
                    addGoodsSpecification.add(goodsSpecification);
                }
            }
        }
        if(addGoodsSpecification.size() > 0) {
            // 进行新增操作
            for(LitemallGoodsSpecification goodsSpecification : addGoodsSpecification) {
                goodsSpecificationService.add(goodsSpecification);
            }

            goodsSpecificationList_request.removeAll(addGoodsSpecification);
        }

        if(goodsSpecificationList_request.size() > 0) {
            // 进行修改操作
            for(LitemallGoodsSpecification goodsSpecification : goodsSpecificationList_request) {
                // 如果规格名称和规格值都有填写的话则更新该规格信息
                if(StringUtils.isNotBlank(goodsSpecification.getSpecification())
                        && StringUtils.isNotBlank(goodsSpecification.getValue())
                        && goodsSpecification.getId() != 0) {
                    goodsSpecificationService.updateById(goodsSpecification);
                }
            }
        }

        return ResponseUtil.ok();
    }

    static class RequestBodyForSave {
        private Integer goodsId;
        private LitemallGoodsSpecification[] goodsSpecifications;

        public Integer getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Integer goodsId) {
            this.goodsId = goodsId;
        }

        public LitemallGoodsSpecification[] getGoodsSpecifications() {
            return goodsSpecifications;
        }

        public void setGoodsSpecifications(LitemallGoodsSpecification[] goodsSpecifications) {
            this.goodsSpecifications = goodsSpecifications;
        }
    }

}
