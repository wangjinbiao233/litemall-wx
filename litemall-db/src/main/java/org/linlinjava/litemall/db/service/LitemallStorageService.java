package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.db.dao.LitemallStorageMapper;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.domain.LitemallStorageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallStorageService {

    @Autowired
    private LitemallStorageMapper litemallStorageMapper;


    /**
     * 添加文件记录
     *
     * @param record
     * @return
     */
    public int insertSelective(LitemallStorage record){
        return litemallStorageMapper.insert(record);
    }


    /**
     * 根据key查找文件
     *
     * @param key
     * @return
     */
    public LitemallStorage findByKey(String key) {

        LitemallStorageExample example = new LitemallStorageExample();
        LitemallStorageExample.Criteria criteria = example.createCriteria();
        criteria.andKeyEqualTo(key);

        return litemallStorageMapper.selectOneByExample(example);
    }


    /**
     * 根据id查询文件记录
     *
     * @param id
     * @return
     */
    public LitemallStorage selectByPrimaryKey(Integer id){
        return litemallStorageMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据id删除文件记录
     *
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id){
        return litemallStorageMapper.deleteByPrimaryKey(id);
    }


    /**
     * 查询文件列表
     *
     * @param page
     * @param size
     * @return
     */
    public Map<String,Object> selectStorage(String key,String oldName,String startTime,String endTime,Integer page, Integer size){
        LitemallStorageExample example = new LitemallStorageExample();
        LitemallStorageExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotEmpty(key)){
            criteria.andKeyEqualTo(key);
        }
        if (StringUtils.isNotEmpty(oldName)){
            criteria.andOldNameEqualTo(oldName);
        }

        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            criteria.andCreateTimeBetween(LocalDateTime.parse(startTime + " 00:00:00"), LocalDateTime.parse(endTime + " 23:59:59"));
        }

        PageHelper.startPage(page, size);
        List<LitemallStorage> list = litemallStorageMapper.selectByExample(example);


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
     * 根据id更新数据
     *
     * @param record
     * @return 更新条数
     */
    public int updateByPrimaryKeySelective(LitemallStorage record){
        return litemallStorageMapper.updateByPrimaryKeySelective(record);
    }



}
