package xyz.ibudai.dailyword.model.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "engine")
public class EngineProps {

    private String host;

    private String opus;

    private String nllb;

    private Long connTimeout;

    private Long readTimeout;

}
