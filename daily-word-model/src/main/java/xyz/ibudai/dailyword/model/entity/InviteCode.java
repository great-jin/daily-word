package xyz.ibudai.dailyword.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.ibudai.dailyword.model.enums.status.InviteCodeStatus;

import java.time.LocalDate;

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

    @TableField("generate_date")
    private LocalDate generateDate;

    @TableField("expire_date")
    private LocalDate expireDate;

    @TableField("status")
    private Integer status;


    public static InviteCode init(Integer userId) {
        LocalDate now = LocalDate.now();

        InviteCode inviteCode = new InviteCode();
        inviteCode.setUserId(userId);
        inviteCode.setGenerateDate(now);
        // 七天过期
        inviteCode.setExpireDate(now.plusDays(7));
        inviteCode.setStatus(InviteCodeStatus.UN_USE.getStatus());
        return inviteCode;
    }
}

