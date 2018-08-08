package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class WmwImgMedicalAnalyseZero {
    private Integer id;

    private Integer analyseId;

    private String zeroZrgImg;

    private String zeroBpImg;

    private String zeroZpImg;

    private String zeroYgImg;

    private String zeroBrownImg;

    private String zeroRedImg;

    private String zeroUvImg;

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

    public String getZeroZrgImg() {
        return zeroZrgImg;
    }

    public void setZeroZrgImg(String zeroZrgImg) {
        this.zeroZrgImg = zeroZrgImg == null ? null : zeroZrgImg.trim();
    }

    public String getZeroBpImg() {
        return zeroBpImg;
    }

    public void setZeroBpImg(String zeroBpImg) {
        this.zeroBpImg = zeroBpImg == null ? null : zeroBpImg.trim();
    }

    public String getZeroZpImg() {
        return zeroZpImg;
    }

    public void setZeroZpImg(String zeroZpImg) {
        this.zeroZpImg = zeroZpImg == null ? null : zeroZpImg.trim();
    }

    public String getZeroYgImg() {
        return zeroYgImg;
    }

    public void setZeroYgImg(String zeroYgImg) {
        this.zeroYgImg = zeroYgImg == null ? null : zeroYgImg.trim();
    }

    public String getZeroBrownImg() {
        return zeroBrownImg;
    }

    public void setZeroBrownImg(String zeroBrownImg) {
        this.zeroBrownImg = zeroBrownImg == null ? null : zeroBrownImg.trim();
    }

    public String getZeroRedImg() {
        return zeroRedImg;
    }

    public void setZeroRedImg(String zeroRedImg) {
        this.zeroRedImg = zeroRedImg == null ? null : zeroRedImg.trim();
    }

    public String getZeroUvImg() {
        return zeroUvImg;
    }

    public void setZeroUvImg(String zeroUvImg) {
        this.zeroUvImg = zeroUvImg == null ? null : zeroUvImg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}