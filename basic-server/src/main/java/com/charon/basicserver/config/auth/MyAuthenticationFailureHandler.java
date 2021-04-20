package com.charon.basicserver.config.auth;

import com.charon.basicserver.config.exception.AjaxResponse;
import com.charon.basicserver.config.exception.CustomException;
import com.charon.basicserver.config.exception.CustomExceptionType;
import com.charon.basicserver.mapper.MyUserDetailsServiceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Set;


/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-08 21:27
 **/
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${spring.security.logintype}")
    private String loginType;

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;


    private  static ObjectMapper objectMapper = new ObjectMapper();

    //规则定义：1小时之内5次机会，第6次失败就触发限流行为（禁止访问）
    Set<RequestLimitRule> rules =
            Collections.singleton(RequestLimitRule.of(Duration.ofMinutes(60),5));

    RequestRateLimiter limiter = new InMemorySlidingWindowRequestRateLimiter(rules);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");

        String errorMsg;
        if(exception instanceof LockedException){
            errorMsg = "您已经多次登陆失败，账户已被锁定，请稍后再试！";
        }else if(exception instanceof SessionAuthenticationException){
            errorMsg = exception.getMessage();
        }else {
            errorMsg = "请检查您的用户名和密码输入是否正确";
        }
        //每次登陆失败计数器加1，并判断该用户是否已经到了触发了锁定规则
       /* boolean reachLimit  = limiter.overLimitWhenIncremented(username);
        if(reachLimit){
            myUserDetailsServiceMapper.updateLockedByUserId(username);
            errorMsg = "您多次登陆失败，账户已被锁定，请稍后再试！";
        }*/

        if("JSON".equalsIgnoreCase(loginType)){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.error(
                    new CustomException(CustomExceptionType.USER_INPUT_ERROR,errorMsg)
            )));
        }else {
            response.setContentType("text/html;charset=UTF-8");
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
