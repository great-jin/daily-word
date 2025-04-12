package xyz.ibudai.dailyword.model.vo;

import lombok.Data;

@Data
public class RegisterVo {

    private String username;

    private String password;

    private String email;

    private String captcha;

    private String inviteCode;

}
