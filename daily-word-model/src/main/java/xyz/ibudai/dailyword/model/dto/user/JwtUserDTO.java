package xyz.ibudai.dailyword.model.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtUserDTO {

    private Integer userId;

    private String key;

    private String username;

    private String password;

    private String role;

    private String authentic;

    private String refreshToken;

}
