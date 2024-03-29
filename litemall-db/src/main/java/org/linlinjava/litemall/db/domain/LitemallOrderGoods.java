package org.linlinjava.litemall.db.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class LitemallOrderGoods {
	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private Integer id;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.order_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private Integer orderId;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.goods_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private Integer goodsId;

	private Integer storeId;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.goods_name
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private String goodsName;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.goods_sn
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private String goodsSn;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.product_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private Integer productId;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.number
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private Short number;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.retail_price
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private BigDecimal retailPrice;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.goods_specification_values
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private String goodsSpecificationValues;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.goods_specification_ids
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private Integer[] goodsSpecificationIds;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.pic_url
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private String picUrl;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.add_time
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private LocalDateTime addTime;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column litemall_order_goods.deleted
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	private Boolean deleted;

	// 订单类型 1：商品 2：服务
	private String flag;

	// 疗程数
	private int treatmentNum;

	// 预约状态 true：已预约 false：未预约
	private String reserveFlag;

	// 是否退货
	private String isReturn;

	// 订单状态
	private Short orderStatus;

	// 实付价格
	private BigDecimal actualPrice;

	private Integer isExtract;

	private List<Short> orderStatusList;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.id
	 *
	 * @return the value of litemall_order_goods.id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.id
	 *
	 * @param id
	 *            the value for litemall_order_goods.id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.order_id
	 *
	 * @return the value of litemall_order_goods.order_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.order_id
	 *
	 * @param orderId
	 *            the value for litemall_order_goods.order_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.goods_id
	 *
	 * @return the value of litemall_order_goods.goods_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public Integer getGoodsId() {
		return goodsId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.goods_id
	 *
	 * @param goodsId
	 *            the value for litemall_order_goods.goods_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.goods_name
	 *
	 * @return the value of litemall_order_goods.goods_name
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.goods_name
	 *
	 * @param goodsName
	 *            the value for litemall_order_goods.goods_name
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.goods_sn
	 *
	 * @return the value of litemall_order_goods.goods_sn
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public String getGoodsSn() {
		return goodsSn;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.goods_sn
	 *
	 * @param goodsSn
	 *            the value for litemall_order_goods.goods_sn
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.product_id
	 *
	 * @return the value of litemall_order_goods.product_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public Integer getProductId() {
		return productId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.product_id
	 *
	 * @param productId
	 *            the value for litemall_order_goods.product_id
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.number
	 *
	 * @return the value of litemall_order_goods.number
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public Short getNumber() {
		return number;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.number
	 *
	 * @param number
	 *            the value for litemall_order_goods.number
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setNumber(Short number) {
		this.number = number;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.retail_price
	 *
	 * @return the value of litemall_order_goods.retail_price
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.retail_price
	 *
	 * @param retailPrice
	 *            the value for litemall_order_goods.retail_price
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.goods_specification_values
	 *
	 * @return the value of litemall_order_goods.goods_specification_values
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public String getGoodsSpecificationValues() {
		return goodsSpecificationValues;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.goods_specification_values
	 *
	 * @param goodsSpecificationValues
	 *            the value for litemall_order_goods.goods_specification_values
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setGoodsSpecificationValues(String goodsSpecificationValues) {
		this.goodsSpecificationValues = goodsSpecificationValues;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.goods_specification_ids
	 *
	 * @return the value of litemall_order_goods.goods_specification_ids
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public Integer[] getGoodsSpecificationIds() {
		return goodsSpecificationIds;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.goods_specification_ids
	 *
	 * @param goodsSpecificationIds
	 *            the value for litemall_order_goods.goods_specification_ids
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setGoodsSpecificationIds(Integer[] goodsSpecificationIds) {
		this.goodsSpecificationIds = goodsSpecificationIds;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.pic_url
	 *
	 * @return the value of litemall_order_goods.pic_url
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.pic_url
	 *
	 * @param picUrl
	 *            the value for litemall_order_goods.pic_url
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.add_time
	 *
	 * @return the value of litemall_order_goods.add_time
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public LocalDateTime getAddTime() {
		return addTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.add_time
	 *
	 * @param addTime
	 *            the value for litemall_order_goods.add_time
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setAddTime(LocalDateTime addTime) {
		this.addTime = addTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column litemall_order_goods.deleted
	 *
	 * @return the value of litemall_order_goods.deleted
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column litemall_order_goods.deleted
	 *
	 * @param deleted
	 *            the value for litemall_order_goods.deleted
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getTreatmentNum() {
		return treatmentNum;
	}

	public void setTreatmentNum(int treatmentNum) {
		this.treatmentNum = treatmentNum;
	}

	public String getReserveFlag() {
		return reserveFlag;
	}

	public void setReserveFlag(String reserveFlag) {
		this.reserveFlag = reserveFlag;
	}

	public String getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Integer getIsExtract() {
		return isExtract;
	}

	public void setIsExtract(Integer isExtract) {
		this.isExtract = isExtract;
	}

	public List<Short> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<Short> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table litemall_order_goods
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", orderId=").append(orderId);
		sb.append(", goodsId=").append(goodsId);
		sb.append(", goodsName=").append(goodsName);
		sb.append(", goodsSn=").append(goodsSn);
		sb.append(", productId=").append(productId);
		sb.append(", number=").append(number);
		sb.append(", retailPrice=").append(retailPrice);
		sb.append(", goodsSpecificationValues=").append(goodsSpecificationValues);
		sb.append(", goodsSpecificationIds=").append(goodsSpecificationIds);
		sb.append(", picUrl=").append(picUrl);
		sb.append(", addTime=").append(addTime);
		sb.append(", deleted=").append(deleted);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table litemall_order_goods
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
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
		LitemallOrderGoods other = (LitemallOrderGoods) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getOrderId() == null ? other.getOrderId() == null
						: this.getOrderId().equals(other.getOrderId()))
				&& (this.getGoodsId() == null ? other.getGoodsId() == null
						: this.getGoodsId().equals(other.getGoodsId()))
				&& (this.getGoodsName() == null ? other.getGoodsName() == null
						: this.getGoodsName().equals(other.getGoodsName()))
				&& (this.getGoodsSn() == null ? other.getGoodsSn() == null
						: this.getGoodsSn().equals(other.getGoodsSn()))
				&& (this.getProductId() == null ? other.getProductId() == null
						: this.getProductId().equals(other.getProductId()))
				&& (this.getNumber() == null ? other.getNumber() == null : this.getNumber().equals(other.getNumber()))
				&& (this.getRetailPrice() == null ? other.getRetailPrice() == null
						: this.getRetailPrice().equals(other.getRetailPrice()))
				&& (this.getGoodsSpecificationValues() == null ? other.getGoodsSpecificationValues() == null
						: this.getGoodsSpecificationValues().equals(other.getGoodsSpecificationValues()))
				&& (Arrays.equals(this.getGoodsSpecificationIds(), other.getGoodsSpecificationIds()))
				&& (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
				&& (this.getAddTime() == null ? other.getAddTime() == null
						: this.getAddTime().equals(other.getAddTime()))
				&& (this.getDeleted() == null ? other.getDeleted() == null
						: this.getDeleted().equals(other.getDeleted()));
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table litemall_order_goods
	 *
	 * @mbg.generated Sat Apr 07 10:22:31 CST 2018
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
		result = prime * result + ((getGoodsId() == null) ? 0 : getGoodsId().hashCode());
		result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
		result = prime * result + ((getGoodsSn() == null) ? 0 : getGoodsSn().hashCode());
		result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
		result = prime * result + ((getNumber() == null) ? 0 : getNumber().hashCode());
		result = prime * result + ((getRetailPrice() == null) ? 0 : getRetailPrice().hashCode());
		result = prime * result
				+ ((getGoodsSpecificationValues() == null) ? 0 : getGoodsSpecificationValues().hashCode());
		result = prime * result + (Arrays.hashCode(getGoodsSpecificationIds()));
		result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
		result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
		result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
		return result;
	}

	/**
	 * This enum was generated by MyBatis Generator. This enum corresponds to the
	 * database table litemall_order_goods
	 *
	 * @mbg.generated
	 * @project https://github.com/itfsw/mybatis-generator-plugin
	 */
	public enum Column {
		id("id"), orderId("order_id"), goodsId("goods_id"), goodsName("goods_name"), goodsSn("goods_sn"), productId(
				"product_id"), number("number"), retailPrice("retail_price"), goodsSpecificationValues(
						"goods_specification_values"), goodsSpecificationIds(
								"goods_specification_ids"), picUrl("pic_url"), addTime("add_time"), deleted("deleted");

		/**
		 * This field was generated by MyBatis Generator. This field corresponds to the
		 * database table litemall_order_goods
		 *
		 * @mbg.generated
		 * @project https://github.com/itfsw/mybatis-generator-plugin
		 */
		private final String column;

		/**
		 * This method was generated by MyBatis Generator. This method corresponds to
		 * the database table litemall_order_goods
		 *
		 * @mbg.generated
		 * @project https://github.com/itfsw/mybatis-generator-plugin
		 */
		public String value() {
			return this.column;
		}

		/**
		 * This method was generated by MyBatis Generator. This method corresponds to
		 * the database table litemall_order_goods
		 *
		 * @mbg.generated
		 * @project https://github.com/itfsw/mybatis-generator-plugin
		 */
		public String getValue() {
			return this.column;
		}

		/**
		 * This method was generated by MyBatis Generator. This method corresponds to
		 * the database table litemall_order_goods
		 *
		 * @mbg.generated
		 * @project https://github.com/itfsw/mybatis-generator-plugin
		 */
		Column(String column) {
			this.column = column;
		}

		/**
		 * This method was generated by MyBatis Generator. This method corresponds to
		 * the database table litemall_order_goods
		 *
		 * @mbg.generated
		 * @project https://github.com/itfsw/mybatis-generator-plugin
		 */
		public String desc() {
			return this.column + " DESC";
		}

		/**
		 * This method was generated by MyBatis Generator. This method corresponds to
		 * the database table litemall_order_goods
		 *
		 * @mbg.generated
		 * @project https://github.com/itfsw/mybatis-generator-plugin
		 */
		public String asc() {
			return this.column + " ASC";
		}
	}
}