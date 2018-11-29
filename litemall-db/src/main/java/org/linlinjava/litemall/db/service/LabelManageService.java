package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.db.dao.LitemallLabelMapper;
import org.linlinjava.litemall.db.dao.LitemallLabelUserMapper;
import org.linlinjava.litemall.db.domain.LitemallLabel;
import org.linlinjava.litemall.db.domain.LitemallLabelUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LabelManageService {

    @Resource
    private LitemallLabelMapper litemallLabelMapper;

    @Resource
    private LitemallLabelUserMapper litemallLabelUserMapper;

    /**
     * 查询标签列表
     * @param label
     * @param page
     * @param limit
     * @param sort
     * @return
     */
    public PageInfo<Map> querySelective(LitemallLabel label, Integer page, Integer limit, String sort) {
        PageHelper.startPage(page, limit);
        List<Map> list = litemallLabelMapper.selectSelective(label);
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 添加标签
     * @param label
     */
    public int add(LitemallLabel label) {
        if(StringUtils.isNoneBlank(label.getLabelName()) && label.getUserId() != null){

            return litemallLabelMapper.insertSelective(label);
        } else {
            return 0;

        }
    }

    /**
     * 修改标签
     * @param label
     * @return
     */
    public int update(LitemallLabel label) {
        return litemallLabelMapper.updateByPrimaryKeySelective(label);
    }

    public LitemallLabel selectById(Integer id) {
        return litemallLabelMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据userId查询标签
     * @param userId
     * @return
     */
    public List<LitemallLabel> selectByUserId(String userId) {
        return litemallLabelMapper.selectByUserId(userId);
    }

    /**
     * 添加
     * @param labelUser
     */
    public int addUserLabelInfo(LitemallLabelUser labelUser) {
        return litemallLabelUserMapper.insertSelective(labelUser);
    }
}
