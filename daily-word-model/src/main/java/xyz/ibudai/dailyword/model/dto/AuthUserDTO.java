package xyz.ibudai.dailyword.model.dto;

import lombok.Data;

@Data
public class AuthUserDTO {

    private String username;

    private String password;

    private String role;

}
