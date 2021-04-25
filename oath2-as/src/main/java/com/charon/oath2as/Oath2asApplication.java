package com.charon.oath2as;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.charon")
@MapperScan(basePackages = "com.charon")
public class Oath2asApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oath2asApplication.class, args);
    }

}
