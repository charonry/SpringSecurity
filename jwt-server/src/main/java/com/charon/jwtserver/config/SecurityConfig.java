package com.charon.jwtserver.config;

import com.charon.jwtserver.service.MyUserDetailsService;
import com.charon.jwtserver.config.auth.MyLogoutSuccessHandler;
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
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    MyUserDetailsService myUserDetailsService;

    @Resource
    private DataSource datasource;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.logout()
                .logoutUrl("/signout")
                //.logoutSuccessUrl("/login.html")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .and().rememberMe()
                .rememberMeParameter("remember-me-new")
                .rememberMeCookieName("remember-me-cookie")
                .tokenValiditySeconds(2 * 24 * 60 * 60)
                .tokenRepository(persistentTokenRepository())
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/login","/hello").permitAll()
                .antMatchers("/index").authenticated()
                .anyRequest().access("@rbacService.hasPermission(request,authentication)")
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login.html")
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);

    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        //将项目中静态资源路径开放出来
        web.ignoring()
                .antMatchers( "/css/**", "/fonts/**", "/img/**", "/js/**");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(datasource);

        return tokenRepository;
    }

}
