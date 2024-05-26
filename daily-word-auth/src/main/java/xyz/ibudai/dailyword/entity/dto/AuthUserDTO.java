package xyz.ibudai.dailyword.entity.dto;

import lombok.Data;

@Data
public class AuthUserDTO {

    private String username;

    private String password;

    private String role;

}
