package com.charon.basicserver.filter;

import com.charon.basicserver.config.auth.MyAuthenticationFailureHandler;
import com.charon.basicserver.mapper.MyUserDetailsServiceMapper;
import com.charon.basicserver.model.MyUserDetails;
import com.charon.basicserver.model.SmsCode;
import com.charon.basicserver.utils.MyContants;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-20 21:40
 **/
@Component
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/smslogin",request.getRequestURI())
                && StringUtils.equalsIgnoreCase(request.getMethod(),"post")){
            try{
                //验证谜底与用户输入是否匹配
                validate(new ServletWebRequest(request));
            }catch(AuthenticationException e){
                myAuthenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }


    /**
     * 验证规则
     * @param request
     * @throws ServletRequestBindingException
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {

        HttpSession session = request.getRequest().getSession();
        SmsCode codeInSession = (SmsCode)session.getAttribute(MyContants.SMS_SESSION_KEY);
        String mobileInRequest = request.getParameter("mobile");
        String codeInRequest = request.getParameter("smsCode");


        if(StringUtils.isEmpty(mobileInRequest)){
            throw new SessionAuthenticationException("手机号码不能为空");
        }

        MyUserDetails myUserDetails = myUserDetailsServiceMapper.findByUserName(mobileInRequest);
        if(Objects.isNull(myUserDetails)){
            throw new SessionAuthenticationException("您输入的手机号不是系统的注册用户");
        }

        if(StringUtils.isEmpty(codeInRequest)) {
            throw new SessionAuthenticationException("短信验证码不能为空");
        }

        if(Objects.isNull(codeInSession)) {
            throw new SessionAuthenticationException("短信验证码不存在");
        }

        if(codeInSession.isExpired()) {
            session.removeAttribute(MyContants.SMS_SESSION_KEY);
            throw new SessionAuthenticationException("短信验证码已经过期");
        }

        if(!codeInSession.getCode().equals(codeInRequest)) {
            throw new SessionAuthenticationException("短信验证码不正确");
        }

        if(!codeInSession.getMobile().equals(mobileInRequest)) {
            throw new SessionAuthenticationException("短信发送目标与您输入的手机号不一致");
        }

        session.removeAttribute(MyContants.SMS_SESSION_KEY);

    }
}
