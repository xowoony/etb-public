package com.emptybeer.etb.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {
    public static String hashSha512(String input) {
        try {
            StringBuilder passwordHashBuilder = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            for (byte hashByte : md.digest()) {
                passwordHashBuilder.append(String.format("%02x", hashByte));
            }
            return passwordHashBuilder.toString();
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        } catch (NullPointerException ignored) {
            return null;
        }
    }

    private CryptoUtils() {
    }
}
