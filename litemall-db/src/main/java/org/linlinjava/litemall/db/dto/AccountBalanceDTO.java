package org.linlinjava.litemall.db.dto;

import java.math.BigDecimal;

/**
 *  销售订单统计报表
 *
 * @version 1.0
 * @since JDK1.8
 * @author huanghaoqi
 * @date 2018年09月27日 16:22:04
 */
public class AccountBalanceDTO {
    /**
     * 会员编号
     */
    private String memberId;
    /**
     * 会员名称
     */
    private String username;

    /**
     * 存储金
     */
    private BigDecimal rechargeMoney;
    /**
     * 分销金
     */
    private BigDecimal money;
    /**
     * 操作日期
     */
    private String operationTime;
    /**
     * 交易类型
     */
    private String operationType;
    /**
     * 支付方式
     */
    private Integer rechargeType;
    /**
     * 支付方式名称
     */
    private String rechargeTypeName;
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 交易金额
     */
    private BigDecimal chargeMoney;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(BigDecimal rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getRechargeTypeName() {
        return rechargeTypeName;
    }

    public void setRechargeTypeName(String rechargeTypeName) {
        this.rechargeTypeName = rechargeTypeName;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }
}
