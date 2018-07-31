package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class LitemallKnowledge{
    private Integer id;

    private String title;

    private String bannerUrl;
    
    private String titlePicUrl;

    private String introduction;
    
    private Integer knowledgeCls;
    
    private String knowledgeClsName;

    private Boolean isShow;

    private Integer visitCount;

    private Integer commentCount;

    private Integer praiseCount;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getTitlePicUrl() {
        return titlePicUrl;
    }

    public void setTitlePicUrl(String titlePicUrl) {
        this.titlePicUrl = titlePicUrl == null ? null : titlePicUrl.trim();
    }

    public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl == null ? null : bannerUrl.trim();
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction == null ? null : introduction.trim();
	}

	public Integer getKnowledgeCls() {
        return knowledgeCls;
    }

    public void setKnowledgeCls(Integer knowledgeCls) {
        this.knowledgeCls = knowledgeCls;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public String getKnowledgeClsName() {
		return knowledgeClsName;
	}

	public void setKnowledgeClsName(String knowledgeClsName) {
		this.knowledgeClsName = knowledgeClsName;
	}
}
