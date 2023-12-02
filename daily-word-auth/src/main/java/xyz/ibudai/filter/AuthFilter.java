package xyz.ibudai.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ibudai.common.ResultData;
import xyz.ibudai.util.TokenUtil;

import java.io.IOException;
import java.util.Objects;

@Component
public class AuthFilter implements Filter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * 每次请求读取请求头 Token 验证是否登录
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        int status;
        String msg;
        String token = req.getHeader("Token");
        if (Objects.nonNull(token) && !token.isBlank()) {
            boolean isExpired = false;
            try {
                TokenUtil.parseJWT(token);
            } catch (ExpiredJwtException e) {
                isExpired = true;
            }
            if (!isExpired) {
                filterChain.doFilter(req, servletResponse);
                return;
            } else {
                status = 203;
                msg = "Login expired.";
            }
        } else {
            status = 203;
            msg = "Please login and try again.";
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        ResultData<Object> result = new ResultData<>(status, msg, null);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
