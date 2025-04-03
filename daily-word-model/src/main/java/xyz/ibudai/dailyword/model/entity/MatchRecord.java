package xyz.ibudai.dailyword.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.time.LocalDateTime;

/**
 * (MatchRecord)表实体类
 *
 * @author ibudai
 * @since 2025-03-16 09:26:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("match_record")
public class MatchRecord extends Model<MatchRecord> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("season")
    private Integer season;

    @TableField("group_id")
    private String groupId;

    /**
     * {@link xyz.ibudai.dailyword.model.enums.RankMode}
     */
    @TableField("rank_mode")
    private String rankMode;

    @TableField("room_number")
    private String roomNumber;

    /**
     * {@link xyz.ibudai.dailyword.model.enums.RankType}
     */
    @TableField("rank_type")
    private Integer rankType;

    @TableField("catalog")
    private String catalog;

    @TableField("word_count")
    private Integer wordCount;

    @TableField("cost_second")
    private Integer costSecond;

    @TableField("correct_count")
    private Integer correctCount;

    @TableField("score")
    private Integer score;

    @TableField("finished")
    private Boolean finished;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

}

