package com.charon.basicserver;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-07 21:41
 **/
@SpringBootTest
public class PasswordEncoderTest {

    @Test
    public void bCryptPasswordTest(){
        //对于同一个原始密码，每次加密之后的hash密码都是不一样的 BCrypt*算法生成长度为 60 的字符串
        BCryptPasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();
        String rawPassword = "charon";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("原始密码:" + rawPassword);
        System.out.println("加密之后的hash密码:" + encodedPassword);

        System.out.println(rawPassword + "是否匹配" + encodedPassword + ":"
                + passwordEncoder.matches(rawPassword, encodedPassword));

        System.out.println("charonry是否匹配" + encodedPassword + ":"
                + passwordEncoder.matches("charonry", encodedPassword));
        //$2a$10$8LHVnB5dw1nmKyuu0lMeq.a2ryCCwseRljRmoIIHUFlSbo2yGIdlW
        //"2a"表示 BCrypt 算法版本;"10"表示算法的强度; 8LHVnB5dw1nmKyuu0lMeq.部分实际上是随机生成的盐,剩余部分是纯文本的实际哈希值。
    }
}
