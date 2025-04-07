package xyz.ibudai.dailyword.model.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "resources.jwt")
public class JwtProps {

    private String secret;

    private String issuer;
}
