package xyz.ibudai.dailyword.auth.util;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.model.props.JwtProps;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenUtil {

    private final JwtProps jwtProps;


    /**
     * 生成 Token
     *
     * @param data the data
     */
    public String createJWT(String data) {
        return createJWT(data, null);
    }

    /**
     * 生成 Token
     *
     * @param data      the data
     * @param ttlMillis the expired time
     */
    public String createJWT(String data, Long ttlMillis) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        JwtBuilder builder = getJwtBuilder(data, ttlMillis, uuid);
        return builder.compact();
    }

    /**
     * 解析 Token
     *
     * @param token the token
     */
    public Claims parseJWT(String token) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成加密后的秘钥
     */
    private SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(jwtProps.getSecret());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    private JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = TimeUnit.HOURS.toMillis(jwtProps.getTtlHours());
        }

        Date expDate = new Date(nowMillis + ttlMillis);
        return Jwts.builder()
                .setId(uuid)
                // 计算内容
                .setSubject(subject)
                // 签发者
                .setIssuer(jwtProps.getIssuer())
                // 签发时间
                .setIssuedAt(now)
                // 加密算法签名
                .signWith(SignatureAlgorithm.HS256, generalKey())
                .setExpiration(expDate);
    }
}
