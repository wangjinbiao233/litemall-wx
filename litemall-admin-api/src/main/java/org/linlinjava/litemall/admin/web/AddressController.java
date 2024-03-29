package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallAddress;
import org.linlinjava.litemall.db.service.LitemallAddressService;
import org.linlinjava.litemall.db.service.LitemallRegionService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/address")
public class AddressController {
    private final Log logger = LogFactory.getLog(AddressController.class);

    @Autowired
    private LitemallAddressService addressService;
    @Autowired
    private LitemallRegionService regionService;

    private Map<String, Object> toVo (LitemallAddress address){
        Map<String, Object> addressVo = new HashMap<>();
        addressVo.put("id", address.getId());
        addressVo.put("userId", address.getUserId());
        addressVo.put("userName", address.getUserName());
        addressVo.put("name", address.getName());
        addressVo.put("mobile", address.getMobile());
        addressVo.put("isDefault", address.getIsDefault());
        addressVo.put("provinceId", address.getProvinceId());
        addressVo.put("cityId", address.getCityId());
        addressVo.put("areaId", address.getAreaId());
        addressVo.put("address", address.getAddress());
        String province = regionService.findById(address.getProvinceId()).getName();
        String city = regionService.findById(address.getCityId()).getName();
        String area = regionService.findById(address.getAreaId()).getName();
        addressVo.put("province", province);
        addressVo.put("city", city);
        addressVo.put("area", area);
        return addressVo;
    }

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
    		LitemallAddress address,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        PageInfo<LitemallAddress> pageInfo = addressService.querySelective(address, page, limit, sort, order);
        List<LitemallAddress> dataList = pageInfo.getList();
        List<Map<String, Object>> addressVoList = new ArrayList<>(dataList.size());
        for (LitemallAddress litemallAddress : dataList) {
        	 Map<String, Object> addressVo = toVo(litemallAddress);
             addressVoList.add(addressVo);
		}
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageInfo.getTotal());
        data.put("items", addressVoList);
        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallAddress address){
        if(adminId == null){
            return ResponseUtil.fail401();
        }

        addressService.add(address);

        Map<String, Object> addressVo = toVo(address);
        return ResponseUtil.ok(addressVo);
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer addressId){
        if(adminId == null){
            return ResponseUtil.fail401();
        }

        LitemallAddress address = addressService.findById(addressId);
        Map<String, Object> addressVo = toVo(address);
        return ResponseUtil.ok(addressVo);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallAddress address){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        addressService.updateById(address);
        Map<String, Object> addressVo = toVo(address);
        return ResponseUtil.ok(addressVo);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallAddress address){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        addressService.delete(address.getId());
        return ResponseUtil.ok();
    }

}
