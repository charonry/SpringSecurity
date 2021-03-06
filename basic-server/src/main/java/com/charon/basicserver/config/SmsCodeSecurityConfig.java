package com.charon.basicserver.config;

import com.charon.basicserver.config.auth.MyAccessDeniedHandler;
import com.charon.basicserver.config.auth.MyAuthenticationEntryPoint;
import com.charon.basicserver.config.auth.MyAuthenticationFailureHandler;
import com.charon.basicserver.config.auth.MyAuthenticationSuccessHandler;
import com.charon.basicserver.filter.SmsCodeAuthenticationFilter;
import com.charon.basicserver.filter.SmsCodeAuthenticationProvider;
import com.charon.basicserver.filter.SmsCodeValidateFilter;
import com.charon.basicserver.service.MyUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-20 22:35
 **/
@Component
public class SmsCodeSecurityConfig  extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;



    @Resource
    MyUserDetailsService myUserDetailsService;

    @Resource
    SmsCodeValidateFilter smsCodeValidateFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        // ????????????????????????
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(myUserDetailsService);

        //??????????????????????????????????????????????????????????????????
        http.addFilterBefore(smsCodeValidateFilter, UsernamePasswordAuthenticationFilter.class);
        //????????????????????????????????????????????????????????????????????????
        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
    }
}
