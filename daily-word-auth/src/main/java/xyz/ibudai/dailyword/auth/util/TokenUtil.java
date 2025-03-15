package xyz.ibudai.dailyword.auth.util;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TokenUtil {

    /**
     * 密钥
     */
    public static final String JWT_KEY = "ibudai";
    /**
     * 默认过期时间
     */
    public static final Long JWT_TTL = TimeUnit.HOURS.toMillis(12);


    /**
     * 生成 Token
     *
     * @param data the data
     */
    public static String createJWT(String data) {
        return createJWT(data, null);
    }

    /**
     * 生成 Token
     *
     * @param data      the data
     * @param ttlMillis the expired time
     */
    public static String createJWT(String data, Long ttlMillis) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        JwtBuilder builder = getJwtBuilder(data, ttlMillis, uuid);
        return builder.compact();
    }

    /**
     * 解析 Token
     *
     * @param token the token
     */
    public static Claims parseJWT(String token) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成加密后的秘钥
     */
    private static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)
                // 计算内容
                .setSubject(subject)
                // 签发者
                .setIssuer("budai")
                // 签发时间
                .setIssuedAt(now)
                // 加密算法签名
                .signWith(algorithm, secretKey)
                .setExpiration(expDate);
    }
}
