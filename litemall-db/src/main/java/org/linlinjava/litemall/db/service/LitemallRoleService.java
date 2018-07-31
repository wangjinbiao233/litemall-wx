package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.LitemallAdminRoleMapper;
import org.linlinjava.litemall.db.dao.LitemallRoleMapper;
import org.linlinjava.litemall.db.dao.LitemallRoleMenuMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallRoleService {

    @Autowired
    private LitemallRoleMapper litemallRoleMapper;

    @Autowired
    private LitemallRoleMenuMapper litemallRoleMenuMapper;

    @Autowired
    private LitemallAdminRoleMapper litemallAdminRoleMapper;


    /**
     * 添加角色
     *
     * @param record 角色
     * @return int 插入条数
     */
    public int insertSelective(LitemallRole record){
        return litemallRoleMapper.insertSelective(record);
    }

    /**
     * 根据id查询角色
     *
     * @param id 角色id
     * @return LitemallRole 角色
     */
    public LitemallRole selectByPrimaryKey(Integer id){
        return litemallRoleMapper.selectByPrimaryKey(id);
    }

    /**
     *
     * 根据id更新角色信息
     *
     * @param record 角色
     * @return int 跟新条数
     */
    public int updateByPrimaryKeySelective(LitemallRole record){
        return litemallRoleMapper.updateByPrimaryKeySelective(record);
    }

    /**
     *
     * 分页查询角色表,如果roleName为空，则查询全部
     *
     * @param roleName 模糊查询
     * @param page
     * @param size
     * @return list<LitemallRole>
     */
    public Map<String,Object> selectByRoleName(String roleName, Integer page, Integer size){
        LitemallRoleExample example = new LitemallRoleExample();
        LitemallRoleExample.Criteria criteria = example.createCriteria();

        if (roleName != null){
            criteria.andRoleNameLike("%"+roleName+"%");
        }

        PageHelper.startPage(page, size);
        List<LitemallRole> list = litemallRoleMapper.selectByExample(example);
        PageInfo pageinfo = new PageInfo(list);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("items",list);
        resMap.put("pageNum",pageinfo.getPageNum());
        resMap.put("pageSize", pageinfo.getPageSize());
        resMap.put("startRow", pageinfo.getStartRow());
        resMap.put("endRow", pageinfo.getEndRow());
        resMap.put("total", pageinfo.getTotal());
        resMap.put("pages", pageinfo.getPages());
        return resMap;
    }


    /**
     * 添加角色和菜单关联
     *
     * @param roleId
     * @param menuList
     */
    public void insertRoleMenu(int roleId,List<String> menuList){
        LitemallRoleMenuExample example = new LitemallRoleMenuExample();
        LitemallRoleMenuExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);

        litemallRoleMenuMapper.deleteByExample(example);

        for (String menu:menuList){
            LitemallRoleMenu litemallRoleMenu = new LitemallRoleMenu();
            litemallRoleMenu.setRoleId(roleId);
            litemallRoleMenu.setMenuId(menu);
            litemallRoleMenuMapper.insertSelective(litemallRoleMenu);
        }

    }

    /**
     * 根据角色查询菜单
     *
     * @param roleId
     * @return List<String>
     */
    public List<String> selectMenuByRoleId(Integer roleId){

        LitemallRoleMenuExample example = new LitemallRoleMenuExample();
        LitemallRoleMenuExample.Criteria criteria = example.createCriteria();

        criteria.andRoleIdEqualTo(roleId);

        List<LitemallRoleMenu> list = litemallRoleMenuMapper.selectByExample(example);
        List<String> resultList = new ArrayList<String>();
        for (LitemallRoleMenu rm : list){
            resultList.add(rm.getMenuId());
        }

        return resultList;

    }


    /**
     *
     * 添加admin用户和角色关系
     * @param adminId
     * @param roleList
     */
    public void insertAdminRole(Integer adminId, List<String> roleList) {
        LitemallAdminRoleExample example = new LitemallAdminRoleExample();
        LitemallAdminRoleExample.Criteria criteria = example.createCriteria();
        criteria.andAdminIdEqualTo(adminId );
        litemallAdminRoleMapper.deleteByExample(example);

        for (String roleId:roleList){
            LitemallAdminRole litemallAdminRole = new LitemallAdminRole();
            litemallAdminRole.setAdminId(adminId);
            litemallAdminRole.setRoleId(Integer.parseInt(roleId));
            litemallAdminRoleMapper.insertSelective(litemallAdminRole);
        }
    }

    /**
     * 查询用户所拥有的菜单
     *
     * @param id
     * @return
     */
    public List<String> selectAdminUserMenuByAdminId(Integer id){
        List<String> list = litemallRoleMapper.selectAdminUserMenuByAdminId(id);
        return list;
    }

    /**
     * 查询用户拥有的角色
     *
     * @param adminId 用户id
     * @return
     */
    public List<String> selectAdminUserRoleByAdminId(Integer adminId) {
        LitemallAdminRoleExample example = new LitemallAdminRoleExample();
        LitemallAdminRoleExample.Criteria criteria = example.createCriteria();

        criteria.andAdminIdEqualTo(adminId);


        List<LitemallAdminRole> list = litemallAdminRoleMapper.selectByExample(example);
        List<String> resList = new ArrayList<String>();
        for (LitemallAdminRole ar:list){
            resList.add(ar.getRoleId()+"");
        }

        return resList;
    }
    
    
    /**
     * 删除角色
     */
    public void removeRole(Integer roleId){
    	litemallRoleMapper.removeRole(roleId);
    	litemallRoleMenuMapper.removeRoleMenu(roleId);
    	litemallAdminRoleMapper.removeRole(roleId);
    }

	public List<String> selectPadAdminUserRoleById(Integer id) {
		List<String> list = litemallRoleMapper.selectPadAdminUserRoleById(id);
        return list;
	}
}
