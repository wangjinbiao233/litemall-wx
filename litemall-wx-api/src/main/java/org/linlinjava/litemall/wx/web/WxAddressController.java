package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallAddress;
import org.linlinjava.litemall.db.service.LitemallAddressService;
import org.linlinjava.litemall.db.service.LitemallRegionService;
import org.linlinjava.litemall.db.util.JacksonUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wx/address")
public class WxAddressController {
	private final Log logger = LogFactory.getLog(WxAddressController.class);

	@Autowired
	private LitemallAddressService addressService;
	@Autowired
	private LitemallRegionService regionService;

	/**
	 * 获取用户的收货地址
	 */
	@RequestMapping("list")
	public Object list(@RequestBody String body, HttpServletRequest request) {
		Integer userId = JacksonUtil.parseInteger(body, "userId");
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		List<LitemallAddress> addressList = addressService.queryByUid(userId);
		List<Map<String, Object>> addressVoList = new ArrayList<>(addressList.size());
		for (LitemallAddress address : addressList) {
			Map<String, Object> addressVo = new HashMap<>();
			addressVo.put("id", address.getId());
			addressVo.put("name", address.getName());
			addressVo.put("mobile", address.getMobile());
			addressVo.put("isDefault", address.getIsDefault());
			String province = regionService.findById(address.getProvinceId()).getName();
			String city = regionService.findById(address.getCityId()).getName();
			String area = regionService.findById(address.getAreaId()).getName();
			String addr = address.getAddress();
			String detailedAddress = province + city + area + " " + addr;
			addressVo.put("detailedAddress", detailedAddress);

			addressVoList.add(addressVo);
		}
		return ResponseUtil.ok(addressVoList);
	}

	/**
	 * 获取收货地址的详情
	 */
	@RequestMapping("detail")
	public Object detail(@RequestBody String body, HttpServletRequest request) {

		Integer userId = JacksonUtil.parseInteger(body, "userId");
		Integer id = JacksonUtil.parseInteger(body, "id");
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (id == null) {
			return ResponseUtil.fail402();
		}

		LitemallAddress address = addressService.findById(id);
		if (address == null) {
			return ResponseUtil.fail403();
		}

		Map<Object, Object> data = new HashMap<Object, Object>();
		data.put("id", address.getId());
		data.put("name", address.getName());
		data.put("provinceId", address.getProvinceId());
		data.put("cityId", address.getCityId());
		data.put("districtId", address.getAreaId());
		data.put("mobile", address.getMobile());
		data.put("address", address.getAddress());
		data.put("isDefault", address.getIsDefault());
		String pname = regionService.findById(address.getProvinceId()).getName();
		data.put("provinceName", pname);
		String cname = regionService.findById(address.getCityId()).getName();
		data.put("cityName", cname);
		String dname = regionService.findById(address.getAreaId()).getName();
		data.put("areaName", dname);
		return ResponseUtil.ok(data);
	}

	/**
	 * 添加或更新收货地址
	 */
	@RequestMapping("save")
	public Object save(@RequestBody LitemallAddress address) {

		if (address == null) {
			return ResponseUtil.fail402();
		}

		if (address.getUserId() == null) {
			return ResponseUtil.fail401();
		}

		Integer userId = address.getUserId();

		if (address.getIsDefault()) {
			// 重置其他收获地址的默认选项
			addressService.resetDefault(userId);
		}

		if (address.getId() == null || address.getId() == 0) {
			address.setId(null);
			address.setUserId(userId);
			addressService.add(address);
		} else {
			address.setUserId(userId);
			addressService.update(address);
		}
		return ResponseUtil.ok();
	}

	/**
	 * 删除指定的收货地址
	 */
	@RequestMapping("delete")
	public Object delete(@LoginUser Integer userId, @RequestBody LitemallAddress address) {
		if (userId == null) {
			return ResponseUtil.fail401();
		}
		if (address == null) {
			return ResponseUtil.fail402();
		}

		addressService.delete(address.getId());
		return ResponseUtil.ok();
	}
}