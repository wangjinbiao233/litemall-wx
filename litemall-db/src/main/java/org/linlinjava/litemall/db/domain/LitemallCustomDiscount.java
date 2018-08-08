package org.linlinjava.litemall.db.domain;

import java.math.BigDecimal;

public class LitemallCustomDiscount extends LitemallDiscount {
	private String discountCount;// 优惠卷总数
	private String getCount;// 已领取个数
	private String useCount;// 已使用个数
	private String isUser;//用户是否以领取

	private String startTimeStr;
	private String endTimeStr;

	private BigDecimal goodsTotalPrice;
	private String discountTypeStr;

	public String getDiscountCount() {
		return discountCount;
	}

	public void setDiscountCount(String discountCount) {
		this.discountCount = discountCount;
	}

	public String getGetCount() {
		return getCount;
	}

	public void setGetCount(String getCount) {
		this.getCount = getCount;
	}

	public String getUseCount() {
		return useCount;
	}

	public void setUseCount(String useCount) {
		this.useCount = useCount;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public BigDecimal getGoodsTotalPrice() {
		return goodsTotalPrice;
	}

	public void setGoodsTotalPrice(BigDecimal goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}

    public String getDiscountTypeStr() {
		return discountTypeStr;
	}

	public void setDiscountTypeStr(String discountTypeStr) {
		this.discountTypeStr = discountTypeStr;
	}

	public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LitemallCustomDiscount{");
        sb.append("discountCount='").append(discountCount).append('\'');
        sb.append(", getCount='").append(getCount).append('\'');
        sb.append(", useCount='").append(useCount).append('\'');
        sb.append(", isUser='").append(isUser).append('\'');
        sb.append(", startTimeStr='").append(startTimeStr).append('\'');
        sb.append(", endTimeStr='").append(endTimeStr).append('\'');
        sb.append(", goodsTotalPrice=").append(goodsTotalPrice);
        sb.append('}');
        return sb.toString();
    }
}