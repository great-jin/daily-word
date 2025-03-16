package xyz.ibudai.dailyword.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthUserDTO {

    private Integer userId;

    private String username;

    private String password;

    private String role;

    private String authentic;

    private String refreshToken;

}
