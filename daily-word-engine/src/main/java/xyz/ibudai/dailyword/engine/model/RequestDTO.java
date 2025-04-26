package xyz.ibudai.dailyword.engine.model;

import lombok.Data;
import xyz.ibudai.dailyword.engine.enums.Language;
import xyz.ibudai.dailyword.engine.enums.Model;

@Data
public class RequestDTO {

    private Model model;

    private String text;

    private Language targetType;

}
