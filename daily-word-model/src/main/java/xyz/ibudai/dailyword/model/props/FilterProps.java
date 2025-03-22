package xyz.ibudai.dailyword.model.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Api properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "resources.filter")
public class FilterProps {

    private String[] excludes;

}
