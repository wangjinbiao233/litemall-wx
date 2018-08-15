package org.linlinjava.litemall.db.service;

import java.text.NumberFormat;
import java.util.ArrayList;
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
		List<Map<String,Object>> medicalAnalyList = wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyList(map);
		int total = wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyCount(map);
		List<Map<String,Object>> resMedicalAnalyList = new ArrayList<Map<String,Object>>();
		if(medicalAnalyList.size() > 0) {
			for(Map<String,Object> res1Map :medicalAnalyList) {
				double score01 = (double)(res1Map.get("sco0")!=null?res1Map.get("sco0"):0.00);		
				double score11 = (double)(res1Map.get("sco1")!=null?res1Map.get("sco1"):0.00);	
				double score21 = (double)(res1Map.get("sco2")!=null?res1Map.get("sco2"):0.00);		
				double score31 = (double)(res1Map.get("sco3")!=null?res1Map.get("sco3"):0.00);	
				double score41 = (double)(res1Map.get("sco4")!=null?res1Map.get("sco4"):0.00);		
				int score51 = (int)(res1Map.get("sco5")!=null?res1Map.get("sco5"):0);		
				double score61 = (double)(res1Map.get("sco6")!=null?res1Map.get("sco6"):0.00);	
				double score71 = (double)(res1Map.get("sco7")!=null?res1Map.get("sco7"):0.00);		
				double score81 = (double)(res1Map.get("sco8")!=null?res1Map.get("sco8"):0.00);	
				double score91 = (double)(res1Map.get("sco9")!=null?res1Map.get("sco9"):0.00);		
				double score101 = (double)(res1Map.get("sco10")!=null?res1Map.get("sco10"):0.00);		
				double score111 = (double)(res1Map.get("sco11")!=null?res1Map.get("sco11"):0.00);		
				double score121 = (double)(res1Map.get("sco12")!=null?res1Map.get("sco12"):0.00);		
				double score131 = (double)(res1Map.get("sco13")!=null?res1Map.get("sco13"):0.00);		
				double score141 = (double)(res1Map.get("sco14")!=null?res1Map.get("sco14"):0.00);		
				int cnt0 = 0;
				int cnt1 = 0;
				int cnt2 = 0;
				int cnt3 = 0;
				int cnt4 = 0;
				int cnt5 = 0;
				int cnt6 = 0;
				int cnt7 = 0;
				int cnt8 = 0;
				int cnt9 = 0;
				int cnt10 = 0;
				int cnt11 = 0;
				int cnt12= 0;
				int cnt13= 0;
				int cnt14= 0;
				for(Map<String,Object> res2Map :medicalAnalyList) {
					double score02 = (double)(res2Map.get("sco0")!=null?res2Map.get("sco0"):0.00);		
					double score12 = (double)(res2Map.get("sco1")!=null?res2Map.get("sco1"):0.00);			
					double score22 = (double)(res2Map.get("sco2")!=null?res2Map.get("sco2"):0.00);			
					double score32 = (double)(res2Map.get("sco3")!=null?res2Map.get("sco3"):0.00);			
					double score42 = (double)(res2Map.get("sco4")!=null?res2Map.get("sco4"):0.00);			
					int score52 = (int)(res2Map.get("sco5")!=null?res2Map.get("sco5"):0);			
					double score62 = (double)(res2Map.get("sco6")!=null?res2Map.get("sco6"):0.00);			
					double score72 = (double)(res2Map.get("sco7")!=null?res2Map.get("sco7"):0.00);			
					double score82 = (double)(res2Map.get("sco8")!=null?res2Map.get("sco8"):0.00);			
					double score92 = (double)(res2Map.get("sco9")!=null?res2Map.get("sco9"):0.00);			
					double score102 = (double)(res2Map.get("sco10")!=null?res2Map.get("sco10"):0.00);			
					double score112 = (double)(res2Map.get("sco11")!=null?res2Map.get("sco11"):0.00);			
					double score122 = (double)(res2Map.get("sco12")!=null?res2Map.get("sco12"):0.00);			
					double score132 = (double)(res2Map.get("sco13")!=null?res2Map.get("sco13"):0.00);			
					double score142 = (double)(res2Map.get("sco14")!=null?res2Map.get("sco14"):0.00);			
					
					if(new Double(score01).compareTo(new Double(score02))< 0){
						cnt0 ++;						
					}
					if(new Double(score11).compareTo(new Double(score12))< 0){
						cnt1 ++;						
					}
					if(new Double(score21).compareTo(new Double(score22))< 0){
						cnt2 ++;						
					}
					if(new Double(score31).compareTo(new Double(score32))< 0){
						cnt3 ++;						
					}
					if(new Double(score41).compareTo(new Double(score42))< 0){
						cnt4 ++;						
					}
					if(new Double(score51).compareTo(new Double(score52))< 0){
						cnt5 ++;						
					}
					if(new Double(score61).compareTo(new Double(score62))< 0){
						cnt6 ++;						
					}
					if(new Double(score71).compareTo(new Double(score72))< 0){
						cnt7 ++;						
					}
					if(new Double(score81).compareTo(new Double(score82))< 0){
						cnt8 ++;						
					}
					if(new Double(score91).compareTo(new Double(score92))< 0){
						cnt9 ++;						
					}
					if(new Double(score101).compareTo(new Double(score102))< 0){
						cnt10 ++;						
					}
					if(new Double(score111).compareTo(new Double(score112))< 0){
						cnt11 ++;						
					}
					if(new Double(score121).compareTo(new Double(score122))< 0){
						cnt12 ++;						
					}
					if(new Double(score131).compareTo(new Double(score132))< 0){
						cnt13 ++;						
					}
					if(new Double(score141).compareTo(new Double(score142))< 0){
						cnt14 ++;						
					}
				}
				res1Map.put("score0jibai", accuracy(cnt0,total,0));
				res1Map.put("score1jibai", accuracy(cnt1,total,0));
				res1Map.put("score2jibai", accuracy(cnt2,total,0));
				res1Map.put("score3jibai", accuracy(cnt3,total,0));
				res1Map.put("score4jibai", accuracy(cnt4,total,0));
				res1Map.put("score5jibai", accuracy(cnt5,total,0));
				res1Map.put("score6jibai", accuracy(cnt6,total,0));
				res1Map.put("score7jibai", accuracy(cnt7,total,0));
				res1Map.put("score8jibai", accuracy(cnt8,total,0));
				res1Map.put("score9jibai", accuracy(cnt9,total,0));
				res1Map.put("score10jibai", accuracy(cnt10,total,0));
				res1Map.put("score11jibai", accuracy(cnt11,total,0));
				res1Map.put("score12jibai", accuracy(cnt12,total,0));
				res1Map.put("score13jibai", accuracy(cnt13,total,0));
				res1Map.put("score14jibai", accuracy(cnt14,total,0));
				resMedicalAnalyList.add(res1Map);
			}
		}
		
		return resMedicalAnalyList;
	}
	
	public List<Map<String,Object>> selectWmwImgMedicalAnalyDetailList(Map<String,Object> map,Integer page, Integer size, String sort, String order) {
		PageHelper.startPage(page, size);
		List<Map<String,Object>> medicalAnalyList = wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyDetailList(map);
		int total = wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyCount(map);
		List<Map<String,Object>> resMedicalAnalyList = new ArrayList<Map<String,Object>>();
		if(medicalAnalyList.size() > 0) {
			for(Map<String,Object> res1Map :medicalAnalyList) {
				double score01 = (double)(res1Map.get("sco0")!=null?res1Map.get("sco0"):0.00);		
				double score11 = (double)(res1Map.get("sco1")!=null?res1Map.get("sco1"):0.00);	
				double score21 = (double)(res1Map.get("sco2")!=null?res1Map.get("sco2"):0.00);		
				double score31 = (double)(res1Map.get("sco3")!=null?res1Map.get("sco3"):0.00);	
				double score41 = (double)(res1Map.get("sco4")!=null?res1Map.get("sco4"):0.00);		
				int score51 = (int)(res1Map.get("sco5")!=null?res1Map.get("sco5"):0);		
				double score61 = (double)(res1Map.get("sco6")!=null?res1Map.get("sco6"):0.00);	
				double score71 = (double)(res1Map.get("sco7")!=null?res1Map.get("sco7"):0.00);		
				double score81 = (double)(res1Map.get("sco8")!=null?res1Map.get("sco8"):0.00);	
				double score91 = (double)(res1Map.get("sco9")!=null?res1Map.get("sco9"):0.00);		
				double score101 = (double)(res1Map.get("sco10")!=null?res1Map.get("sco10"):0.00);		
				double score111 = (double)(res1Map.get("sco11")!=null?res1Map.get("sco11"):0.00);		
				double score121 = (double)(res1Map.get("sco12")!=null?res1Map.get("sco12"):0.00);		
				double score131 = (double)(res1Map.get("sco13")!=null?res1Map.get("sco13"):0.00);		
				double score141 = (double)(res1Map.get("sco14")!=null?res1Map.get("sco14"):0.00);		
				int cnt0 = 0;
				int cnt1 = 0;
				int cnt2 = 0;
				int cnt3 = 0;
				int cnt4 = 0;
				int cnt5 = 0;
				int cnt6 = 0;
				int cnt7 = 0;
				int cnt8 = 0;
				int cnt9 = 0;
				int cnt10 = 0;
				int cnt11 = 0;
				int cnt12= 0;
				int cnt13= 0;
				int cnt14= 0;
				for(Map<String,Object> res2Map :medicalAnalyList) {
					double score02 = (double)(res2Map.get("sco0")!=null?res2Map.get("sco0"):0.00);		
					double score12 = (double)(res2Map.get("sco1")!=null?res2Map.get("sco1"):0.00);			
					double score22 = (double)(res2Map.get("sco2")!=null?res2Map.get("sco2"):0.00);			
					double score32 = (double)(res2Map.get("sco3")!=null?res2Map.get("sco3"):0.00);			
					double score42 = (double)(res2Map.get("sco4")!=null?res2Map.get("sco4"):0.00);			
					int score52 = (int)(res2Map.get("sco5")!=null?res2Map.get("sco5"):0);			
					double score62 = (double)(res2Map.get("sco6")!=null?res2Map.get("sco6"):0.00);			
					double score72 = (double)(res2Map.get("sco7")!=null?res2Map.get("sco7"):0.00);			
					double score82 = (double)(res2Map.get("sco8")!=null?res2Map.get("sco8"):0.00);			
					double score92 = (double)(res2Map.get("sco9")!=null?res2Map.get("sco9"):0.00);			
					double score102 = (double)(res2Map.get("sco10")!=null?res2Map.get("sco10"):0.00);			
					double score112 = (double)(res2Map.get("sco11")!=null?res2Map.get("sco11"):0.00);			
					double score122 = (double)(res2Map.get("sco12")!=null?res2Map.get("sco12"):0.00);			
					double score132 = (double)(res2Map.get("sco13")!=null?res2Map.get("sco13"):0.00);			
					double score142 = (double)(res2Map.get("sco14")!=null?res2Map.get("sco14"):0.00);			
					
					if(new Double(score01).compareTo(new Double(score02))< 0){
						cnt0 ++;						
					}
					if(new Double(score11).compareTo(new Double(score12))< 0){
						cnt1 ++;						
					}
					if(new Double(score21).compareTo(new Double(score22))< 0){
						cnt2 ++;						
					}
					if(new Double(score31).compareTo(new Double(score32))< 0){
						cnt3 ++;						
					}
					if(new Double(score41).compareTo(new Double(score42))< 0){
						cnt4 ++;						
					}
					if(new Double(score51).compareTo(new Double(score52))< 0){
						cnt5 ++;						
					}
					if(new Double(score61).compareTo(new Double(score62))< 0){
						cnt6 ++;						
					}
					if(new Double(score71).compareTo(new Double(score72))< 0){
						cnt7 ++;						
					}
					if(new Double(score81).compareTo(new Double(score82))< 0){
						cnt8 ++;						
					}
					if(new Double(score91).compareTo(new Double(score92))< 0){
						cnt9 ++;						
					}
					if(new Double(score101).compareTo(new Double(score102))< 0){
						cnt10 ++;						
					}
					if(new Double(score111).compareTo(new Double(score112))< 0){
						cnt11 ++;						
					}
					if(new Double(score121).compareTo(new Double(score122))< 0){
						cnt12 ++;						
					}
					if(new Double(score131).compareTo(new Double(score132))< 0){
						cnt13 ++;						
					}
					if(new Double(score141).compareTo(new Double(score142))< 0){
						cnt14 ++;						
					}
				}
				res1Map.put("score0jibai", accuracy(cnt0,total,0));
				res1Map.put("score1jibai", accuracy(cnt1,total,0));
				res1Map.put("score2jibai", accuracy(cnt2,total,0));
				res1Map.put("score3jibai", accuracy(cnt3,total,0));
				res1Map.put("score4jibai", accuracy(cnt4,total,0));
				res1Map.put("score5jibai", accuracy(cnt5,total,0));
				res1Map.put("score6jibai", accuracy(cnt6,total,0));
				res1Map.put("score7jibai", accuracy(cnt7,total,0));
				res1Map.put("score8jibai", accuracy(cnt8,total,0));
				res1Map.put("score9jibai", accuracy(cnt9,total,0));
				res1Map.put("score10jibai", accuracy(cnt10,total,0));
				res1Map.put("score11jibai", accuracy(cnt11,total,0));
				res1Map.put("score12jibai", accuracy(cnt12,total,0));
				res1Map.put("score13jibai", accuracy(cnt13,total,0));
				res1Map.put("score14jibai", accuracy(cnt14,total,0));
				resMedicalAnalyList.add(res1Map);
			}
		}
		
		return resMedicalAnalyList;
	}
	
   public int countWmwImgMedicalAnalyList(Map<String,Object> map, Integer page, Integer size, String sort, String order) {
    	return wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyCount(map);
   }
   
   public int countWmwImgMedicalAnalyDetailList(Map<String,Object> map, Integer page, Integer size, String sort, String order) {
	   return wmwImgMedicalAnalyseMapper.selectWmwImgMedicalAnalyDetailCount(map);
   }
   
	public static String accuracy(int num, int total, int scale){
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(0);
		String result = numberFormat.format((float) num / (float) total * 100);
		return result + "%";
	}
	
	public static void main(String[] args) {
		System.out.println(accuracy(3,68,2));
	}

}
