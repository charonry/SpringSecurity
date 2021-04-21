package com.charon.basicserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-21 19:59
 **/
@Controller
public class SessionController {

    @RequestMapping(value="/uid",method = RequestMethod.GET)
    @ResponseBody
    public  String uid(HttpSession session) {
        return "sessionId:" + session.getId();
    }
}
