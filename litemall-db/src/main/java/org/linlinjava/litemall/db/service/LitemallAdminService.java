package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.LitemallAdminMapper;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.LitemallAdmin.Column;
import org.linlinjava.litemall.db.domain.LitemallAdminExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallAdminService {
    @Resource
    private LitemallAdminMapper adminMapper;

    public List<LitemallAdmin> findAdmin(String username) {
        LitemallAdminExample example = new LitemallAdminExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return adminMapper.selectByExample(example);
    }

    private final Column[] result = new Column[]{Column.id, Column.username, Column.avatar};
    public List<LitemallAdmin> querySelective(String username, Integer page, Integer limit, String sort, String order) {
        LitemallAdminExample example = new LitemallAdminExample();
        LitemallAdminExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(username)){
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        PageHelper.startPage(page, limit);
        return adminMapper.selectByExampleSelective(example, result);
    }

    public Map<String, Object> selectSelective(LitemallAdmin litemallAdmin, Integer page, Integer limit){
        PageHelper.startPage(page, limit);
        List<LitemallAdmin> adminList= adminMapper.selectLitemallAdminBySelective(litemallAdmin);
        PageInfo pageinfo = new PageInfo(adminList);
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageinfo.getTotal());
        data.put("items", adminList);
        return data;
    }

    public int countSelective(String username, Integer page, Integer size, String sort, String order) {
        LitemallAdminExample example = new LitemallAdminExample();
        LitemallAdminExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(username)){
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        return (int)adminMapper.countByExample(example);
    }

    public void updateById(LitemallAdmin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    public void deleteById(Integer id) {
        LitemallAdmin admin = adminMapper.selectByPrimaryKey(id);
        if(admin == null){
            return;
        }
        admin.setDeleted(true);
        adminMapper.updateByPrimaryKey(admin);
    }

    public void add(LitemallAdmin admin) {
        adminMapper.insertSelective(admin);
    }

    public LitemallAdmin findById(Integer id) {
        return adminMapper.selectByPrimaryKeySelective(id, result);
    }
}
