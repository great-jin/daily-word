package xyz.ibudai.dailyword.model.entity.user;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * (UserDetail)表实体类
 *
 * @author ibudai
 * @since 2025-03-16 09:26:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_detail")
public class UserDetail extends Model<UserDetail> {

    @TableId(type = IdType.INPUT)
    private Integer userId;

    @TableField("user_name")
    private String userName;

    @TableField("avatar")
    private String avatar;

    @TableField("email")
    private String email;

    @TableField("invite_code")
    private String inviteCode;

    @TableField("register_time")
    private LocalDateTime registerTime;

    @TableField("last_online_time")
    private LocalDateTime lastOnlineTime;

}

