package com.example.huawei.demo2;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;

/**
 * @program: SpringSecurity
 * @description 整合老系统 如果不是自适应单向函数 就会更新密码
 * @author: charon
 * @create: 2021-03-31 21:44
 **/
public class Userservice implements UserDetailsPasswordService {
    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        // 去后台数据库更新密码
        int result =1;

        if(result ==1){
            ((User)userDetails).setPassword(newPassword);
        }
        return userDetails;
    }
}
