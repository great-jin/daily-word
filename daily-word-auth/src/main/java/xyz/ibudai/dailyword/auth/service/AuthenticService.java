package xyz.ibudai.dailyword.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import xyz.ibudai.dailyword.repository.entity.AuthUser;

/**
 * (TbUser)表服务接口
 *
 * @author makejava
 * @since 2023 -01-31 14:31:28
 */
public interface AuthenticService extends UserDetailsService {

    /**
     * Login boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean login(AuthUser user);

    /**
     * Send mail boolean.
     *
     * @param address the mail
     * @return the boolean
     */
    boolean sendMail(String address);

    /**
     * Register boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean register(AuthUser user);

    /**
     * Forgot boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean forgot(AuthUser user);
}
