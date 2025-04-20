package xyz.ibudai.dailyword.model.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "dictionary.file")
public class DictionaryProps {

    /**
     * 词典路径
     */
    private String dictionaryPath;

    /**
     * 词汇本路径
     */
    private String vocabularyPath;
}
