package xyz.ibudai.dailyword.auth.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private final static String ALGORITHM = "AES/CBC/NoPadding";
    private final static String DEFAULT_IV = "budai_invincible";
    private final static String DEFAULT_KEY = "budai_invincible";


    /**
     * 默认的 KEY 和 IV 加密
     */
    public static String encrypt(String data) throws Exception {
        return encrypt(data, DEFAULT_KEY, DEFAULT_IV);
    }

    /**
     * 默认的 KEY 和 IV 加密
     */
    public static String desEncrypt(String data) throws Exception {
        return desEncrypt(data, DEFAULT_KEY, DEFAULT_IV);
    }

    /**
     * 加密
     *
     * @param data the data
     * @param key  the key
     * @param iv   the iv
     * @return the string
     * @throws Exception the exception
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = data.getBytes();
        int length = dataBytes.length;
        if (length % blockSize != 0) {
            length = length + (blockSize - (length % blockSize));
        }
        byte[] plaintext = new byte[length];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext);
        return new Base64().encodeToString(encrypted);
    }

    /**
     * 解密
     *
     * @param data the data
     * @param key  the key
     * @param iv   the iv
     * @return the string
     * @throws Exception the exception
     */
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        byte[] encrypted1 = new Base64().decode(data);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] bytes = cipher.doFinal(encrypted1);
        return new String(bytes);
    }
}
