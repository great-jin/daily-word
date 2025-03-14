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
import java.util.concurrent.TimeUnit;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        String token, plainPwd;
        try {
            AuthUserDTO userDTO = new AuthUserDTO();
            plainPwd = AESUtil.desEncrypt(user.getPassword()).trim();
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(plainPwd);
            userDTO.setRole(user.getRole());
            // 验证成功为用户生成过期时间为 60 分钟的 Token
            String key = objectMapper.writeValueAsString(userDTO);
            token = TokenUtil.createJWT(key, TimeUnit.MINUTES.toMillis(60));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 将 Token 写入响应的请求头返回
        response.addHeader(HttpHeader.TOKEN.getBackend(), token);
        String auth = user.getUsername() + ":" + user.getPassword();
        response.addHeader(HttpHeader.AUTHENTIC.getBackend(), Base64.getEncoder().encodeToString(auth.getBytes()));
        response.setContentType(ContentType.JSON.getVal());
        ResponseData result = ResponseData.success(true);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
