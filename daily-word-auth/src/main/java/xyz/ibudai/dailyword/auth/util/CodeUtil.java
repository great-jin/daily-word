package xyz.ibudai.dailyword.auth.util;

import java.util.Random;

public class CodeUtil {

    public static String generateEmailCode() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();
    }
}
