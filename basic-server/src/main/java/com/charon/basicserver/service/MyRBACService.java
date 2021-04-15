package com.charon.basicserver.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-15 20:58
 **/
@Component("rbacService")
public class MyRBACService {

    /**
     * 判断某用户是否具有该request资源的访问权限
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication){
        //获取当前登录用户的UserDetails
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails){
            UserDetails userDetails = (UserDetails)principal;
            //将当前请求的访问资源路径，如:"/syslog",包装成资源权限标识
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(request.getRequestURI());
            //判断用户已授权访问的资源中，是否包含“本次请求的资源”
            return userDetails.getAuthorities().contains(simpleGrantedAuthority);
        }
        return  false;
    }

    public boolean checkUserId(Authentication authentication, int id) {
        //验证逻辑代码
        return true;
    }
}
