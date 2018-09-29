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
}
