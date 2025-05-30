package xyz.ibudai.dailyword.auth.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.auth.cache.LoginCache;
import xyz.ibudai.dailyword.basic.encrypt.SHAUtil;
import xyz.ibudai.dailyword.basic.enums.ContentType;
import xyz.ibudai.dailyword.basic.encrypt.AESUtil;
import xyz.ibudai.dailyword.auth.util.TokenUtil;
import xyz.ibudai.dailyword.model.base.ResponseData;
import xyz.ibudai.dailyword.model.dto.user.JwtUserDTO;
import xyz.ibudai.dailyword.model.vo.user.AuthUserVo;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;
import xyz.ibudai.dailyword.repository.dao.UserDetailDao;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenUtil tokenUtil;
    private final ObjectMapper objectMapper;

    private final UserDetailDao userDetailDao;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        String refreshToken;
        JwtUserDTO jwtUser;
        try {
            jwtUser = JwtUserDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    // 密码为 SHA-256 哈希值
                    .password(SHAUtil.hash(user.getPassword().trim()))
                    .build();
            String key = objectMapper.writeValueAsString(jwtUser);
            refreshToken = tokenUtil.createJWT(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 更新登录时间
        userDetailDao.updateOnlineTime(user.getId());

        AuthUserVo userVo = new AuthUserVo();
        try {
            userVo.setKey(AESUtil.encrypt(user.getId().toString()));
            // token = Base64(username:password)
            String auth = jwtUser.getUsername() + ":" + jwtUser.getPassword();
            userVo.setAuthentic(Base64.getEncoder().encodeToString(auth.getBytes()));
            // For reLogin check
            userVo.setVerifyToken(LoginCache.login(user.getId()));
            // For JWT
            userVo.setRefreshToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 回传用户信息
        ResponseData result = ResponseData.success(userVo);
        response.setContentType(ContentType.JSON.getVal());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
