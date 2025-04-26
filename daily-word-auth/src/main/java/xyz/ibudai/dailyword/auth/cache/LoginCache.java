package xyz.ibudai.dailyword.auth.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LoginCache {

    private final static Cache<Integer, String> LOGIN_MAP = Caffeine.newBuilder()
            .expireAfterWrite(12, TimeUnit.HOURS)
            .initialCapacity(12)
            .build();


    public static String read(Integer userId) {
        return LOGIN_MAP.getIfPresent(userId);
    }

    /**
     * Login string.
     *
     * @param userId the user id
     * @return the string
     */
    public static String login(Integer userId) {
        String key = userId + "" + System.currentTimeMillis();
        String uuid = UUID.nameUUIDFromBytes(key.getBytes()).toString();

        // 不管是否存在直接覆盖，使得旧值无效
        LOGIN_MAP.put(userId, uuid);
        return uuid;
    }

    /**
     * Logout.
     *
     * @param userId the user id
     */
    public static void logout(Integer userId) {
        LOGIN_MAP.invalidate(userId);
    }
}
