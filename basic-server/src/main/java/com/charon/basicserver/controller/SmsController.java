package com.charon.basicserver.controller;

import com.charon.basicserver.config.exception.AjaxResponse;
import com.charon.basicserver.config.exception.CustomException;
import com.charon.basicserver.config.exception.CustomExceptionType;
import com.charon.basicserver.mapper.MyUserDetailsServiceMapper;
import com.charon.basicserver.model.MyUserDetails;
import com.charon.basicserver.model.SmsCode;
import com.charon.basicserver.utils.MyContants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-20 20:44
 **/
@RestController
@Slf4j
public class SmsController {

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @RequestMapping(value = "/smscode",method = RequestMethod.GET)
    public AjaxResponse sms(@RequestParam String mobile, HttpSession session){

        MyUserDetails myUserDetails = myUserDetailsServiceMapper.findByUserName(mobile);

        if(myUserDetails == null){
            return AjaxResponse.error(
                    new CustomException(CustomExceptionType.USER_INPUT_ERROR
                            ,"您输入的手机号不是系统注册用户")
            );
        }

        SmsCode smsCode = new SmsCode(RandomStringUtils.randomNumeric(4),60,mobile);

        //TODO 此处调用验证码发送服务接口
        log.info(smsCode.getCode() + "=》" + mobile);

        session.setAttribute(MyContants.SMS_SESSION_KEY,smsCode);

        return AjaxResponse.success("短信息已经发送到您的手机");

    }

}
