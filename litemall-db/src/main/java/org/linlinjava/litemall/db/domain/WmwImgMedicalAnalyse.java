package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class WmwImgMedicalAnalyse {
    private Integer analyseId;

    private Integer userId;

    private Integer shopId;

    private Integer merchantId;

    private Date createTime;

    private Integer status;

    private Integer type;

    private String phoneVersion;

    private Integer platformAnalyseId;

    private Integer faceType;

    public Integer getAnalyseId() {
        return analyseId;
    }

    public void setAnalyseId(Integer analyseId) {
        this.analyseId = analyseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhoneVersion() {
        return phoneVersion;
    }

    public void setPhoneVersion(String phoneVersion) {
        this.phoneVersion = phoneVersion == null ? null : phoneVersion.trim();
    }

    public Integer getPlatformAnalyseId() {
        return platformAnalyseId;
    }

    public void setPlatformAnalyseId(Integer platformAnalyseId) {
        this.platformAnalyseId = platformAnalyseId;
    }

    public Integer getFaceType() {
        return faceType;
    }

    public void setFaceType(Integer faceType) {
        this.faceType = faceType;
    }
}