package com.charon.oauthrs.service;



import com.charon.mapper.MyUserDetailsServiceMapper;
import com.charon.oauthrs.model.MyUserDetails;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-14 22:59
 **/
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //加载基础用户信息
        MyUserDetails myUserDetails = myUserDetailsServiceMapper.findByUserName(username);

        if(myUserDetails == null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        //加载用户角色列表
        List<String> roleCodes = myUserDetailsServiceMapper.findRoleByUserName(username);
        //通过用户角色列表加载用户的资源权限列表
        List<String> authorities = myUserDetailsServiceMapper.findAuthorityByRoleCodes(roleCodes);
        //角色是一个特殊的权限，ROLE_前缀
        roleCodes = roleCodes.stream()
                .map(rc -> "ROLE_" + rc )
                .collect(Collectors.toList());

        //角色是一种特殊的权限，所以合并
        authorities.addAll(roleCodes);

        //转成用逗号分隔的字符串，为用户设置权限标识
        myUserDetails.setAuthorities(
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        String.join(",",authorities)
                )
        );

        return myUserDetails;
    }
}
