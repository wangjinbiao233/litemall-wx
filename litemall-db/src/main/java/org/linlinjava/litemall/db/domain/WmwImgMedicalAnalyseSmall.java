package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class WmwImgMedicalAnalyseSmall {
    private Integer id;

    private Integer analyseId;

    private String smallZrgImg;

    private String smallBpImg;

    private String smallZpImg;

    private String smallYgImg;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnalyseId() {
        return analyseId;
    }

    public void setAnalyseId(Integer analyseId) {
        this.analyseId = analyseId;
    }

    public String getSmallZrgImg() {
        return smallZrgImg;
    }

    public void setSmallZrgImg(String smallZrgImg) {
        this.smallZrgImg = smallZrgImg == null ? null : smallZrgImg.trim();
    }

    public String getSmallBpImg() {
        return smallBpImg;
    }

    public void setSmallBpImg(String smallBpImg) {
        this.smallBpImg = smallBpImg == null ? null : smallBpImg.trim();
    }

    public String getSmallZpImg() {
        return smallZpImg;
    }

    public void setSmallZpImg(String smallZpImg) {
        this.smallZpImg = smallZpImg == null ? null : smallZpImg.trim();
    }

    public String getSmallYgImg() {
        return smallYgImg;
    }

    public void setSmallYgImg(String smallYgImg) {
        this.smallYgImg = smallYgImg == null ? null : smallYgImg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}