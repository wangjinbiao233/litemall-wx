package org.linlinjava.litemall.wx.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.linlinjava.litemall.db.domain.LitemallFaceUserdata;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.service.LitemallFaceService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.util.FileUploadConfig;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.wx.util.ImagePixelConvert;
import org.linlinjava.litemall.wx.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 人脸评测
 * @author dgt
 *
 */
@RestController
@RequestMapping("/wx/facialevaluate")
public class WxFacialEvaluateController {

	@Autowired
	private LitemallFaceService faceService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("getInfo")
	public Object getCoupon(String imageAddress,Integer imgId, Integer userId) {
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (imageAddress == null) {
			return ResponseUtil.fail402();
		}
		
		Map<String,Object> obj =new HashMap<String, Object>();
		try {
			obj =sendRequest(imageAddress);
			if((boolean)obj.get("success")==true){
				Integer faceFourdataId=faceService.saveFaceFourdata(userId,imgId, obj);
				obj.put("faceFourdataId", faceFourdataId);
			}else{
				LitemallFaceUserdata userdata=new LitemallFaceUserdata();
				userdata.setFaildata(obj.toString());
				userdata.setUserId(userId);
				userdata.setFaceimgId(imgId);
				userdata.setFacepixelid((Integer)obj.get("sid"));
				faceService.saveFiledata(userdata);
				
				return ResponseUtil.fail("图片转换失败：" + userdata);
			}
		} catch (Exception e) {
			LitemallFaceUserdata userdata=new LitemallFaceUserdata();
			userdata.setFaildata(e.getMessage());
			userdata.setUserId(userId);
			userdata.setFaceimgId(imgId);
			faceService.saveFiledata(userdata);
			return ResponseUtil.fail("系统内部异常：" + userdata);
		}
		//{success=true, error=null, result={skinAge=21, score=65, tagScore={maokong=0.68, redArea=0.69, sesu=0.64, wenli=0.6}}}
		return ResponseUtil.ok(obj);
	}
	
	@RequestMapping("getEvaluateData")
	public Object getEvaluateData(Integer faceFourdataId, Integer userId) {
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (faceFourdataId == null) {
			return ResponseUtil.fail402();
		}
		Map<String,Object> data=faceService.getFaceInfo(faceFourdataId);
		 
		return ResponseUtil.ok(data);
	}
	
	@RequestMapping("faceListData")
	public Object faceListData(Integer userId, @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("facedata", faceService.selectByUserId(userId, page, size));
		data.put("allcount", faceService.selectCountByUserId(userId));
		data.put("currentPage", page);
		return ResponseUtil.ok(data);
	}
	
	
	@RequestMapping("firstfaceCheck")
	public Object firstfaceCheck(Integer userId) {
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		Integer n=faceService.selectFirstCountByUserId(userId);
		return ResponseUtil.ok(n);
	}

	public Map<String,Object> sendRequest(String url) throws Exception {

		long tm = System.currentTimeMillis();
//		String appid = "011afe9cc1bab24f";
		String appid = "671afe9bb1bab45c";
		
		String str = "appId=" + appid + "timeStamp=" + tm
				+ "6cd5d076d7aabfc0b67b919625e1110c";
		String authSign = MD5Util.getMD5Lower(str);
		
//		String posturl = "http://aapi.dev.wmwbeautysalon.com/service/v1/philab/4d/uploadImage";
		String posturl = "http://ap.api.wmwbeautysalon.com/service/v1/philab/4d/uploadImage";

		//url = "http://mall.dgtis.com/images/image/2018/06/08/iyyv3iai7kegztsu92vc.jpg";
		BufferedImage readimg = ImageIO.read(new URL(url));
		
		//转像素
		int type = readimg.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
				: readimg.getType();
		BufferedImage resizeImageHintPng = ImagePixelConvert
				.resizeImage(readimg, type);
		File file = new File(tm + ".jpg");//生成
		ImageIO.write(resizeImageHintPng, "png", file);

		FileSystemResource resource = new FileSystemResource(file);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("appId", appid);
		map.add("timeStamp", tm);
		map.add("authSign", authSign);
		map.add("image", resource);

		RestTemplate restTemplate = new RestTemplate();
		// 发送请求，设置请求返回数据格式为String（去除上面方法中使用的httpEntity）
		Integer sid=savePixelImage(file);//存储转换后的图片到服务器上
		ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
				posturl, map, Object.class);
		
