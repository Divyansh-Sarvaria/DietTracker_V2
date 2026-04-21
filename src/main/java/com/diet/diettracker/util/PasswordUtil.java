package com.diet.diettracker.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int SALT_ROUNDS = 12;

    public static String hash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt(SALT_ROUNDS));
    }

    public static boolean verify(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    private PasswordUtil() {} 
}