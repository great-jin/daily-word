package xyz.ibudai.dailyword.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.model.vo.RegisterVo;

/**
 * (TbUser)表服务接口
 *
 * @author makejava
 * @since 2023 -01-31 14:31:28
 */
public interface AuthenticService extends UserDetailsService {

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
     * @param registerVo the register vo
     * @return the boolean
     */
    Integer register(RegisterVo registerVo);

    /**
     * Forgot boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean forgot(UserDetail user);
}
