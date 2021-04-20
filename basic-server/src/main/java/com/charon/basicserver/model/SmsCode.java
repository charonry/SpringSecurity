package com.charon.basicserver.model;

import java.time.LocalDateTime;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-20 20:47
 **/
public class SmsCode {
    //短信验证码
    private String code;

    //过期时间
    private LocalDateTime expireTime;

    private String mobile;


    public SmsCode(String code, int expireAfterSeconds,String mobile){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
        this.mobile = mobile;
    }

    public boolean isExpired(){
        return  LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
