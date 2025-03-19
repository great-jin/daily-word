package xyz.ibudai.dailyword.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import xyz.ibudai.dailyword.basic.enums.ContentType;
import xyz.ibudai.dailyword.basic.enums.HttpHeader;
import xyz.ibudai.dailyword.basic.enums.LoginStatus;
import xyz.ibudai.dailyword.basic.common.ResponseData;
import xyz.ibudai.dailyword.auth.util.TokenUtil;
import xyz.ibudai.dailyword.model.entity.AuthUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class TokenFilter implements Filter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${auth.filter.excludes}")
    private String excludesApi;

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
        if (this.excludesUrl(req.getRequestURI())) {
            // 免认证服务
            filterChain.doFilter(req, servletResponse);
            return;
        }

        String msg;
        String token = req.getHeader(HttpHeader.TOKEN.getFrontend());
        if (Objects.nonNull(token) && !token.isBlank()) {
            boolean isValid = true;
            try {
                TokenUtil.parseJWT(token);
            } catch (ExpiredJwtException e) {
                isValid = false;
            }
            if (isValid) {
                // Token verify success
                this.setUserContext(req.getHeader(HttpHeader.AUTHENTIC.getFrontend()));
                filterChain.doFilter(req, servletResponse);
                return;
            }

            msg = LoginStatus.EXPIRE.getMsg();
        } else {
            msg = LoginStatus.NOT_LOGIN.getMsg();
        }
        response.setContentType(ContentType.JSON.getVal());
        response.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
        ResponseData result = ResponseData.denied(msg);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }


    /**
     * Excludes url boolean.
     *
     * @param path the path
     * @return the boolean
     */
    private boolean excludesUrl(String path) {
        boolean isMarch = false;
        try {
            String[] excludesResource = Arrays.stream(excludesApi.split(","))
                    .map(it -> contextPath + it)
                    .toArray(String[]::new);

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

    /**
     * 设置登录信息至上下文
     *
     * @param authentic the authentic
     * @throws JsonProcessingException the json processing exception
     */
    private void setUserContext(String authentic) throws JsonProcessingException {
        if (StringUtils.isBlank(authentic)) {
            return;
        }

        Claims claims = TokenUtil.parseJWT(authentic);
        AuthUser authUser = objectMapper.readValue(claims.getSubject(), AuthUser.class);
        // 设置用户上下文信息
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
