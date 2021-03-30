package com.charon.basicserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan(basePackages = {"com.charon.basicserver.generator"})
public class BasicServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicServerApplication.class, args);
    }

}
