package xyz.ibudai.dailyword.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserVo {

    private Integer userId;

    private String key;

    private String username;

    private String password;

    private String role;

    private String authentic;

    private String refreshToken;

}
