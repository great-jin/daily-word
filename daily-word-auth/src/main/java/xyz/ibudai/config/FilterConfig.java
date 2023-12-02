package xyz.ibudai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.ibudai.filter.AuthFilter;

import java.util.Collections;

@Configuration
public class FilterConfig {

    @Value("${auth.api.login}")
    private String loginApi;

    @Bean
    public FilterRegistrationBean<AuthFilter> orderFilter1() {
        FilterRegistrationBean<AuthFilter> filter = new FilterRegistrationBean<>();
        filter.setName("auth-filter");
        // Set effect url
        filter.setUrlPatterns(Collections.singleton("/**"));
        // Set ignore url, when multiply the value spilt with ","
        filter.addInitParameter("excludedUris", loginApi);
        filter.setOrder(-1);
        filter.setFilter(new AuthFilter());
        return filter;
    }
}
