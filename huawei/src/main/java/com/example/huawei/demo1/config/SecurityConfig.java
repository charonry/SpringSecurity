package com.example.huawei.demo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-03-30 21:55
 **/
@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Bean
    @Primary
    UserDetailsService us1(){
        return  new InMemoryUserDetailsManager(User.builder().username("charon").password("{noop}123").roles("admin").build());
    }

    @Bean
    UserDetailsService us2(){
        return  new InMemoryUserDetailsManager(User.builder().username("crane").password("{noop}123").roles("user").build());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
       /* DaoAuthenticationProvider dao1 = new DaoAuthenticationProvider();
        dao1.setUserDetailsService(us1());
        DaoAuthenticationProvider dao2 = new DaoAuthenticationProvider();
        dao2.setUserDetailsService(us2());
        return new ProviderManager(dao1,dao2);*/
        DaoAuthenticationProvider dao1 = new KaptchaAuthenticationProvider();
        dao1.setUserDetailsService(us1());
        return new ProviderManager(dao1);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http.authorizeRequests().anyRequest().authenticated()
            .and().formLogin()
            .and().csrf().disable();*/
        http.authorizeRequests().antMatchers("/vc.jpg").permitAll().anyRequest().authenticated()
                .and().formLogin().loginPage("/login.html").permitAll().and().csrf().disable();
    }
}
