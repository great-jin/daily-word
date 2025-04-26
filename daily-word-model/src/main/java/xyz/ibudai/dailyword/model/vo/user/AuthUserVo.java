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

    /**
     * The value of userId after AES encryption
     */
    private String key;

    /**
     * The value for Spring Security
     */
    private String authentic;

    /**
     * The value for re login verify
     */
    private String verifyToken;

    /**
     * The value for JWT Verify
     */
    private String refreshToken;

}
