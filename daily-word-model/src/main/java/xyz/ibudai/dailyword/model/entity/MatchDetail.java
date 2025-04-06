package xyz.ibudai.dailyword.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

/**
 * (MatchDetail)表实体类
 *
 * @author budai
 * @since 2025-04-06 21:10:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("match_detail")
public class MatchDetail extends Model<MatchDetail> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

    @TableField("word_indies")
    private String wordIndies;

}

