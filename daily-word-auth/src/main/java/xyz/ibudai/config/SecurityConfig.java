package xyz.ibudai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import xyz.ibudai.common.ResultData;
import xyz.ibudai.entity.AuthUser;
import xyz.ibudai.entity.AuthUserDTO;
import xyz.ibudai.service.AuthUserService;
import xyz.ibudai.util.AESUtil;
import xyz.ibudai.util.TokenUtil;
import xyz.ibudai.util.encrypt.AESEncoder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${auth.api.login}")
    private String loginAPI;

    @Value("${auth.api.free}")
    private String freeAPIs;

    @Value("${auth.api.user}")
    private String userAPIs;

    @Value("${auth.api.admin}")
    private String adminAPIs;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthUserService authUserService;

    /**
     * Security 3.x: authenticationManager() + authenticationProvider()
     * <p>
     * Security 2.x: configure(AuthenticationManagerBuilder auth)
     */
    @Bean
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 创建一个用户认证提供者
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 设置动态用户信息
        authProvider.setUserDetailsService(authUserService);
        // 设置加密机制
        authProvider.setPasswordEncoder(new AESEncoder());
        return authProvider;
    }

    /**
     * Security 3.x 通过注入 SecurityFilterChain 对象配置规则
     * Security 2.x 通过继承 WebSecurityConfigurerAdapter 并重写 configure(HttpSecurity) 实现
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 解析配置接口名单
        String[] freeUrls = freeAPIs.trim().split(",");
        String[] userUrls = userAPIs.trim().split(",");
        String[] adminUrls = adminAPIs.trim().split(",");
        if (!StringUtils.isBlank(contextPath)) {
            if (freeUrls.length > 0) {
                freeUrls = Arrays.stream(freeUrls)
                        .map(it -> contextPath + "/" + it)
                        .toArray(String[]::new);
            }
            if (userUrls.length > 0) {
                userUrls = Arrays.stream(userUrls)
                        .map(it -> contextPath + "/" + it)
                        .toArray(String[]::new);
            }
            if (adminUrls.length > 0) {
                adminUrls = Arrays.stream(adminUrls)
                        .map(it -> contextPath + "/" + it)
                        .toArray(String[]::new);
            }
        }

        // 配置 security 作用规则
        final String[] freeResource = freeUrls;
        final String[] userResource = userUrls;
        final String[] adminResource = adminUrls;
        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(freeResource).permitAll();
                    // 为不同权限分配不同资源
                    auth.requestMatchers(userResource).hasRole("USER");
                    auth.requestMatchers(adminResource).hasRole("ADMIN");
                    // 默认无定义资源都需认证
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> {
                    // 配置登录接口
                    form.loginProcessingUrl(loginAPI).permitAll();
                    // 登录成功处理逻辑
                    form.successHandler(this::successHandle);
                    // 登录失败处理逻辑
                    form.failureHandler(this::failureHandle);
                })
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(handle -> {
                    // 无认证异常处理逻辑
                    handle.authenticationEntryPoint(this::unAuthHandle);
                })
                // 关闭跨站攻击
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * 认证登录成功处理
     */
    private void successHandle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
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
        response.addHeader("token", token);
        String auth = user.getUsername() + ":" + user.getPassword();
        response.addHeader("auth", Base64.getEncoder().encodeToString(auth.getBytes()));
        response.setContentType("application/json;charset=UTF-8");
        ResultData<Object> result = new ResultData<>(200, "success.", true);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    /**
     * 认证登录失败处理
     */
    private void failureHandle(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String msg;
        // 有认证信息但验证不通过，根据对应类型进行提示
        if (exception instanceof LockedException) {
            msg = "Account has been locked, please contact the administrator.";
        } else if (exception instanceof BadCredentialsException) {
            msg = "Account credential error, please recheck.";
        } else {
            msg = "Account doesn't exist, please recheck.";
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(203);
        ResultData<Object> result = new ResultData<>(203, msg, null);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    /**
     * 未认证访问资源处理
     */
    private void unAuthHandle(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // 无认证信息则提示进行登录
        String msg = "Authorization failure, please login and try again.";
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(203);
        ResultData<Object> result = new ResultData<>(203, msg, null);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
