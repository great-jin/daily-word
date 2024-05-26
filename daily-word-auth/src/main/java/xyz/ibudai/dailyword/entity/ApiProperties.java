package xyz.ibudai.dailyword.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "auth.api")
public class ApiProperties {

    private String excludes;

    private String loginApi;

    private String commonApi;

    private String userApi;

    private String adminApi;

}
