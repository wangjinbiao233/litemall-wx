package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class LitemallKnowledgeGoods {
    private Integer id;

    private Integer fkKnowledgeId;

    private String fkGoodsId;

    private String creator;

    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkKnowledgeId() {
        return fkKnowledgeId;
    }

    public void setFkKnowledgeId(Integer fkKnowledgeId) {
        this.fkKnowledgeId = fkKnowledgeId;
    }

    public String getFkGoodsId() {
        return fkGoodsId;
    }

    public void setFkGoodsId(String fkGoodsId) {
        this.fkGoodsId = fkGoodsId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}