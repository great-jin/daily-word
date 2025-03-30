package xyz.ibudai.dailyword.model.dto;

import lombok.Data;

@Data
public class AnswerDTO {

    private String matchId;

    private Integer correctCount;

    private Integer costTime;

    private Integer score;

}