		file.delete();
		System.out.println(responseEntity.getStatusCode()+" body:"+responseEntity.getBody());
		
		Map<String,Object> mapdata=(Map<String, Object>) responseEntity.getBody();
		mapdata.put("sid", sid);
		
		return mapdata;
	}
	
	public Integer savePixelImage(File file){
		//File file = new File("C:\\Users\\dgt\\Desktop\\666.png");//生成
	
		FileSystemResource resource = new FileSystemResource(file);
	
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", resource);
		
		RestTemplate restTemplate = new RestTemplate();
		
		String posturl=FileUploadConfig.WX_UPLOAD_URL;
		ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
				posturl, map, Object.class);
		Map<String,Object> obj=(Map<String, Object>)responseEntity.getBody();
		System.out.println(responseEntity.getBody());
		if(Integer.parseInt(obj.get("errno")+"")==0){
			Map<String,Object> data=(Map<String,Object>)obj.get("data");
			Integer id=(Integer) data.get("id");
			
			return id;
		}
		return null;
		
	}

	public static void main(String[] args) throws Exception {
//		String url = "http://mall.dgtis.com/images/image/2018/06/20/1wmlcdcp7yq5yu5puixw.jpg";
//		WxFacialEvaluateController a = new WxFacialEvaluateController();
//		Map<String,Object> obj = (Map<String, Object>) a.sendRequest(url);
//		System.out.println(obj.get("success"));
//		if((boolean)obj.get("success")==true){
//			Map<String,Object> data=(Map<String, Object>) obj.get("result");
//			Map<String,Object> tagscore=(Map<String, Object>) data.get("tagScore");
//			LitemallFaceUserdata user=new LitemallFaceUserdata();
//			user.setSkinAge((Integer)data.get("skinAge"));
//			user.setScore((Integer)data.get("score"));
//			user.setMaokong((int)((double)tagscore.get("maokong")*100));
//			user.setRedArea((int)((double)tagscore.get("redArea")*100));
//			user.setSesu((int)((double)tagscore.get("sesu")*100));
//			user.setWenli((int)((double)tagscore.get("wenli")*100));
//		}
		WxFacialEvaluateController a = new WxFacialEvaluateController();
		File file = new File("C:\\Users\\dgt\\Desktop\\666.png");//生成
		Integer s=a.savePixelImage(file);
		System.out.println(s);
	}

	/**
	 * // RestTemplate restTemplate = new RestTemplate();
	 * 
	 * // 对中文格式数据进行处理 /* FormHttpMessageConverter fc = new
	 * FormHttpMessageConverter(); StringHttpMessageConverter stringConverter =
	 * new StringHttpMessageConverter( StandardCharsets.UTF_8);
	 * List<HttpMessageConverter<?>> partConverters = new
	 * ArrayList<HttpMessageConverter<?>>();
	 * partConverters.add(stringConverter); partConverters.add(new
	 * ResourceHttpMessageConverter()); fc.setPartConverters(partConverters);
	 * restTemplate.getMessageConverters().addAll( Arrays.asList(fc, new
	 * MappingJackson2HttpMessageConverter()));
	 * @throws Exception 
	 */
	
	/*public static void main(String[] args) throws Exception {
	 String url="http://mall.dgtis.com/images/image/2018/06/19/6syy84wj33jxkes85rty.jpg";
	 WxFacialEvaluateController w=new WxFacialEvaluateController();
		Map<String, Object> obj = (Map<String, Object>)w.sendRequest(url);
		System.out.println(obj.toString());
		//{success=false, error={code=MERCHANT-ANALYSIS-0003, desc=图片上传失败, extDesc=不好意思！您拍摄的照片找不到有效人脸或者人脸区域太小，请调整姿势，重新拍照，谢谢！！！}, result=null}
	}*/

}
