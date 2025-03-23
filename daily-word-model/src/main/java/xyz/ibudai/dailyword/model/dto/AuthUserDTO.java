package xyz.ibudai.dailyword.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDTO {

    private Integer userId;

    private String user;

    private String username;

    private String password;

    private String role;

    private String authentic;

    private String refreshToken;

}
