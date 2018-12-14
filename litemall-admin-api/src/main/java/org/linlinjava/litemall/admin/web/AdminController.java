package org.linlinjava.litemall.admin.web;

import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.admin.service.AdminTokenManager;
import org.linlinjava.litemall.admin.util.bcrypt.BCryptPasswordEncoder;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.linlinjava.litemall.db.service.LitemallAdminService;
import org.linlinjava.litemall.db.service.LitemallRoleService;
import org.linlinjava.litemall.db.service.LitemallStoreService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/admin")
public class AdminController {
    @Autowired
    private LitemallAdminService adminService;
    @Autowired
    private LitemallRoleService litemallRoleService;
    @Autowired
	private LitemallStoreService storeService;

    @GetMapping("/info")
    public Object info(String token){
        Integer adminId = AdminTokenManager.getUserId(token);
        if(adminId == null){
            return ResponseUtil.badArgumentValue();
        }
        LitemallAdmin admin = adminService.findById(adminId);
        if(admin == null){
            return ResponseUtil.badArgumentValue();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("name", admin.getUsername());
        data.put("avatar", admin.getAvatar());

        // 目前roles不支持，这里简单设置admin
        //roles.add("admin");
        List<String> roles = litemallRoleService.selectPadRoleIdByUserId(adminId);        
        data.put("roles", roles);
        data.put("introduction", "admin introduction");
        //根据当前登录人信息，查询他所属门店信息
        List<LitemallStore> store = storeService.selectStoreByUserid(adminId);
        if(store!=null && store.size()>0) {
        	data.put("store", store.get(0));
        	data.put("userStoreList", store);
        }else {
        	data.put("store", null);
        	data.put("userStoreList", null);
        }        
        
        return ResponseUtil.ok(data);
    }

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
                       LitemallAdmin litemallAdmin,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        //List<LitemallAdmin> adminList = adminService.querySelective(username, page, limit, sort, order);
        //int total = adminService.countSelective(litemallAdmin.getUsername(), page, limit, sort, order);
        Map<String, Object> data = adminService.selectSelective(litemallAdmin,page,limit);


        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallAdmin admin){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        String rawPassword = admin.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        admin.setPassword(encodedPassword);

        adminService.add(admin);
        return ResponseUtil.ok(admin);
    }

    @GetMapping("/read")
    public Object read(@LoginAdmin Integer adminId, Integer id){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        if(id == null){
            return ResponseUtil.badArgument();
        }

        LitemallAdmin admin = adminService.findById(id);
        return ResponseUtil.ok(admin);
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallAdmin admin){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        Integer anotherAdminId = admin.getId();
        if(anotherAdminId.intValue() == 1){
            return ResponseUtil.fail(403, "超级管理员不能修改");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);

        adminService.updateById(admin);
        return ResponseUtil.ok(admin);
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallAdmin admin){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        Integer anotherAdminId = admin.getId();
        if(anotherAdminId.intValue() == 1){
            return ResponseUtil.fail(403, "超级管理员不能删除");
        }
        adminService.deleteById(anotherAdminId);
        return ResponseUtil.ok();
    }


    public Object uploadAvatar(@RequestParam("file") MultipartFile file){

        System.out.print(""+file.getSize());


        return ResponseUtil.ok();
    }
}
