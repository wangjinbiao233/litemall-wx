package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class WmwImgMedicalAnalyseOriginal {
    private Integer id;

    private Integer analyseId;

    private String originalZrgImg;

    private String originalBpImg;

    private String originalZpImg;

    private String originalYgImg;

    private Date createTime;

    private Integer upside;

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

    public String getOriginalZrgImg() {
        return originalZrgImg;
    }

    public void setOriginalZrgImg(String originalZrgImg) {
        this.originalZrgImg = originalZrgImg == null ? null : originalZrgImg.trim();
    }

    public String getOriginalBpImg() {
        return originalBpImg;
    }

    public void setOriginalBpImg(String originalBpImg) {
        this.originalBpImg = originalBpImg == null ? null : originalBpImg.trim();
    }

    public String getOriginalZpImg() {
        return originalZpImg;
    }

    public void setOriginalZpImg(String originalZpImg) {
        this.originalZpImg = originalZpImg == null ? null : originalZpImg.trim();
    }

    public String getOriginalYgImg() {
        return originalYgImg;
    }

    public void setOriginalYgImg(String originalYgImg) {
        this.originalYgImg = originalYgImg == null ? null : originalYgImg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpside() {
        return upside;
    }

    public void setUpside(Integer upside) {
        this.upside = upside;
    }
}