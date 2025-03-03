package xyz.ibudai.dailyword.auth.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Api properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth.api")
public class ApiProperties {

    private String excludes;

    private String loginApi;

    private String freeApi;

    private String userApi;

    private String adminApi;

}
