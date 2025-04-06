package xyz.ibudai.dailyword.model.vo.match;

import lombok.Data;

@Data
public class MatchRecordVo {

    private Integer matchId;

    private String rankMode;

    private Integer rankType;

    private String catalog;

    private Integer wordCount;

    private Integer costSecond;

    private Integer correctCount;

    private Integer score;

}
