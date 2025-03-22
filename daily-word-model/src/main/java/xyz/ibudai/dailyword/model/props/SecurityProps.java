package xyz.ibudai.dailyword.model.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Api properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "resources.security")
public class SecurityProps {

    private String loginUrl;

    private String[] excludeUrls;

    private String[] commonUrls;

    private String[] userUrls;

    private String[] adminUrls;

}
