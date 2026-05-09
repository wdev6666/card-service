package com.zbank.cardservice.util;

import java.security.SecureRandom;
import java.util.Random;

public class CardUtil {

    private static final Random random = new Random();

    public static String generateCardNumber() {

        StringBuilder sb = new StringBuilder("4578");

        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public static String generatePin(){
        SecureRandom secureRand = new SecureRandom();
        int number = secureRand.nextInt(10000);

        return String.format("%04d", number);
    }

    public static int generateCVV() {
        return 100 + random.nextInt(900);
    }
}