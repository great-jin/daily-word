package xyz.ibudai.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.ibudai.filter.AuthFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class FilterConfig {

    @Value("${auth.filter.login}")
    private String loginApi;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public FilterRegistrationBean<AuthFilter> orderFilter1() {
        FilterRegistrationBean<AuthFilter> filter = new FilterRegistrationBean<>();
        filter.setName("auth-filter");
        // Set effect url
        filter.setUrlPatterns(Collections.singleton("/**"));
        // Set ignore url, when multiply the value spilt with ","
        String[] urls = loginApi.split(",");
        if (!StringUtils.isBlank(contextPath)) {
            if (urls.length > 0) {
                urls = Arrays.stream(urls)
                        .map(it -> contextPath + it)
                        .toArray(String[]::new);
            }
        }
        filter.addInitParameter("excludedUris", StringUtils.join(urls, ","));
        filter.setOrder(-1);
        filter.setFilter(new AuthFilter());
        return filter;
    }
}
