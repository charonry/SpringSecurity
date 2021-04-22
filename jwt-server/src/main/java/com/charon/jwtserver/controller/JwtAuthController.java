package com.charon.jwtserver.controller;

import com.charon.jwtserver.config.exception.AjaxResponse;
import com.charon.jwtserver.config.exception.CustomException;
import com.charon.jwtserver.config.exception.CustomExceptionType;
import com.charon.jwtserver.service.JwtAuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-22 20:46
 **/
@RestController
public class JwtAuthController {
    @Resource
    JwtAuthService jwtAuthService;

    @RequestMapping(value = "/authentication")
    public AjaxResponse login(@RequestBody Map<String,String> map){
        String username  = map.get("username");
        String password = map.get("password");
        if(StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)){
            return AjaxResponse.error(
                    new CustomException(CustomExceptionType.USER_INPUT_ERROR,
                            "用户名或者密码不能为空"));
        }

        try{
            return AjaxResponse.success(jwtAuthService.login(username, password));
        }catch(CustomException e){
            return AjaxResponse.error(e);
        }
    }

    @RequestMapping(value = "/refreshtoken")
    public  AjaxResponse refresh(@RequestHeader("${jwt.header}") String token){
        return AjaxResponse.success(jwtAuthService.refreshToken(token));
    }
}
