package com.charon.basicserver.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-20 20:59
 **/
public interface MyRBACServiceMapper {

    @Select("SELECT url\n" +
            "FROM sys_menu m\n" +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id\n" +
            "LEFT JOIN sys_role r ON r.id = rm.role_id\n" +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id\n" +
            "LEFT JOIN sys_user u ON u.id = ur.user_id\n" +
            "WHERE u.username = #{userId} or u.phone = #{userId}")
    List<String> findUrlsByUserName(@Param("userId") String userId);
}
