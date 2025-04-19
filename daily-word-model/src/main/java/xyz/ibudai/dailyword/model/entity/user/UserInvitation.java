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
 * (UserInvitation)表实体类
 *
 * @author budai
 * @since 2025-04-12 09:02:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_invitation")
public class UserInvitation extends Model<UserInvitation> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("from_user")
    private Integer fromUser;

    @TableField("target_user")
    private Integer targetUser;

    @TableField("process_status")
    private Integer processStatus;

    @TableField("create_time")
    private LocalDateTime createTime;
}

