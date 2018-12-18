package org.linlinjava.litemall.db.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.dao.LitemallFaceFourdataMapper;
import org.linlinjava.litemall.db.dao.LitemallFaceUserdataMapper;
import org.linlinjava.litemall.db.domain.LitemallFaceFourdata;
import org.linlinjava.litemall.db.domain.LitemallFaceUserdata;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
/**
 * 人脸评测service
 * @author dgt
 *
 */
@Service
public class LitemallFaceService {

	@Resource
	private LitemallFaceUserdataMapper faceUserdataMapper;

	@Resource
	private LitemallFaceFourdataMapper faceFourdataMapper;
	@Resource
    private LitemallGoodsService goodsService;
	
	public Integer saveFaceFourdata(Integer userId,Integer imgId,Map<String,Object> data){
		Map<String,Object> obj=(Map<String, Object>) data.get("result");
		Map<String,Object> tagscore=(Map<String, Object>) obj.get("tagScore");
		LitemallFaceUserdata user=new LitemallFaceUserdata();
		user.setFaceimgId(imgId);
		user.setFacepixelid((Integer)data.get("sid"));
		user.setSkinAge((Integer)obj.get("skinAge"));
		user.setScore((Integer)obj.get("score"));
		user.setMaokong((int)((double)tagscore.get("maokong")*100));
		user.setRedArea((int)((double)tagscore.get("redArea")*100));
		user.setSesu((int)((double)tagscore.get("sesu")*100));
		user.setWenli((int)((double)tagscore.get("wenli")*100));
		
		Map<Integer,Integer> compar=new HashMap<Integer, Integer>();
		//1234，对应face_skin_type表的4个类型
		compar.put(1, user.getMaokong());
		compar.put(2, user.getSesu());
		compar.put(3, user.getWenli());
		compar.put(4, user.getRedArea());
		
		 user.setMinSkintypeId(compers(compar,true));
		 user.setMaxSkintypeId(compers(compar,false));
		 
		 user.setUserId(userId);
		 user.setCreatetime(new Date());
		 user.setIsdelete(0);
		 faceUserdataMapper.insert(user);
		return user.getId();
	}
	
	public Map<String,Object> getFaceInfo(Integer faceFourdataId){
		LitemallFaceUserdata faceuserdata = faceUserdataMapper.selectById(faceFourdataId);
		
		LitemallFaceFourdata maokong = faceFourdataMapper.selectByTypePercentage(1, faceuserdata.getMaokong());
		LitemallFaceFourdata sesu = faceFourdataMapper.selectByTypePercentage(2, faceuserdata.getSesu());
		LitemallFaceFourdata wenli = faceFourdataMapper.selectByTypePercentage(3, faceuserdata.getWenli());
		LitemallFaceFourdata redArea = faceFourdataMapper.selectByTypePercentage(4, faceuserdata.getRedArea());
		
		//推荐的商品
		Map<String,Object>  data=new HashMap<String, Object>();
		
		data.put("defaultdata", faceuserdata);
		data.put("maokong", maokong);
		data.put("redArea", redArea);
		data.put("sesu", sesu);
		data.put("wenli", wenli);
		if(faceuserdata.getCategoryId()!=null){
			List<LitemallGoods> hotGoods = goodsService.queryByHotCategory(0, 3,faceuserdata.getCategoryId());
			data.put("hotGoods", hotGoods);
		}
		
		return data;
	}
	
	public List<LitemallFaceUserdata> selectByUserId(Integer userId, Integer offset, Integer limit){
		 PageHelper.startPage(offset, limit);
		return faceUserdataMapper.selectByUserId(userId);
	}
	public  Integer selectCountByUserId(Integer userId){
		return faceUserdataMapper.selectCountByUserId(userId);
	}
	
	public  Integer selectFirstCountByUserId(Integer userId){
		return faceUserdataMapper.selectFirstCountByUserId(userId);
	}
	
	/**
	 * 拿最大或最小的key
	 * @param map
	 * @param islow
	 * @return
	 */
	public  Integer compers(Map<Integer, Integer> map,boolean islow) {
		List<Entry<Integer, Integer>> list = new ArrayList<Entry<Integer, Integer>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1,
					Map.Entry<Integer, Integer> o2) {
				if(islow){
				// 升序排列 最小值
					return (o1.getValue() - o2.getValue());
				}
				// 降序排列 最大值
				 return (o2.getValue() - o1.getValue());

			}
		});
		return list.get(0).getKey();
	}
	
	public  Integer saveFiledata(LitemallFaceUserdata userdata){
		userdata.setIsdelete(1);
		userdata.setCreatetime(new Date());
		faceUserdataMapper.insert(userdata);
		
		return userdata.getId();
	}
}
