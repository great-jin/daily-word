package xyz.ibudai.dailyword.model.vo.board;

import lombok.Data;

/**
 * The type Rank board vo.
 */
@Data
public class RankBoardVo {

    private String index;

    private String avatarUrl;

    private String userName;

    private Integer score;

    private Integer matchCount;
}
