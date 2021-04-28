package com.charon.oauthrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.charon"})
@ComponentScan(basePackages = {"com.charon"})
public class OauthrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthrsApplication.class, args);
    }

}
