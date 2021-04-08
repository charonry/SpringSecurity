package com.charon.basicserver.config.auth;

import com.charon.basicserver.config.exception.AjaxResponse;
import com.charon.basicserver.config.exception.CustomException;
import com.charon.basicserver.config.exception.CustomExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-08 22:00
 **/
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private  static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.error(
                new CustomException(CustomExceptionType.SYSTEM_ERROR,"用户未含有该权限")
        )));
    }
}
