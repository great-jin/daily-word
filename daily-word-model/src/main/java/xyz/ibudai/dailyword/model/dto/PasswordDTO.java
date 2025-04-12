package xyz.ibudai.dailyword.model.dto;

import lombok.Data;

@Data
public class PasswordDTO {

    private String email;

    private String code;

    private String originPwd;

    private String password;
}
