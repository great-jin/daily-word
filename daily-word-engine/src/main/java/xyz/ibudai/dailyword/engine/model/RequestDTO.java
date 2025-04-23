package xyz.ibudai.dailyword.engine.model;

import lombok.Data;
import xyz.ibudai.dailyword.engine.enums.Language;

@Data
public class RequestDTO {

    private String text;

    private Language targetType;

}
