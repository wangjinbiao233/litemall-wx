package org.linlinjava.litemall.admin.web;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.dao.LitemallStoreMapper;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.linlinjava.litemall.db.service.LitemallRoleService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/role")
public class RoleController {
    private final Log logger = LogFactory.getLog(RoleController.class);


    @Autowired
    private LitemallRoleService litemallRoleService;

    @Autowired
    private LitemallStoreMapper litemallStoreMapper;

    /**
     * 根据角色名称查询角色
     *
     * @param roleName
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("/selectByRoleName")
    public Object selectByRoleName(@LoginAdmin Integer adminId,String roleName, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        Map<String,Object> map = null;

        try {
            map = litemallRoleService.selectByRoleName(roleName, page, limit);
        } catch (Exception e){
            e.printStackTrace();
            ResponseUtil.fail();
        }

        return ResponseUtil.ok(map);

    }

    /**
     * 添加角色
     *
     * @param litemallRole
     * @return
     */
    @PostMapping("/insertSelective")
    public Object insertSelective(@LoginAdmin Integer adminId, LitemallRole litemallRole){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        Integer res = null;
        try {
            litemallRole.setCreateTime(LocalDateTime.now());
            litemallRole.setCreateUserId(adminId+"");
            litemallRole.setModifyTime(LocalDateTime.now());
            litemallRole.setModifyUserId(adminId+"");
            res = litemallRoleService.insertSelective(litemallRole);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.fail();
        }

        return ResponseUtil.ok(res);
    }

    /**
     * 跟新角色
     *
     * @param litemallRole
     * @return
     */
    @PostMapping("/updateByPrimaryKeySelective")
    public Object updateByPrimaryKeySelective(@LoginAdmin Integer adminId,LitemallRole litemallRole){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        Integer res = null;
        try {
        
            litemallRole.setModifyTime(LocalDateTime.now());
            litemallRole.setModifyUserId(adminId+"");
            res = litemallRoleService.updateByPrimaryKeySelective(litemallRole);
         
            
        } catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.fail();
        }

        return ResponseUtil.ok(res);
    }

    /**
     * 添加角色和菜单关联
     * @param roleId
     * @param menuList
     */
    @PostMapping("/insertRoleMenu")
    public Object insertRoleMenu(@LoginAdmin Integer adminId,String roleId,String menuList){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        try {
            litemallRoleService.insertRoleMenu(Integer.parseInt(roleId),Arrays.asList(menuList.split(",")));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.fail();
        }

        return ResponseUtil.ok();

    }


    /**
     *
     * 根据角色id查询菜单
     *
     * @param roleId
     * @return
     */
    @PostMapping("/selectMenuByRoleId")
    public Object selectMenuByRoleId(@LoginAdmin Integer adminId,Integer roleId){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        List<String> list = litemallRoleService.selectMenuByRoleId(roleId);
        return ResponseUtil.ok(list);
    }



    /**
     * 添加admin用户和角色关联
     * @param adminId
     * @param roleList
     */
    @PostMapping("/insertAdminRole")
    public Object insertAdminRole(@LoginAdmin Integer adminId,String userId,String roleList){

        if(adminId == null){
            return ResponseUtil.unlogin();
        }

        try {
            litemallRoleService.insertAdminRole(Integer.parseInt(userId),Arrays.asList(roleList.split(",")));
            //角色跟新 删除用户和门店的关联，消除数据冗余
        	LitemallStore store = new LitemallStore();
            store.setUserId(userId);
            litemallStoreMapper.deleteStoreUser(store);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.fail();
        }

        return ResponseUtil.ok();
    }

    /**
     *
     * 查询当前用户所拥有的菜单
     *
     * @param adminId
     */
    @PostMapping("selectAdminMenu")
    public Object selectAdminMenu(@LoginAdmin Integer adminId){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        List<String> list = null;
        try {
            list = litemallRoleService.selectAdminUserMenuByAdminId(adminId);
        } catch (Exception e){
            e.printStackTrace();
            ResponseUtil.fail();
        }

        return ResponseUtil.ok(list);
    }


    /**
     * 查询用户拥有的角色
     * @param adminId
     * @return
     */
    @PostMapping("selectAdminRole")
    public Object selectAdminRole(@LoginAdmin Integer adminId,String userId){
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        List<String> list =null;
        try {
            list = litemallRoleService.selectAdminUserRoleByAdminId(Integer.parseInt(userId));
        } catch (Exception e){
            e.printStackTrace();
            ResponseUtil.fail();
        }


        return ResponseUtil.ok(list);
    }
    
    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @PostMapping("/removetMenuByRoleId")
    public Object removetMenuByRoleId(Integer roleId){
    	litemallRoleService.removeRole(roleId);
    	return  ResponseUtil.ok();
    }

}
