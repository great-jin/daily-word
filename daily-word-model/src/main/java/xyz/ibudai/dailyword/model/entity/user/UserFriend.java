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
 * (UserFriend)表实体类
 *
 * @author ibudai
 * @since 2025-03-16 09:26:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_friend")
public class UserFriend extends Model<UserFriend> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("friend_id")
    private Integer friendId;

    @TableField("create_time")
    private LocalDateTime createTime;

}

