package org.linlinjava.litemall.db.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class LitemallFaceUserdata {
    private Integer id;

    private Integer userId;

    private Integer minSkintypeId;

    private Integer maxSkintypeId;

    private Integer skinAge;

    private Integer score;

    private Integer maokong;

    private Integer redArea;

    private Integer sesu;

    private Integer wenli;

    private Integer isdelete;

    private Date createtime;
    
    private String faildata;

    private String skintypename;//什么肌 痘痘肌
    private Integer faceimgId;
    private Integer categoryId;
    
    private Integer facepixelid;
    

	public Integer getFacepixelid() {
		return facepixelid;
	}

	public void setFacepixelid(Integer facepixelid) {
		this.facepixelid = facepixelid;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getFaceimgId() {
		return faceimgId;
	}

	public void setFaceimgId(Integer faceimgId) {
		this.faceimgId = faceimgId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMinSkintypeId() {
        return minSkintypeId;
    }

    public void setMinSkintypeId(Integer minSkintypeId) {
        this.minSkintypeId = minSkintypeId;
    }

    public Integer getMaxSkintypeId() {
        return maxSkintypeId;
    }

    public void setMaxSkintypeId(Integer maxSkintypeId) {
        this.maxSkintypeId = maxSkintypeId;
    }

    public Integer getSkinAge() {
        return skinAge;
    }

    public void setSkinAge(Integer skinAge) {
        this.skinAge = skinAge;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMaokong() {
        return maokong;
    }

    public void setMaokong(Integer maokong) {
        this.maokong = maokong;
    }

    public Integer getRedArea() {
        return redArea;
    }

    public void setRedArea(Integer redArea) {
        this.redArea = redArea;
    }

    public Integer getSesu() {
        return sesu;
    }

    public void setSesu(Integer sesu) {
        this.sesu = sesu;
    }

    public Integer getWenli() {
        return wenli;
    }

    public void setWenli(Integer wenli) {
        this.wenli = wenli;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public String getSkintypename() {
		return skintypename;
	}

	public void setSkintypename(String skintypename) {
		this.skintypename = skintypename;
	}

	public String getFaildata() {
		return faildata;
	}

	public void setFaildata(String faildata) {
		this.faildata = faildata;
	}


    
}