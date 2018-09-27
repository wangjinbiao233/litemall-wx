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
public class SaleOrderReportDTO {
    /**
     * 门店名称
     */
    private String storeName;
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
}
