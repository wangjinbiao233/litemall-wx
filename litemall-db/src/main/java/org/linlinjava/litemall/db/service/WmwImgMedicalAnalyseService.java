package org.linlinjava.litemall.db.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.WmwImgMedicalAnalyseMapper;
import org.linlinjava.litemall.db.domain.WmwImgMedicalAnalyse;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class WmwImgMedicalAnalyseService {
	@Resource
	private WmwImgMedicalAnalyseMapper wmwImgMedicalAnalyseMapper;

	public WmwImgMedicalAnalyse selectWmwByPrimaryKey(Integer analyseId) {
		return wmwImgMedicalAnalyseMapper.selectWmwByPrimaryKey(analyseId);

	}
	public List<Map<String,Object>> selectWmwImgMedicalAnalyList(Map<String,Object> map,Integer page, Integer size, String sort, String order) {
		PageHelper.startPage(page, size);
		return wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyList(map);
	}
	
	public List<Map<String,Object>> selectWmwImgMedicalAnalyDetailList(Map<String,Object> map,Integer page, Integer size, String sort, String order) {
		PageHelper.startPage(page, size);
		return wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyDetailList(map);
	}
	
   public int countWmwImgMedicalAnalyList(Map<String,Object> map, Integer page, Integer size, String sort, String order) {
    	return wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyCount(map);
   }
   
   public int countWmwImgMedicalAnalyDetailList(Map<String,Object> map, Integer page, Integer size, String sort, String order) {
	   return wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyDetailCount(map);
   }

}
