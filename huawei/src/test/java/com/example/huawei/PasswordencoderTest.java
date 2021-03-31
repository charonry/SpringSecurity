package com.example.huawei;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-03-31 21:23
 **/
@SpringBootTest
public class PasswordencoderTest {

    @Test
    void context(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123"));
    }
}
