package xyz.ibudai.dailyword.auth.encrypt;

import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.ibudai.dailyword.auth.util.AESUtil;

import java.util.Objects;

public class AESEncoder implements PasswordEncoder {

    /**
     * @param charSequence user input password
     */
    @Override
    public String encode(CharSequence charSequence) {
        String str = charSequence.toString();
        try {
            String plain;
            if (!Objects.equals(str, "userNotFoundPassword")) {
                plain = AESUtil.desEncrypt(str);
            } else {
                plain = str;
            }
            return AESUtil.encrypt(plain);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param charSequence user input password
     * @param s            database password
     */
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        try {
            String plain = AESUtil.desEncrypt(charSequence.toString());
            String result = AESUtil.encrypt(plain);
            return Objects.equals(result, s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
