package com.zbank.card_service.util;

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

    public static int generateCVV() {
        return 100 + random.nextInt(900);
    }
}