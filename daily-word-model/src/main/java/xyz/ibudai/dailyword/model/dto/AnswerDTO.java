package xyz.ibudai.dailyword.model.dto;

import lombok.Data;
import xyz.ibudai.dailyword.model.mongo.SubjectContent;

import java.util.List;

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
     * 耗费时间
     */
    private Integer costTime;

    /**
     * 得分
     */
    private Integer score;

    /**
     * The Content list.
     */
    List<SubjectContent> contentList;

}
