package xyz.ibudai.dailyword.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class RegisterVo {

    @TableField("user_name")
    private String username;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("invite_code")
    private String inviteCode;

}
