package org.linlinjava.litemall.db.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LitemallRecharge {
	private Integer id;

	private Integer orderId;

	private String orderSn;

	private Integer orderGoodsInfoId;

	private Integer orderGoodsId;

	private Integer operation;

	private LocalDateTime operationTime;

	private Integer rechargeUserId;

	private Integer rechargeType;

	private BigDecimal rechargeMoney;

	private String remark;

	private LocalDateTime createTime;

	private Integer createUserId;

	private LocalDateTime modifyTime;

	private Integer modifyUserId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn == null ? null : orderSn.trim();
	}

	public Integer getOrderGoodsInfoId() {
		return orderGoodsInfoId;
	}

	public void setOrderGoodsInfoId(Integer orderGoodsInfoId) {
		this.orderGoodsInfoId = orderGoodsInfoId;
	}

	public Integer getOrderGoodsId() {
		return orderGoodsId;
	}

	public void setOrderGoodsId(Integer orderGoodsId) {
		this.orderGoodsId = orderGoodsId;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public LocalDateTime getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(LocalDateTime operationTime) {
		this.operationTime = operationTime;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
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

	public Integer getRechargeUserId() {
		return rechargeUserId;
	}

	public void setRechargeUserId(Integer rechargeUserId) {
		this.rechargeUserId = rechargeUserId;
	}

	public Integer getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(Integer rechargeType) {
		this.rechargeType = rechargeType;
	}

	public BigDecimal getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(BigDecimal rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

}