package xyz.ibudai.dailyword.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.ibudai.dailyword.auth.config.handler.AuthExceptionHandler;
import xyz.ibudai.dailyword.auth.config.handler.LoginFailureHandler;
import xyz.ibudai.dailyword.auth.config.handler.LoginSuccessHandler;
import xyz.ibudai.dailyword.auth.encrypt.AESEncoder;
import xyz.ibudai.dailyword.auth.filter.TokenFilter;
import xyz.ibudai.dailyword.auth.model.ApiProperties;
import xyz.ibudai.dailyword.auth.service.AuthenticService;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    private static final String COMMA = ",";

    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private AuthenticService authenticService;

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private AuthExceptionHandler authExceptionHandler;


    @Bean
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    /**
     * 创建用户认证提供者
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 创建一个用户认证提供者
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 设置动态用户信息
        authProvider.setUserDetailsService(authenticService);
        // 设置加密机制
        authProvider.setPasswordEncoder(new AESEncoder());
        return authProvider;
    }

    /**
     * 配置忽略的地址
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        String[] ignoredApis = apiProperties.getExcludes().split(COMMA);
        return (web) -> web.ignoring().requestMatchers(ignoredApis);
    }

    /**
     * Security 链路配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 解析接口名单
        String[] freeResource = Arrays.stream(apiProperties.getFreeApi().trim().split(COMMA))
                .toArray(String[]::new);
        String[] userResource = Arrays.stream(apiProperties.getUserApi().trim().split(COMMA))
                .toArray(String[]::new);
        String[] adminResource = Arrays.stream(apiProperties.getAdminApi().trim().split(COMMA))
                .toArray(String[]::new);

        // 配置 security 作用规则
        http
                .authorizeHttpRequests(auth -> {
                    // permitAll(): 任意角色都可访问
                    auth.requestMatchers(freeResource).permitAll();
                    // 为不同权限分配不同资源
                    auth.requestMatchers(userResource).hasRole("USER");
                    auth.requestMatchers(adminResource).hasRole("ADMIN");
                    // 默认无定义资源都需认证
                    auth.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> {
                    // 配置登录接口
                    form.loginProcessingUrl(apiProperties.getLoginApi()).permitAll();
                    // 登录成功处理逻辑
                    form.successHandler(loginSuccessHandler);
                    // 登录失败处理逻辑
                    form.failureHandler(loginFailureHandler);
                })
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(handle -> {
                    // 无认证异常处理逻辑
                    handle.authenticationEntryPoint(authExceptionHandler);
                })
                // 优先认证登录
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 关闭跨站攻击
                .csrf(AbstractHttpConfigurer::disable)
                // 允许跨域
                .cors(Customizer.withDefaults());
        return http.build();
    }
}
