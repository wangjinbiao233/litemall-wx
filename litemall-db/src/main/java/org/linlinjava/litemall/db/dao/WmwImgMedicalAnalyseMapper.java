package org.linlinjava.litemall.db.dao;

import java.util.List;
import java.util.Map;

import org.linlinjava.litemall.db.domain.WmwImgMedicalAnalyse;

public interface WmwImgMedicalAnalyseMapper {
    int deleteByPrimaryKey(Integer analyseId);

    int insert(WmwImgMedicalAnalyse record);

    int insertSelective(WmwImgMedicalAnalyse record);

    WmwImgMedicalAnalyse selectWmwByPrimaryKey(Integer analyseId);
    
    List<Map<String,Object>> selectWmwImgMedicalAnalyList(Map<String,Object> map);
    
    List<Map<String,Object>> selectWmwImgMedicalAnalyDetailList(Map<String,Object> map);
    
    int selectWmwImgMedicalAnalyCount(Map<String,Object> map);
    
    int selectWmwImgMedicalAnalyDetailCount(Map<String,Object> map);
    
    int updateByPrimaryKeySelective(WmwImgMedicalAnalyse record);

    int updateByPrimaryKey(WmwImgMedicalAnalyse record);
}