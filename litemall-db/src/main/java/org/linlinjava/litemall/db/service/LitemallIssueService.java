package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallIssueMapper;
import org.linlinjava.litemall.db.domain.LitemallIssue;
import org.linlinjava.litemall.db.domain.LitemallIssueExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallIssueService {
    @Resource
    private LitemallIssueMapper issueMapper;

    public List<LitemallIssue> query(Integer questionType) {
    	LitemallIssue example = new LitemallIssue();
//        example.or().andDeletedEqualTo(false);
    	example.setQuestionType(questionType);
        return issueMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        LitemallIssue issue = issueMapper.selectByPrimaryKey(id);
        if(issue == null){
            return;
        }
        issue.setDeleted(true);
        issueMapper.updateByPrimaryKey(issue);
    }

    public void add(LitemallIssue issue) {
        issueMapper.insertSelective(issue);
    }

    public List<LitemallIssue> querySelective(String question, Integer page, Integer size, String sort, String order) {
        LitemallIssueExample example = new LitemallIssueExample();
        LitemallIssueExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(question)){
            criteria.andQuestionLike("%" + question + "%" );
        }
        criteria.andDeletedEqualTo(false);

        PageHelper.startPage(page, size);
        LitemallIssue issue  = new LitemallIssue();
        //issue.setQuestionType(0);
        return issueMapper.selectByExample(issue);
    }

    public int countSelective(String question, Integer page, Integer size, String sort, String order) {
        LitemallIssueExample example = new LitemallIssueExample();
        LitemallIssueExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(question)){
            criteria.andQuestionLike("%" + question + "%" );
        }
        criteria.andDeletedEqualTo(false);

        return (int)issueMapper.countByExample(example);
    }

    public void updateById(LitemallIssue issue) {
        issueMapper.updateByPrimaryKeySelective(issue);
    }

    public LitemallIssue findById(Integer id) {
        return issueMapper.selectByPrimaryKey(id);
    }
}
