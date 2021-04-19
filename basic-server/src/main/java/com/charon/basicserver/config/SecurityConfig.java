package com.charon.basicserver.config;

import com.charon.basicserver.config.auth.*;
import com.charon.basicserver.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-07 21:11
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Resource
    MyUserDetailsService myUserDetailsService;

    @Resource
    DataSource dataSource;

    @Resource
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    /**
     * 用于安全认证以及授权规则配置
     * Controller服务资源要经过一系列的过滤器的验证，我们配置的是验证的放行规则
     * @param http  HttpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.logout().logoutUrl("/signout")
                    //.logoutSuccessUrl("/aftersignout.html")
                    .logoutSuccessHandler(myLogoutSuccessHandler)
                    .deleteCookies("JSESSIONID")// 支持多配制删除多个cookie
               .and().rememberMe()
                    .rememberMeParameter("remember-me-name")
                    .rememberMeCookieName("remember-me-cookie")
                    .tokenValiditySeconds(60*60*24)
                    .tokenRepository(persistentTokenRepository())
               .and().csrf().disable()//禁用跨站csrf攻击防御
               .formLogin()
                    .loginPage("/login.html")//一旦用户的请求没有权限就跳转到这个页面\
                    .loginProcessingUrl("/login")//登录表单form中action的地址，也就是处理认证请求的路径
                    .usernameParameter("username")//登录表单form中用户名输入框input的name名，不修改的话默认是username
                    .passwordParameter("password")//form中密码输入框input的name名，不修改的话默认是password
                     //.defaultSuccessUrl("/")//登录认证成功后默认转跳的路径
                    //.failureUrl("/login.html")
                    .successHandler(myAuthenticationSuccessHandler)
                    .failureHandler(myAuthenticationFailureHandler)
               .and()
                    .exceptionHandling()
                    .accessDeniedHandler(myAccessDeniedHandler)
                    .authenticationEntryPoint(myAuthenticationEntryPoint)
               .and()
               .authorizeRequests()
                    .antMatchers("/login.html","/login","/aftersignout.html").permitAll()//不需要通过登录验证就可以被访问的资源路径
                    .antMatchers("/person/{id}").access("@rbacService.checkUserId(authentication,#id)")
                    .anyRequest().access("@rbacService.hasPermission(request,authentication)")
               .and()
                    .sessionManagement() //会话管理
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/login.html")
                    .sessionFixation().migrateSession()
                    .maximumSessions(1) //会话限制
                    .maxSessionsPreventsLogin(false)
                    .expiredSessionStrategy(new MyExpiredSessionStrategy());

    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 开放静态资源权限
     * 配置的是静态资源的开放，不经过任何的过滤器链验证，直接访问资源。
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        //将项目中静态资源路径开放出来
        web.ignoring().antMatchers( "/css/**", "/fonts/**", "/img/**", "/js/**");
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }


}
