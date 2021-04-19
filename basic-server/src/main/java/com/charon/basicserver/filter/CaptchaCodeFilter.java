package com.charon.basicserver.filter;

import com.charon.basicserver.config.auth.MyAuthenticationFailureHandler;
import com.charon.basicserver.model.CaptchaImageVO;
import com.charon.basicserver.utils.MyContants;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-19 22:57
 **/
@Component
public class CaptchaCodeFilter extends OncePerRequestFilter {

    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 必须是登录的post请求才能进行验证，其他的直接放行
        if(StringUtils.equals("/login",request.getRequestURI())&&
                StringUtils.equalsIgnoreCase(request.getMethod(),"post")){
            try {
                validate(new ServletWebRequest(request));
            }catch (AuthenticationException e){
                //2.捕获步骤1中校验出现异常，交给失败处理类进行进行处理
                myAuthenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }


    /**
     * 校验验证码
     * @param request
     * @throws ServletRequestBindingException
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getRequest().getSession();
        //获取用户登录界面输入的captchaCode
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "captchaCode");

        if(StringUtils.isEmpty(codeInRequest)){
            throw new SessionAuthenticationException("验证码不能为空");
        }

        // 获取session池中的验证码谜底
        CaptchaImageVO codeInSession = (CaptchaImageVO)
                session.getAttribute(MyContants.CAPTCHA_SESSION_KEY);
        if(Objects.isNull(codeInSession)) {
            throw new SessionAuthenticationException("您输入的验证码不存在");
        }

        // 校验服务器session池中的验证码是否过期
        if(codeInSession.isExpired()) {
            session.removeAttribute(MyContants.CAPTCHA_SESSION_KEY);
            throw new SessionAuthenticationException("验证码已经过期");
        }

        // 请求验证码校验
        if(!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new SessionAuthenticationException("验证码不匹配");
        }

    }
}
