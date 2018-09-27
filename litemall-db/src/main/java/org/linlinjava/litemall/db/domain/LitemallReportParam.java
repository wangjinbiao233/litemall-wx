package org.linlinjava.litemall.db.domain;

import java.time.LocalDate;

/**
 *  报表查询参数
 *
 * @version 1.0
 * @since JDK1.8
 * @author huanghaoqi
 * @date 2018年09月27日 14:53:49
 */
public class LitemallReportParam {
    /**
     * 门店ID
     */
    private Integer storeId;
    /**
     * 会员编号
     */
    private String memberId;
    /**
     * 会员名称
     */
    private String username;
    /**
     * 商品归属1-商品;2-服务
     */
    private String goodsFlag;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 开始日期
     */
    private LocalDate beginDate;
    /**
     * 结束日期
     */
    private LocalDate endDate;
    /**
     * 开始的索引
     */
    private Integer offset;
    /**
     * 页码
     */
    private Integer page;
    /**
     * 每页显示个数
     */
    private Integer limit;
    /**
     * 排序字段
     */
    private String sort;
    /**
     *  升序或降序
     */
    private String order;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
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

    public String getGoodsFlag() {
        return goodsFlag;
    }

    public void setGoodsFlag(String goodsFlag) {
        this.goodsFlag = goodsFlag;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getOffset() {
        if (page != null && limit != null) {
            return  (page - 1) * limit;
        }
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
