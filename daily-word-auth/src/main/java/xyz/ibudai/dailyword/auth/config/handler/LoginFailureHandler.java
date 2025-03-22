package xyz.ibudai.dailyword.auth.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.basic.enums.ContentType;
import xyz.ibudai.dailyword.basic.enums.LoginStatus;
import xyz.ibudai.dailyword.basic.common.ResponseData;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg;
        // 有认证信息但验证不通过，根据对应类型进行提示
        if (exception instanceof LockedException) {
            msg = LoginStatus.LOCKED.getMsg();
        } else if (exception instanceof BadCredentialsException) {
            msg = LoginStatus.BAD_CREDENTIAL.getMsg();
        } else {
            msg = LoginStatus.NOT_EXIST.getMsg();
        }

        response.setContentType(ContentType.JSON.getVal());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseData result = ResponseData.denied(msg);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
