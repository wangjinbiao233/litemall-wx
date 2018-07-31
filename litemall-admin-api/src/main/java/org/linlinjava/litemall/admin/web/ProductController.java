package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallProduct;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallGoodsSpecificationService;
import org.linlinjava.litemall.db.service.LitemallProductService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/admin/product")
public class ProductController {
    private final Log logger = LogFactory.getLog(ProductController.class);

    @Autowired
    private LitemallProductService productService;
    @Autowired
    private LitemallGoodsService goodsService;
    @Autowired
    private LitemallGoodsSpecificationService goodsSpecificationService;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
                       Integer goodsId,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        List<LitemallProduct> productList = productService.querySelective(goodsId, page, limit, sort, order);
        int total = productService.countSelective(goodsId, page, limit, sort, order);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", productList);

        /* 构建table rowspan/colspan 信息，用于前端合并单元格 */
        List<Map<String, Integer>> lstSpanInfo = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> spanAtRow = null; // 用于保存在某一行信息 rospan/colspan 的设置
        int gId = 0;
        int rows = 1;
        for(LitemallProduct litemallProduct : productList) {

            Map<String, Integer> mapSpanInfo = new HashMap<String, Integer>();
            mapSpanInfo.put("rowspan", 1);
            mapSpanInfo.put("colspan", 1);
            lstSpanInfo.add(mapSpanInfo);

            if(gId == 0){
                gId = litemallProduct.getGoodsId();
                spanAtRow = mapSpanInfo;
            } else if(gId == litemallProduct.getGoodsId()){
                rows++;
                mapSpanInfo.put("rowspan", 0);
                mapSpanInfo.put("colspan", 0);
            } else {
                spanAtRow.put("rowspan", rows);

                spanAtRow = mapSpanInfo;
                gId = litemallProduct.getGoodsId();
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

    /**
     *
     * @param adminId
     * @param litemallProduct
     * @return
     */
    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallProduct litemallProduct){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        Integer goodsId = litemallProduct.getGoodsId();
        if(goodsId == null){
            return ResponseUtil.badArgument();
        }

//        LitemallGoods goods = goodsService.findById(goodsId);
//        if(goods == null){
//            return ResponseUtil.badArgumentValue();
//        }
//
//        List<LitemallProduct> productList = productService.queryByGid(goodsId);
//        if(productList.size() != 0){
//            return ResponseUtil.badArgumentValue();
//        }
//
//        Integer[] goodsSpecificationIds = goodsSpecificationService.queryIdsByGid(goodsId);
//        if(goodsSpecificationIds.length == 0) {
//            return ResponseUtil.serious();
//        }

        LitemallProduct product = new LitemallProduct();
        product.setGoodsId(goodsId);
        product.setGoodsNumber(litemallProduct.getGoodsNumber());
        product.setRetailPrice(litemallProduct.getRetailPrice());
        product.setGoodsSpecificationIds(litemallProduct.getGoodsSpecificationIds());
        productService.add(product);

        return ResponseUtil.ok();
    }

    /**
     * 获取商品的规格列表 用于商品添加时候作为'规格'下拉框的项
     * @author add by pengxb @2018-05-14 11:17
     */
    @GetMapping("/listSpecGroup")
    public Object listSpecGroup(@LoginAdmin Integer adminId, Integer goodsId) {
        if(adminId == null) {
            return ResponseUtil.unlogin();
        }

        if(goodsId == 0) {
            return ResponseUtil.badArgumentValue();
        }

        List<Map<String, Object>> lstSpecGroup = goodsSpecificationService.querySpecGroupByGid(goodsId);
        Map<String, Object> mapRs = new HashMap<String, Object>();
        mapRs.put("items", lstSpecGroup);

        return ResponseUtil.ok(mapRs);
    }

    /**
     * 获取已经存在的货品的规格列表
     * @author add by pengxb @2018-05-14 11:17
     */
    @GetMapping("/listProductSpec")
    public Object listProductSpec(@LoginAdmin Integer adminId, Integer goodsId) {
        if(adminId == null) {
            return ResponseUtil.unlogin();
        }
        if(goodsId == 0){
            return ResponseUtil.badArgumentValue();
        }

        List<LitemallProduct> lstProduct = productService.queryByGid(goodsId);
        Map<String, Object> mapRs = new HashMap<String, Object>();

        if(lstProduct != null) {
            String[] arrProductSpecName = new String[lstProduct.size()];
            Integer[][] arrProductSpec = new Integer[lstProduct.size()][];
            for (int i = 0; i < lstProduct.size(); i++) {
                LitemallProduct litemallProduct = lstProduct.get(i);
                arrProductSpec[i] = litemallProduct.getGoodsSpecificationIds();

                arrProductSpecName[i] = litemallProduct.getGoodsSpec();
            }
            mapRs.put("items", arrProductSpec);
            mapRs.put("itemsName", arrProductSpecName);
        }

        return ResponseUtil.ok(mapRs);
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        if(id == null){
            return ResponseUtil.badArgument();
        }

        LitemallProduct product = productService.findById(id);
        return ResponseUtil.ok(product);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallProduct product){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        productService.updateById(product);
        return ResponseUtil.ok(product);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallProduct product){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        productService.deleteById(product.getId());
        return ResponseUtil.ok();
    }

    @PostMapping("/save")
    public Object save(@LoginAdmin Integer adminId,  @RequestBody RequestBodyForSave requestBodyForSave) {

        if(adminId == null) {
            return ResponseUtil.unlogin();
        }

        Integer goodsId = requestBodyForSave.getGoodsId();
        LitemallProduct[] products = requestBodyForSave.getProducts();

        List<LitemallProduct> productList = productService.querySelective(goodsId, 1, 1000, null, null);
        List<LitemallProduct> productList_req = new ArrayList<LitemallProduct>(Arrays.asList(products));

        // 找出已被用户删除的项
        List<LitemallProduct> removeLitemallProduct = new ArrayList<LitemallProduct>();
        for(LitemallProduct litemallProduct : productList) {
            int productId = litemallProduct.getId();
            boolean isHave = false;

            for(LitemallProduct litemallProduct_req : productList_req) {
                Integer productId_req = litemallProduct_req.getId();

                if(productId_req != null && productId_req != 0 && productId_req == productId) {
                    isHave = true;
                    break;
                }
            }

            if(!isHave) {
                removeLitemallProduct.add(litemallProduct);
            }
        }
        if(removeLitemallProduct.size() > 0) {
            // 进行删除操作
            for(LitemallProduct litemallProduct : removeLitemallProduct) {
                productService.deleteById(litemallProduct.getId());
            }

            productList.removeAll(removeLitemallProduct);
        }

        // 找出用户新增的项
        List<LitemallProduct> addLitemallProduct = new ArrayList<LitemallProduct>();
        for(LitemallProduct litemallProduct : productList_req) {
            Integer productId = litemallProduct.getId();

            if(productId != null && productId != 0) {
                continue;
            } else {
                // 只处理选了规格的项
                Integer[] specificationIds = litemallProduct.getGoodsSpecificationIds();
                if(specificationIds != null && specificationIds.length > 0) {
                    litemallProduct.setGoodsId(goodsId);
                    addLitemallProduct.add(litemallProduct);
                }
            }
        }
        if(addLitemallProduct.size() > 0){
            // 进行新增操作
            for(LitemallProduct litemallProduct : addLitemallProduct) {
                productService.add(litemallProduct);
            }

            productList_req.removeAll(addLitemallProduct);
        }

        if(productList_req.size() > 0) {
            // 进行更新操作
            for(LitemallProduct litemallProduct : productList_req) {
                productService.updateById(litemallProduct);
            }
        }


        return ResponseUtil.ok();
    }

    static class RequestBodyForSave {
        private Integer goodsId;
        private LitemallProduct[] products;

        public Integer getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Integer goodsId) {
            this.goodsId = goodsId;
        }

        public LitemallProduct[] getProducts() {
            return products;
        }

        public void setProducts(LitemallProduct[] products) {
            this.products = products;
        }
    }

}
