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
 * (InviteCode)表实体类
 *
 * @author ibudai
 * @since 2025-03-16 09:26:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("invite_code")
public class InviteCode extends Model<InviteCode> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("code")
    private String code;

    @TableField("generate_time")
    private LocalDateTime generateTime;

    @TableField("expire_day")
    private Integer expireDay;

}

