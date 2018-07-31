package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class LitemallStore {
    private Integer id;

    private String storeName;

    private String storeAddress;

    private String storeProvince;

    private String storeCity;

    private String storeImg;

    private Date createDate;

    private String createBy;

    private String storeRemark;

    private String storeTel;
    
    private String createDateStr;
    
    private String storeId;
    
    private String userId;
    
    private String managerName;
    
    private String projectTime;
    
    private String servicePeo;
    
    private String options;
    
    private String	goodsSn;
    
    private String province;
    
    private String cityName;
    
    private String provinceName;
    
    private String goodsId;
    
    private String storeCoordinate;//门店坐标
    
    private String flag;//区分 店长还是医生 0 :代表店长 1:代表医生,2:代表其他
    
    private String userStoreFlag;//用于查询用户分配门店时筛选出未分配店长的门店
    
    private String sflag;//用于用户分配门店
    
    private String doctor;//门店页面医生字段
    
    
    
    
    

    public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getSflag() {
		return sflag;
	}

	public void setSflag(String sflag) {
		this.sflag = sflag;
	}

	public String getUserStoreFlag() {
		return userStoreFlag;
	}

	public void setUserStoreFlag(String userStoreFlag) {
		this.userStoreFlag = userStoreFlag;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getStoreCoordinate() {
		return storeCoordinate;
	}

	public void setStoreCoordinate(String storeCoordinate) {
		this.storeCoordinate = storeCoordinate;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getProjectTime() {
		return projectTime;
	}

	public void setProjectTime(String projectTime) {
		this.projectTime = projectTime;
	}

	public String getServicePeo() {
		return servicePeo;
	}

	public void setServicePeo(String servicePeo) {
		this.servicePeo = servicePeo;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress == null ? null : storeAddress.trim();
    }

    public String getStoreProvince() {
        return storeProvince;
    }

    public void setStoreProvince(String storeProvince) {
        this.storeProvince = storeProvince == null ? null : storeProvince.trim();
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity == null ? null : storeCity.trim();
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg == null ? null : storeImg.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getStoreRemark() {
        return storeRemark;
    }

    public void setStoreRemark(String storeRemrk) {
        this.storeRemark = storeRemrk == null ? null : storeRemrk.trim();
    }

    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel == null ? null : storeTel.trim();
    }
}