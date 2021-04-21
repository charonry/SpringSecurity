package com.charon.basicserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan(basePackages = {"com.charon.basicserver"})
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30 * 60 * 1000)
public class BasicServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicServerApplication.class, args);
    }

}
