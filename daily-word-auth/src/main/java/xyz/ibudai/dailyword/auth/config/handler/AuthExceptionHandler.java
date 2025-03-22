package xyz.ibudai.dailyword.auth.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.basic.enums.ContentType;
import xyz.ibudai.dailyword.basic.enums.LoginStatus;
import xyz.ibudai.dailyword.basic.common.ResponseData;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthExceptionHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 无认证信息则提示进行登录
        String msg = LoginStatus.NOT_AUTH.getMsg();
        response.setContentType(ContentType.JSON.getVal());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseData result = ResponseData.denied(msg);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
