package xyz.ibudai.dailyword.auth.encrypt;

import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.ibudai.dailyword.basic.encrypt.SHAUtil;

import java.util.Objects;

public class HashEncoder implements PasswordEncoder {

    /**
     * @param charSequence user input password
     */
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    /**
     * @param charSequence user input password
     * @param s            database password
     */
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        try {
            return Objects.equals(SHAUtil.hash(s), charSequence.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
