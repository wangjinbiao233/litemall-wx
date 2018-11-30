package org.linlinjava.litemall.db.dto;

import java.math.BigDecimal;

/**
 * 销售订单统计报表
 *
 * @author huanghaoqi
 * @version 1.0
 * @date 2018年09月27日 16:22:04
 * @since JDK1.8
 */
public class SaleOrderReportDTO {
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 订单日期
     */
    private String orderDate;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 订单状态名称
     */
    private String orderStatusName;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 数量
     */
    private BigDecimal goodsNumber;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 金额
     */
    private BigDecimal totalPrices;
    /**
     * 剩余疗程数
     */
    private Integer residueTreatmentNum;
    /**
     * 疗程数
     */
    private Integer treatmentNum;
    /**
     * 商品归属
     */
    private String goodsFlag;
    /**
     * 商品归属名称
     */
    private String goodsFlagName;
    /**
     * 疗程总数
     */
    private Integer treatmentNumCount;
    /**
     * 会员编号
     */
    private String memberId;
    /**
     * 会员名称
     */
    private String username;
    /**
     * 订单金额
     */
    private BigDecimal orderPrice;
    /**
     * 券抵扣
     */
    private BigDecimal couponPrice;
    /**
     * 实际金额
     */
    private BigDecimal actualPrice;
    /**
     * 执行日期
     */
    private String executeDate;
    /**
     * 服务顾问
     */
    private String doctorName;
    /**
     * 存储金
     */
    private BigDecimal rechargeMoney;
    /**
     * 分销金
     */
    private BigDecimal money;
    /**
     * 分销商ID
     */
    private Integer distributionId;
    /**
     * 分销商名称
     */
    private String distributionName;
    /**
     * 分销商标签
     */
    private String distributionLabelNames;
    /**
     * 佣金
     */
    private BigDecimal profitMoney = BigDecimal.ZERO;
    /**
     * 操作状态
     */
    private Integer operationType;
    /**
     * 状态名称
     */
    private String operationTypeName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(BigDecimal goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrices() {
        return totalPrices;
    }

    public void setTotalPrices(BigDecimal totalPrices) {
        this.totalPrices = totalPrices;
    }

    public Integer getResidueTreatmentNum() {
        return residueTreatmentNum;
    }

    public void setResidueTreatmentNum(Integer residueTreatmentNum) {
        this.residueTreatmentNum = residueTreatmentNum;
    }

    public Integer getTreatmentNum() {
        return treatmentNum;
    }

    public void setTreatmentNum(Integer treatmentNum) {
        this.treatmentNum = treatmentNum;
    }

    public String getGoodsFlag() {
        return goodsFlag;
    }

    public void setGoodsFlag(String goodsFlag) {
        this.goodsFlag = goodsFlag;
    }

    public String getGoodsFlagName() {
        return goodsFlagName;
    }

    public void setGoodsFlagName(String goodsFlagName) {
        this.goodsFlagName = goodsFlagName;
    }

    public Integer getTreatmentNumCount() {
        return treatmentNumCount;
    }

    public void setTreatmentNumCount(Integer treatmentNumCount) {
        this.treatmentNumCount = treatmentNumCount;
    }

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

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String getDistributionName() {
        return distributionName;
    }

    public void setDistributionName(String distributionName) {
        this.distributionName = distributionName;
    }

    public Integer getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(Integer distributionId) {
        this.distributionId = distributionId;
    }

    public String getDistributionLabelNames() {
        return distributionLabelNames;
    }

    public void setDistributionLabelNames(String distributionLabelNames) {
        this.distributionLabelNames = distributionLabelNames;
    }

    public BigDecimal getProfitMoney() {
        return profitMoney;
    }

    public void setProfitMoney(BigDecimal profitMoney) {
        this.profitMoney = profitMoney;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }
}
