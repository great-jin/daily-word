package xyz.ibudai.dailyword.engine.model;

import lombok.Data;
import xyz.ibudai.dailyword.engine.enums.Language;

@Data
public class ResponseDTO {

    private Language sourceType;

    private String sourceText;

    private Language targetType;

    private String targetText;

}
