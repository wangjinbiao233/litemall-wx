package org.linlinjava.litemall.db.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LitemallDiscount {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.key
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private String key;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.discount_name
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private String discountName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.discounts_price
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private Integer discountsPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.limit_price
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private Integer limitPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.start_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private LocalDate startTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.end_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private LocalDate endTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.is_get
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private Integer isGet;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.is_use
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private Integer isUse;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.order_sn
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private String orderSn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.create_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private LocalDateTime createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.create_user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private String createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.modify_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private LocalDateTime modifyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_discount.modify_user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    private String modifyUserId;
    
    private Integer discountType;//优惠券类别

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.id
     *
     * @return the value of litemall_discount.id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.id
     *
     * @param id the value for litemall_discount.id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.key
     *
     * @return the value of litemall_discount.key
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public String getKey() {
        return key;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.key
     *
     * @param key the value for litemall_discount.key
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.discount_name
     *
     * @return the value of litemall_discount.discount_name
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public String getDiscountName() {
        return discountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.discount_name
     *
     * @param discountName the value for litemall_discount.discount_name
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.discounts_price
     *
     * @return the value of litemall_discount.discounts_price
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public Integer getDiscountsPrice() {
        return discountsPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.discounts_price
     *
     * @param discountsPrice the value for litemall_discount.discounts_price
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setDiscountsPrice(Integer discountsPrice) {
        this.discountsPrice = discountsPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.limit_price
     *
     * @return the value of litemall_discount.limit_price
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public Integer getLimitPrice() {
        return limitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.limit_price
     *
     * @param limitPrice the value for litemall_discount.limit_price
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setLimitPrice(Integer limitPrice) {
        this.limitPrice = limitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.start_time
     *
     * @return the value of litemall_discount.start_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public LocalDate getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.start_time
     *
     * @param startTime the value for litemall_discount.start_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.end_time
     *
     * @return the value of litemall_discount.end_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public LocalDate getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.end_time
     *
     * @param endTime the value for litemall_discount.end_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.is_get
     *
     * @return the value of litemall_discount.is_get
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public Integer getIsGet() {
        return isGet;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.is_get
     *
     * @param isGet the value for litemall_discount.is_get
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setIsGet(Integer isGet) {
        this.isGet = isGet;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.is_use
     *
     * @return the value of litemall_discount.is_use
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public Integer getIsUse() {
        return isUse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.is_use
     *
     * @param isUse the value for litemall_discount.is_use
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.user_id
     *
     * @return the value of litemall_discount.user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.user_id
     *
     * @param userId the value for litemall_discount.user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.order_sn
     *
     * @return the value of litemall_discount.order_sn
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.order_sn
     *
     * @param orderSn the value for litemall_discount.order_sn
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.create_time
     *
     * @return the value of litemall_discount.create_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.create_time
     *
     * @param createTime the value for litemall_discount.create_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.create_user_id
     *
     * @return the value of litemall_discount.create_user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.create_user_id
     *
     * @param createUserId the value for litemall_discount.create_user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.modify_time
     *
     * @return the value of litemall_discount.modify_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.modify_time
     *
     * @param modifyTime the value for litemall_discount.modify_time
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_discount.modify_user_id
     *
     * @return the value of litemall_discount.modify_user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public String getModifyUserId() {
        return modifyUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_discount.modify_user_id
     *
     * @param modifyUserId the value for litemall_discount.modify_user_id
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_discount
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", key=").append(key);
        sb.append(", discountName=").append(discountName);
        sb.append(", discountsPrice=").append(discountsPrice);
        sb.append(", limitPrice=").append(limitPrice);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", isGet=").append(isGet);
        sb.append(", isUse=").append(isUse);
        sb.append(", userId=").append(userId);
        sb.append(", orderSn=").append(orderSn);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", modifyUserId=").append(modifyUserId);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_discount
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LitemallDiscount other = (LitemallDiscount) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getKey() == null ? other.getKey() == null : this.getKey().equals(other.getKey()))
            && (this.getDiscountName() == null ? other.getDiscountName() == null : this.getDiscountName().equals(other.getDiscountName()))
            && (this.getDiscountsPrice() == null ? other.getDiscountsPrice() == null : this.getDiscountsPrice().equals(other.getDiscountsPrice()))
            && (this.getLimitPrice() == null ? other.getLimitPrice() == null : this.getLimitPrice().equals(other.getLimitPrice()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getIsGet() == null ? other.getIsGet() == null : this.getIsGet().equals(other.getIsGet()))
            && (this.getIsUse() == null ? other.getIsUse() == null : this.getIsUse().equals(other.getIsUse()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOrderSn() == null ? other.getOrderSn() == null : this.getOrderSn().equals(other.getOrderSn()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUserId() == null ? other.getCreateUserId() == null : this.getCreateUserId().equals(other.getCreateUserId()))
            && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()))
            && (this.getModifyUserId() == null ? other.getModifyUserId() == null : this.getModifyUserId().equals(other.getModifyUserId()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_discount
     *
     * @mbg.generated Fri May 04 14:11:30 CST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getKey() == null) ? 0 : getKey().hashCode());
        result = prime * result + ((getDiscountName() == null) ? 0 : getDiscountName().hashCode());
        result = prime * result + ((getDiscountsPrice() == null) ? 0 : getDiscountsPrice().hashCode());
        result = prime * result + ((getLimitPrice() == null) ? 0 : getLimitPrice().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getIsGet() == null) ? 0 : getIsGet().hashCode());
        result = prime * result + ((getIsUse() == null) ? 0 : getIsUse().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOrderSn() == null) ? 0 : getOrderSn().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUserId() == null) ? 0 : getCreateUserId().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getModifyUserId() == null) ? 0 : getModifyUserId().hashCode());
        return result;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table litemall_discount
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        id("id"),
        key("key"),
        discountName("discount_name"),
        discountsPrice("discounts_price"),
        limitPrice("limit_price"),
        startTime("start_time"),
        endTime("end_time"),
        isGet("is_get"),
        isUse("is_use"),
        userId("user_id"),
        orderSn("order_sn"),
        createTime("create_time"),
        createUserId("create_user_id"),
        modifyTime("modify_time"),
        modifyUserId("modify_user_id");

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_discount
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String column;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_discount
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_discount
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_discount
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        Column(String column) {
            this.column = column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_discount
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String desc() {
            return this.column + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_discount
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String asc() {
            return this.column + " ASC";
        }
    }
}