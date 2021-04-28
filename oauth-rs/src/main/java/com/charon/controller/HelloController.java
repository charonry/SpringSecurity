package com.charon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-26 21:23
 **/
@RestController
@RequestMapping("/api")
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello Oauth2 Resource Server";
    }
}
