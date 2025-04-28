package xyz.ibudai.dailyword.basic.tool;

import java.security.SecureRandom;

public class CodeTool {

    /**
     * 验证码长度
     */
    private static final Integer CODE_LEN = 5;

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();


    public static String generate() {
        StringBuilder sb = new StringBuilder(CODE_LEN);
        for (int i = 0; i < CODE_LEN; i++) {
            int index = RANDOM.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(index));
        }
        return sb.toString();
    }
}
