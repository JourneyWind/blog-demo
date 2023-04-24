package com.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class BcryptTest {

    @Test
    public void test(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode+"||");
        boolean matches = bCryptPasswordEncoder.matches("1234", encode);
        System.out.println(matches);
    }
}
