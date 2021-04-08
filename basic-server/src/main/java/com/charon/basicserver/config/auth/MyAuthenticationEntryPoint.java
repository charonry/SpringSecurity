package com.charon.basicserver.config.auth;

import com.charon.basicserver.config.exception.AjaxResponse;
import com.charon.basicserver.config.exception.CustomException;
import com.charon.basicserver.config.exception.CustomExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: SpringSecurity
 * @description 用户未登录就访问系统资源
 * @author: charon
 * @create: 2021-04-08 21:56
 **/
@Component
public class MyAuthenticationEntryPoint  implements AuthenticationEntryPoint {

    private  static ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.error(
                new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户未登录就访问系统资源，请先登录")
        )));

    }
}
