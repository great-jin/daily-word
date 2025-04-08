package xyz.ibudai.dailyword.basic.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {

    /**
     * Hash string.
     *
     * @param data the data
     * @return the string
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static String hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(data.getBytes());

        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b & 0xff));
        }
        return builder.toString();
    }
}
