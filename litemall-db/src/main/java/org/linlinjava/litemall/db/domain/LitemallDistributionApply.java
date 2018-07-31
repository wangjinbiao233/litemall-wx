package org.linlinjava.litemall.db.domain;

import java.time.LocalDateTime;

public class LitemallDistributionApply {
	private Integer id;

	private Integer distributionId;

	private String nickName;

	private String[] picUrls;

	private String distributionType;

	private Integer auditStatus;

	private String remark;

	private LocalDateTime createTime;

	private String createTimeDisp;

	private Integer createUserId;

	private String createUserName;

	private LocalDateTime modifyTime;

	private Integer modifyUserId;

	private Boolean deleted;

	private String formId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDistributionId() {
		return distributionId;
	}

	public void setDistributionId(Integer distributionId) {
		this.distributionId = distributionId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName == null ? null : nickName.trim();
	}

	public String[] getPicUrls() {
		return picUrls;
	}

	public void setPicUrls(String[] picUrls) {
		this.picUrls = picUrls;
	}

	public String getDistributionType() {
		return distributionType;
	}

	public void setDistributionType(String distributionType) {
		this.distributionType = distributionType;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeDisp() {
		return createTimeDisp;
	}

	public void setCreateTimeDisp(String createTimeDisp) {
		this.createTimeDisp = createTimeDisp;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

}