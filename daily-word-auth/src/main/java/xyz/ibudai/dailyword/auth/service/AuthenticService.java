package xyz.ibudai.dailyword.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import xyz.ibudai.dailyword.model.dto.PasswordDTO;
import xyz.ibudai.dailyword.model.enums.status.EmailCodeStatus;
import xyz.ibudai.dailyword.model.enums.status.PasswordStatus;
import xyz.ibudai.dailyword.model.enums.status.RegisterStatus;
import xyz.ibudai.dailyword.model.vo.RegisterVo;

/**
 * (TbUser)表服务接口
 *
 * @author budai
 * @since 2023 -01-31 14:31:28
 */
public interface AuthenticService extends UserDetailsService {

    /**
     * Validate email
     *
     * @param type    the type
     * @param address the address
     * @return the email code status
     */
    EmailCodeStatus validateEmail(Integer type, String address);

    /**
     * Send mail boolean.
     *
     * @param type    the type
     * @param address the mail
     * @return the boolean
     */
    EmailCodeStatus sendMail(Integer type, String address);

    /**
     * Register .
     *
     * @param registerVo the register vo
     * @return the boolean
     */
    RegisterStatus register(RegisterVo registerVo);

    /**
     * Forgot .
     *
     * @param dto the dto
     * @return the boolean
     */
    PasswordStatus forgot(PasswordDTO dto);
}
