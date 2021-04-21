package com.charon.jwtserver.mapper;


import com.charon.jwtserver.model.MyUserDetails;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-14 23:09
 **/
public interface MyUserDetailsServiceMapper {

    //根据userID查询用户信息
    @Select("SELECT username,password,enabled,accountNonLocked\n" +
            "FROM sys_user u\n" +
            "WHERE u.username = #{userName} or u.phone = #{userName}")
    MyUserDetails findByUserName(@Param("userName") String userName);

    //根据userID查询用户角色列表
    @Select("SELECT role_code\n" +
            "FROM sys_role r\n" +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id\n" +
            "LEFT JOIN sys_user u ON u.id = ur.user_id\n" +
            "WHERE u.username = #{userName}  or u.phone = #{userName}")
    List<String> findRoleByUserName(@Param("userName") String userName);


    //根据用户角色查询用户权限
    @Select({
            "<script>",
            "SELECT url " ,
            "FROM sys_menu m " ,
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " ,
            "LEFT JOIN sys_role r ON r.id = rm.role_id ",
            "WHERE r.role_code IN ",
            "<foreach collection='roleCodes' item='roleCode' open='(' separator=',' close=')'>",
            "#{roleCode}",
            "</foreach>",
            "</script>"
    })
    List<String> findAuthorityByRoleCodes(@Param("roleCodes") List<String> roleCodes);

    //用户登录失败锁定账号
    @Update({"UPDATE sys_user u \n" +
            " SET u.accountNonLocked = 0 \n" +
            " WHERE u.username = #{userId}" })
    int updateLockedByUserId(@Param("userId") String userId);
}
