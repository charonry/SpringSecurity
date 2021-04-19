package com.charon.basicserver.model;

import java.time.LocalDateTime;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-19 22:24
 **/
public class CaptchaImageVO {
    private String code;

    private LocalDateTime expireTime;


    public CaptchaImageVO(String code, int expireAfterSeconds) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
    }

    public boolean isExpired(){
        return  LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
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
}
