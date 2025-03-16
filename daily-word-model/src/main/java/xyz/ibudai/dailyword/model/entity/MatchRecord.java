package xyz.ibudai.dailyword.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * (MatchRecord)表实体类
 *
 * @author ibudai
 * @since 2025-03-16 09:26:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("match_record")
public class MatchRecord extends Model<MatchRecord> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("group_id")
    private String groupId;

    @TableField("room_id")
    private Integer roomId;

    @TableField("user_id")
    private Integer userId;

    @TableField("rank_type")
    private Integer rankType;

    @TableField("rank_mode")
    private Integer rankMode;

    @TableField("catalog_id")
    private Integer catalogId;

    @TableField("word_count")
    private Integer wordCount;

    @TableField("cost_second")
    private Integer costSecond;

    @TableField("score")
    private Integer score;

    @TableField("create_time")
    private LocalDateTime createTime;

}

