package xyz.ibudai.dailyword.auth.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.basic.enums.ContentType;
import xyz.ibudai.dailyword.basic.enums.HttpHeader;
import xyz.ibudai.dailyword.auth.util.AESUtil;
import xyz.ibudai.dailyword.auth.util.TokenUtil;
import xyz.ibudai.dailyword.basic.common.ResponseData;
import xyz.ibudai.dailyword.model.dto.AuthUserDTO;
import xyz.ibudai.dailyword.model.entity.AuthUser;

import java.io.IOException;
import java.util.Base64;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        String refreshToken;
        AuthUserDTO userDTO;
        try {
            userDTO = AuthUserDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .password(AESUtil.desEncrypt(user.getPassword()).trim())
                    .build();
            String key = objectMapper.writeValueAsString(userDTO);
            refreshToken = TokenUtil.createJWT(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 回传用户信息
        userDTO.setPassword(null);
        userDTO.setRefreshToken(refreshToken);
        String auth = user.getUsername() + ":" + user.getPassword();
        userDTO.setAuthentic(Base64.getEncoder().encodeToString(auth.getBytes()));
        ResponseData result = ResponseData.success(userDTO);
        response.setContentType(ContentType.JSON.getVal());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
