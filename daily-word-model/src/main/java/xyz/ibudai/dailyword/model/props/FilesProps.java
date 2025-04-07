package xyz.ibudai.dailyword.model.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "files")
public class FilesProps {

    private String host;

    private String secret;

    private String home;

    private String avatarDir;

    private Double scale;

    private Double quality;
}
