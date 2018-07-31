package org.linlinjava.litemall.db.domain;

/**
 * 
 * 序列号
 * @author Administrator
 *
 */
public class LitemallSerialNumber {
    private Integer id;

    private Integer serialNumber;

    private String serialType;//类型  MEMBER_ID：会员编号

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialType() {
        return serialType;
    }

    public void setSerialType(String serialType) {
        this.serialType = serialType == null ? null : serialType.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", serialNumber=").append(serialNumber);
        sb.append(", serialType=").append(serialType);
        sb.append("]");
        return sb.toString();
    }
}