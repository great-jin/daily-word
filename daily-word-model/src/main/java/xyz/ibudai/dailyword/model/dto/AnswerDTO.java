package xyz.ibudai.dailyword.model.dto;

import lombok.Data;

/**
 * The type Answer dto.
 */
@Data
public class AnswerDTO {

    /**
     * 匹配 KEY
     */
    private Integer matchId;

    /**
     * 答对数量
     */
    private Integer correctCount;

    /**
     * 耗费时间
     */
    private Integer costTime;

    /**
     * 得分
     */
    private Integer score;

}
