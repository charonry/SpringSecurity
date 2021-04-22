package com.charon.jwtserver.service;

import com.charon.jwtserver.config.exception.CustomException;
import com.charon.jwtserver.config.exception.CustomExceptionType;
import com.charon.jwtserver.util.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-22 20:49
 **/
@Service
public class JwtAuthService {

    @Resource
    JwtTokenUtil jwtTokenUtil;

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    UserDetailsService userDetailsService;


    /**
     * 登录认证换取JWT令牌
     * @return JWT
     */
    public String login(String username,String password) throws CustomException {
        //使用用户名密码进行登录验证
        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username,password );
            Authentication authenticate = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }catch (AuthenticationException e){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户名或者密码不正确");
        }
         //生成JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername( username );
        return jwtTokenUtil.generateToken(userDetails);
    }

    public String refreshToken(String oldToken){
        if(!jwtTokenUtil.isTokenExpired(oldToken)){
            return jwtTokenUtil.refreshToken(oldToken);
        }
        return null;
    }
}
