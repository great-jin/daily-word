package xyz.ibudai.dailyword.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import xyz.ibudai.dailyword.model.dto.PasswordDTO;
import xyz.ibudai.dailyword.model.vo.RegisterVo;

/**
 * (TbUser)表服务接口
 *
 * @author budai
 * @since 2023 -01-31 14:31:28
 */
public interface AuthenticService extends UserDetailsService {

    /**
     * Send mail boolean.
     *
     * @param type    the type
     * @param address the mail
     * @return the boolean
     */
    Integer sendMail(Integer type, String address);

    /**
     * Register .
     *
     * @param registerVo the register vo
     * @return the boolean
     */
    Integer register(RegisterVo registerVo);

    /**
     * Forgot .
     *
     * @param dto the dto
     * @return the boolean
     */
    Integer forgot(PasswordDTO dto);
}
