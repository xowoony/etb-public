package com.emptybeer.etb;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class RandomTest {
    @Test
    public void test() throws Exception {
        // 랜덤한 숫자 6자리가 콘솔에 출력된다.
        String authCode = RandomStringUtils.randomNumeric(6);
        String authSalt = String.format("%s%s%f%f",
                "xowoony@gmail.com",
                authCode,
                Math.random(),
                Math.random());
        System.out.println(authSalt);
        //여기서
        StringBuilder authSaltHashBuilder = new StringBuilder();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(authSalt.getBytes(StandardCharsets.UTF_8));
        for (byte hashByte : md.digest()) {
            authSaltHashBuilder.append(String.format("%02x", hashByte));
        }
        authSalt = authSaltHashBuilder.toString();
        System.out.println(authSalt);
    }
}

