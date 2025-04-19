package xyz.ibudai.dailyword.repository.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;

import java.util.Objects;

public class SecurityUtil {

    /**
     * Get login user id
     *
     * @return the user id
     */
    public static Integer getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new IllegalStateException("尚未登录");
        }
        AuthUser user = (AuthUser) authentication.getPrincipal();
        if (Objects.isNull(user)) {
            throw new IllegalStateException("尚未登录");
        }

        return user.getId();
    }
}
