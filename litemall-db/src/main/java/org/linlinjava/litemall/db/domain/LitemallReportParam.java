package org.linlinjava.litemall.db.domain;


import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 报表查询参数
 *
 * @author huanghaoqi
 * @version 1.0
 * @date 2018年09月27日 14:53:49
 * @since JDK1.8
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
    private String beginDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 开始的索引
     */
    private Integer offset;
    /**
     * 服务顾问
     */
    private String doctorName;
    /**
     * 交易类型 1充值,2消费,3退款
     */
    private Integer operationType;
    /**
     * 支付方式
     */
    private Integer rechargeType;
    /**
     * 页码
     */
    private Integer page = 1;
    /**
     * 每页显示个数
     */
    private Integer limit = 20;
    /**
     * 排序字段
     */
    private String sort;
    /**
     * 升序或降序
     */
    private String order;
    /**
     * 登录人ID
     */
    private Integer adminId;
    /**
     * 下单人的用户ID集合
     */
    private List<Integer> orderUserIds;

    /**
     * 分销商Ids
     * 2018/12/11
     */
    private List<Integer> distributionIds;
    /**
     * 分销商标签ids
     */
    private List<Integer> labelIds;
    /**
     * 分销商名称
     */
    private String distributionName;
    /**
     * 分销商标签名称
     */
    private String distributionLabelNames;
    /**
     * 佣金状态
     */
    private Integer optType;

    public String getDistributionName() {
        return distributionName;
    }

    public void setDistributionName(String distributionName) {
        this.distributionName = distributionName;
    }

    public String getDistributionLabelNames() {
        return distributionLabelNames;
    }

    public void setDistributionLabelNames(String distributionLabelNames) {
        this.distributionLabelNames = distributionLabelNames;
    }

    public List<Integer> getDistributionIds() {
        return distributionIds;
    }

    public void setDistributionIds(List<Integer> distributionIds) {
        this.distributionIds = distributionIds;
    }

    public List<Integer> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<Integer> labelIds) {
        this.labelIds = labelIds;
    }

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
            return (page - 1) * limit;
        }
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getBeginDate() {
        if(StringUtils.isEmpty(beginDate)){
            return null;
        }
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        if(StringUtils.isEmpty(endDate)){
            return null;
        }
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public List<Integer> getOrderUserIds() {
        return orderUserIds;
    }

    public void setOrderUserIds(List<Integer> orderUserIds) {
        this.orderUserIds = orderUserIds;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }
}
