package xyz.ibudai.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import xyz.ibudai.common.ResultData;
import xyz.ibudai.util.TokenUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
@Component
public class TokenFilter implements Filter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${auth.filter.ignored}")
    private String excludesApi;

    @Value("${server.servlet.context-path}")
    private String contextPath;

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
        if (excludesUrl(req.getRequestURI())) {
            filterChain.doFilter(req, servletResponse);
            return;
        }

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

    private boolean excludesUrl(String path) {
        boolean isMarch = false;
        try {
            String[] excludesResource = excludesApi.split(",");
            if (!StringUtils.isBlank(contextPath)) {
                if (excludesResource.length > 0) {
                    excludesResource = Arrays.stream(excludesResource)
                            .map(it -> contextPath + it)
                            .toArray(String[]::new);
                }
            }

            for (String pattern : excludesResource) {
                isMarch = pathMatcher.match(pattern, path);
                if (isMarch) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Verify path failed", e);
        }
        return isMarch;
    }
}
