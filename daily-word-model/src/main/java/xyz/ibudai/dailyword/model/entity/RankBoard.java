package xyz.ibudai.dailyword.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import xyz.ibudai.dailyword.model.config.SystemConfig;

import java.time.LocalDateTime;

/**
 * (RankBoard)表实体类
 *
 * @author ibudai
 * @since 2025-03-16 09:26:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("rank_board")
public class RankBoard extends Model<RankBoard> {

    @TableField(exist = false)
    private String index;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("user_name")
    private String userName;

    @TableField("season")
    private Integer season;

    @TableField("catalog")
    private String catalog;

    @TableField("score")
    private Integer score;

    @TableField("match_count")
    private Integer matchCount;

    @TableField("match_win")
    private Integer matchWin;

    @TableField("match_lost")
    private Integer matchLost;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


    /**
     * Init rank board.
     *
     * @return the rank board
     */
    public static RankBoard init() {
        return RankBoard.builder()
                .season(SystemConfig.getSeason())
                .score(0)
                .matchCount(0)
                .matchWin(0)
                .matchLost(0)
                .build();
    }
}

