package xyz.ibudai.dailyword.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.ibudai.dailyword.auth.cache.LoginCache;
import xyz.ibudai.dailyword.basic.enums.ContentType;
import xyz.ibudai.dailyword.basic.enums.HttpHeader;
import xyz.ibudai.dailyword.basic.enums.LoginStatus;
import xyz.ibudai.dailyword.model.base.ResponseData;
import xyz.ibudai.dailyword.auth.util.TokenUtil;
import xyz.ibudai.dailyword.model.dto.user.JwtUserDTO;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;
import xyz.ibudai.dailyword.model.props.FilterProps;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final FilterProps filterProps;

    private final TokenUtil tokenUtil;
    private final ObjectMapper objectMapper;

    private final AuthUserDao authUserDao;


    /**
     * 每次请求读取请求头 Token 验证是否登录
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.isExcludesUrl(request.getRequestURI())) {
            // 免认证服务
            filterChain.doFilter(request, response);
            return;
        }

        String msg;
        String token = request.getHeader(HttpHeader.TOKEN.getFrontend());
        if (Objects.nonNull(token) && !token.isBlank()) {
            boolean isValid = true;
            try {
                tokenUtil.parseJWT(token);
            } catch (ExpiredJwtException e) {
                isValid = false;
            }

            if (isValid) {
                // JWT 合法则验证是否存在旧登录需作废
                if (isNewLogin(request)) {
                    this.setUserContext(request.getHeader(HttpHeader.TOKEN.getFrontend()));
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    // 已在其它入口登录，当前作废
                    msg = LoginStatus.RE_LOGIN.getMsg();
                }
            } else {
                // JWT 过期
                msg = LoginStatus.EXPIRE.getMsg();
            }
        } else {
            msg = LoginStatus.NOT_LOGIN.getMsg();
        }
        response.setContentType(ContentType.JSON.getVal());
        response.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
        ResponseData result = ResponseData.denied(msg);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }


    /**
     * Excludes url boolean.
     *
     * @param path the path
     * @return the boolean
     */
    private boolean isExcludesUrl(String path) {
        boolean isMarch = false;
        try {
            for (String pattern : filterProps.getExcludes()) {
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
     * 是否不存在正在使用中的登录
     *
     * @param request the request
     * @return the boolean
     * @throws IOException the io exception
     */
    private boolean isNewLogin(HttpServletRequest request) throws IOException {
        AuthUser authUser =
                this.getUserContext(request.getHeader(HttpHeader.TOKEN.getFrontend()));
        String exitedToken = LoginCache.read(authUser.getId());
        if (StringUtils.isBlank(exitedToken)) {
            // 未存在登录进程，合法
            return true;
        }

        // 新旧 Token 是否一致
        String verifyToken = request.getHeader(HttpHeader.VERIFY.getFrontend());
        return Objects.equals(exitedToken, verifyToken);
    }

    /**
     * 设置登录信息至上下文
     *
     * @param authentic the authentic
     * @throws JsonProcessingException the json processing exception
     */
    private void setUserContext(String authentic) throws IOException {
        AuthUser authUser = this.getUserContext(authentic);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 读取登录用户信息
     *
     * @param authentic the authentic
     * @return the user context
     * @throws IOException the io exception
     */
    private AuthUser getUserContext(String authentic) throws IOException {
        if (StringUtils.isBlank(authentic)) {
            throw new IllegalStateException("The jwt token invalid");
        }

        Claims claims = tokenUtil.parseJWT(authentic);
        JwtUserDTO jwtUser = objectMapper.readValue(claims.getSubject(), JwtUserDTO.class);

        // 查询用户信息
        AuthUser authUser = authUserDao.selectById(jwtUser.getUserId());
        if (Objects.isNull(authUser)) {
            throw new IllegalStateException("The user not exist");
        }
        return authUser;
    }
}
