package com.example.mbaningapijpapractice.Util;

import java.security.SecureRandom;


public class Utility {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSecureCode() {
        int code = secureRandom.nextInt(900000) + 100000; // Generates a number between 100000 and 999999
        return String.valueOf(code);
    }


}
