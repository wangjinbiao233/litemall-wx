package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallGoodsSpecificationMapper;
import org.linlinjava.litemall.db.domain.LitemallGoodsSpecification;
import org.linlinjava.litemall.db.domain.LitemallGoodsSpecificationExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallGoodsSpecificationService {
    @Resource
    private LitemallGoodsSpecificationMapper goodsSpecificationMapper;

    public List<LitemallGoodsSpecification> queryByGid(Integer id) {
        LitemallGoodsSpecificationExample example = new LitemallGoodsSpecificationExample();
        example.or().andGoodsIdEqualTo(id).andDeletedEqualTo(false);
        return goodsSpecificationMapper.selectByExample(example);
    }

    public LitemallGoodsSpecification findById(Integer id) {
        return goodsSpecificationMapper.selectByPrimaryKey(id);
    }

    public List<LitemallGoodsSpecification> querySelective(Integer goodsId, Integer page, Integer size, String sort, String order) {
        LitemallGoodsSpecificationExample example = new LitemallGoodsSpecificationExample();
        LitemallGoodsSpecificationExample.Criteria criteria = example.createCriteria();

        if(goodsId != null){
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);
        //example.setOrderByClause("goods_id");

        PageHelper.startPage(page, size);
        return goodsSpecificationMapper.selectByExample(example);
    }

    public int countSelective(Integer goodsId, Integer page, Integer size, String sort, String order) {
        LitemallGoodsSpecificationExample example = new LitemallGoodsSpecificationExample();
        LitemallGoodsSpecificationExample.Criteria criteria = example.createCriteria();

        if(goodsId != null){
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);

        return (int)goodsSpecificationMapper.countByExample(example);
    }

    public void updateById(LitemallGoodsSpecification goodsSpecification) {
        goodsSpecificationMapper.updateByPrimaryKeySelective(goodsSpecification);
    }

    public void deleteById(Integer id) {
        LitemallGoodsSpecification goodsSpecification = goodsSpecificationMapper.selectByPrimaryKey(id);
        if(goodsSpecification == null){
            return;
        }
        goodsSpecification.setDeleted(true);
        goodsSpecificationMapper.updateByPrimaryKey(goodsSpecification);
    }

    public void add(LitemallGoodsSpecification goodsSpecification) {
        goodsSpecificationMapper.insertSelective(goodsSpecification);
    }

    public Integer[] queryIdsByGid(Integer goodsId) {
        List<LitemallGoodsSpecification> goodsSpecificationList = queryByGid(goodsId);
        Integer[] ids = new Integer[goodsSpecificationList.size()];
        for(int i = 0; i < ids.length; i++){
            ids[i] = goodsSpecificationList.get(i).getId();
        }
        return ids;
    }

    public List<Map<String, Object>> querySpecGroupByGid(Integer goodsId) {
        List<Map<String, Object>> lstSpecGroup = new ArrayList<Map<String, Object>>();

        List<LitemallGoodsSpecification> goodsSpecificationList = queryByGid(goodsId);
        for(LitemallGoodsSpecification litemallGoodsSpecification : goodsSpecificationList) {
            String name = litemallGoodsSpecification.getSpecification();

            int id = litemallGoodsSpecification.getId();
            String value = litemallGoodsSpecification.getValue();

            Map<String, Object> mapSpecGroup = null;
            for(int i = 0; i < lstSpecGroup.size(); i++) {

                if(lstSpecGroup.get(i).get("label").equals(name)) {
                    mapSpecGroup = lstSpecGroup.get(i);
                    break;
                }
            }

            Map<String, Object> mapSpecGroupItem = new HashMap<String, Object>();
            mapSpecGroupItem.put("label", value);
            mapSpecGroupItem.put("value", id);
            mapSpecGroupItem.put("disabled", false);
            if(mapSpecGroup == null) {
                List<Map<String, Object>> lstSpecGroupItem = new ArrayList<Map<String, Object>>();
                lstSpecGroupItem.add(mapSpecGroupItem);

                mapSpecGroup = new HashMap<String, Object>();
                mapSpecGroup.put("label", name);
                mapSpecGroup.put("options", lstSpecGroupItem);

                lstSpecGroup.add(mapSpecGroup);
            } else {
                List<Map<String, Object>> lstSpecGroupItem = (List<Map<String, Object>>)mapSpecGroup.get("options");
                lstSpecGroupItem.add(mapSpecGroupItem);
            }
        }

        return lstSpecGroup;
    }
    
    public Object querySpecGroupByIds(Integer[] ids) {
    	
    	List<LitemallGoodsSpecification> goodsSpecificationList = new ArrayList<LitemallGoodsSpecification>();
        for(Integer id : ids) {
        	LitemallGoodsSpecification litemallGoodsSpecification = goodsSpecificationMapper.selectByPrimaryKey(id);
        	goodsSpecificationList.add(litemallGoodsSpecification);
        }

         Map<String, VO> map = new HashMap<>();
         List<VO> specificationVoList = new ArrayList<>();

         for(LitemallGoodsSpecification goodsSpecification : goodsSpecificationList){
             String specification = goodsSpecification.getSpecification();
             VO goodsSpecificationVo = map.get(specification);
             if(goodsSpecificationVo == null){
                 goodsSpecificationVo = new VO();
                 goodsSpecificationVo.setName(specification);
                 List<LitemallGoodsSpecification> valueList = new ArrayList<>();
                 valueList.add(goodsSpecification);
                 goodsSpecificationVo.setValueList(valueList);
                 map.put(specification, goodsSpecificationVo);
                 specificationVoList.add(goodsSpecificationVo);
             }
             else{
                 List<LitemallGoodsSpecification> valueList = goodsSpecificationVo.getValueList();
                 valueList.add(goodsSpecification);
             }
         }

         return specificationVoList;
         
    }
    

    private class VO {
        private String name;
        private List<LitemallGoodsSpecification> valueList;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<LitemallGoodsSpecification> getValueList() {
            return valueList;
        }

        public void setValueList(List<LitemallGoodsSpecification> valueList) {
            this.valueList = valueList;
        }
    }

    /**
     * [
     *  {
     *      name: '',
     *      valueList: [ {}, {}]
     *  },
     *  {
     *      name: '',
     *      valueList: [ {}, {}]
     *  }
     *  ]
     *
     * @param id
     * @return
     */
    public Object getSpecificationVoList(Integer id) {
        List<LitemallGoodsSpecification> goodsSpecificationList = queryByGid(id);

        Map<String, VO> map = new HashMap<>();
        List<VO> specificationVoList = new ArrayList<>();

        for(LitemallGoodsSpecification goodsSpecification : goodsSpecificationList){
            String specification = goodsSpecification.getSpecification();
            VO goodsSpecificationVo = map.get(specification);
            if(goodsSpecificationVo == null){
                goodsSpecificationVo = new VO();
                goodsSpecificationVo.setName(specification);
                List<LitemallGoodsSpecification> valueList = new ArrayList<>();
                valueList.add(goodsSpecification);
                goodsSpecificationVo.setValueList(valueList);
                map.put(specification, goodsSpecificationVo);
                specificationVoList.add(goodsSpecificationVo);
            }
            else{
                List<LitemallGoodsSpecification> valueList = goodsSpecificationVo.getValueList();
                valueList.add(goodsSpecification);
            }
        }

        return specificationVoList;
    }

}
