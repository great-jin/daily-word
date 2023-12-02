package xyz.ibudai.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import xyz.ibudai.entity.AuthUser;

/**
 * (TbUser)表服务接口
 *
 * @author makejava
 * @since 2023-01-31 14:31:28
 */
public interface AuthUserService extends UserDetailsService {

    boolean login(AuthUser user) throws Exception;
}
