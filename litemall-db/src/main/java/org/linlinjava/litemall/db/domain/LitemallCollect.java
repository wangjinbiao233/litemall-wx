package org.linlinjava.litemall.db.domain;

import java.time.LocalDateTime;

public class LitemallCollect {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_collect.id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_collect.user_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_collect.value_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    private Integer valueId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_collect.is_attention
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    private Boolean isAttention;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_collect.type_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    private Integer typeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_collect.add_time
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    private LocalDateTime addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_collect.deleted
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    private Boolean deleted;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_collect.id
     *
     * @return the value of litemall_collect.id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    
    private String userName;
    
	private String goodName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_collect.id
     *
     * @param id the value for litemall_collect.id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_collect.user_id
     *
     * @return the value of litemall_collect.user_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_collect.user_id
     *
     * @param userId the value for litemall_collect.user_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_collect.value_id
     *
     * @return the value of litemall_collect.value_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public Integer getValueId() {
        return valueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_collect.value_id
     *
     * @param valueId the value for litemall_collect.value_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public void setValueId(Integer valueId) {
        this.valueId = valueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_collect.is_attention
     *
     * @return the value of litemall_collect.is_attention
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public Boolean getIsAttention() {
        return isAttention;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_collect.is_attention
     *
     * @param isAttention the value for litemall_collect.is_attention
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public void setIsAttention(Boolean isAttention) {
        this.isAttention = isAttention;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_collect.type_id
     *
     * @return the value of litemall_collect.type_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_collect.type_id
     *
     * @param typeId the value for litemall_collect.type_id
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_collect.add_time
     *
     * @return the value of litemall_collect.add_time
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public LocalDateTime getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_collect.add_time
     *
     * @param addTime the value for litemall_collect.add_time
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_collect.deleted
     *
     * @return the value of litemall_collect.deleted
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_collect.deleted
     *
     * @param deleted the value for litemall_collect.deleted
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_collect
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
        sb.append(", userId=").append(userId);
        sb.append(", valueId=").append(valueId);
        sb.append(", isAttention=").append(isAttention);
        sb.append(", typeId=").append(typeId);
        sb.append(", addTime=").append(addTime);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_collect
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
        LitemallCollect other = (LitemallCollect) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getValueId() == null ? other.getValueId() == null : this.getValueId().equals(other.getValueId()))
            && (this.getIsAttention() == null ? other.getIsAttention() == null : this.getIsAttention().equals(other.getIsAttention()))
            && (this.getTypeId() == null ? other.getTypeId() == null : this.getTypeId().equals(other.getTypeId()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_collect
     *
     * @mbg.generated Sat Apr 07 10:22:31 CST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getValueId() == null) ? 0 : getValueId().hashCode());
        result = prime * result + ((getIsAttention() == null) ? 0 : getIsAttention().hashCode());
        result = prime * result + ((getTypeId() == null) ? 0 : getTypeId().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table litemall_collect
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        id("id"),
        userId("user_id"),
        valueId("value_id"),
        isAttention("is_attention"),
        typeId("type_id"),
        addTime("add_time"),
        deleted("deleted");

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_collect
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String column;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_collect
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_collect
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_collect
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        Column(String column) {
            this.column = column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_collect
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String desc() {
            return this.column + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_collect
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String asc() {
            return this.column + " ASC";
        }
    }
}