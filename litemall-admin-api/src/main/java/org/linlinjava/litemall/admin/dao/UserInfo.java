package org.linlinjava.litemall.admin.dao;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private int id; // 标识
    private String loginId; // 用户登录名
    private String loginPwd; // 登录密码
    private String userName; // 用户名称
    private String userEmail; // 用户邮箱
    private String userPhone; // 用户手机号码
    private String company; // 所属公司
    private String department; // 部门
    private String deptName; // 部门名称
    private String post; // 职位
    private String workplace; // 工作地点
    private String employeNo; // 员工号
    private String createDate; // 创建时间
    private String updateDate; // 更新时间
    private String companyId; // 公司标识
    private boolean isAdmin; // 是否为管理员
    private boolean superAdmin; // 是否为超级管理员
    private String sex; // 性别
    private String headImage; //人物头像
    private String remainLeave;//年假剩余天数
    private String openId;//微信用户唯一标识
    private Integer isDelete;//是否删除
    private String vacation;//假期字段 用于签到


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getVacation() {
        return vacation;
    }

    public void setVacation(String vacation) {
        this.vacation = vacation;
    }

    public Integer isDelete() {
        return isDelete;
    }

    public void setDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getEmployeNo() {
        return employeNo;
    }

    public void setEmployeNo(String employeNo) {
        this.employeNo = employeNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getRemainLeave() {
        return remainLeave;
    }

    public void setRemainLeave(String remainLeave) {
        this.remainLeave = remainLeave;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    @Override
    public String toString() {
        return "UserInfo [id=" + id + ", loginId=" + loginId + ", loginPwd=" + loginPwd + ", userName=" + userName
                + ", userEmail=" + userEmail + ", userPhone=" + userPhone + ", company=" + company + ", department="
                + department + ", post=" + post + ", workplace=" + workplace + ", employeNo=" + employeNo
                + ", createDate=" + createDate + ", updateDate=" + updateDate + ", companyId=" + companyId
                + ", isAdmin=" + isAdmin + ", sex=" + sex + ", headImage=" + headImage + ", remainLeave=" + remainLeave
                + "]";
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
