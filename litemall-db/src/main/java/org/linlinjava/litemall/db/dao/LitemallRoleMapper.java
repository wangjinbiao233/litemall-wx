package org.linlinjava.litemall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.domain.LitemallRoleExample;

public interface LitemallRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    long countByExample(LitemallRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    int deleteByExample(LitemallRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    int insert(LitemallRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    int insertSelective(LitemallRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    List<LitemallRole> selectByExample(LitemallRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    List<LitemallRole> selectByExampleSelective(@Param("example") LitemallRoleExample example, @Param("selective") LitemallRole.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    LitemallRole selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    LitemallRole selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LitemallRole.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    int updateByExampleSelective(@Param("record") LitemallRole record, @Param("example") LitemallRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    int updateByExample(@Param("record") LitemallRole record, @Param("example") LitemallRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    int updateByPrimaryKeySelective(LitemallRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated Wed Apr 18 13:21:47 CST 2018
     */
    int updateByPrimaryKey(LitemallRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    LitemallRole selectOneByExample(LitemallRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    LitemallRole selectOneByExampleSelective(@Param("example") LitemallRoleExample example, @Param("selective") LitemallRole.Column ... selective);

    List<String> selectAdminUserMenuByAdminId(Integer id);
    
    /**
     * 删除角色
     * @param roleId
     */
    void removeRole(Integer roleId);

    /**
     * pad端查询当前用户所属角色
     * @param id
     * @return
     */
	List<String> selectPadAdminUserRoleById(Integer id);
	
	/**
	 * pad端查询当前用户所属角色
	 * @param id
	 * @return
	 */
	List<String> selectPadRoleIdByUserId(Integer id);
}