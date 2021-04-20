package com.charon.basicserver.config.auth;

import com.charon.basicserver.config.exception.AjaxResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-08 21:18
 **/
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${spring.security.logintype}")
    private String loginType;

    //Jackson JSON数据处理类
    private  static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        if("JSON".equalsIgnoreCase(loginType)){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.success("/index")));
        }else {
            // 会帮我们跳转到上一次请求的页面上
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
