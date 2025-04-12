package xyz.ibudai.dailyword.socket.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.socket.enums.Protocol;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ChannelManager {

    private static final Integer INITIAL_SIZE = 12;

    private static final Integer MAX_SIZE = 1024;

    /**
     * Key: ChannelId
     * Value: UserId
     */
    private final static Cache<String, Integer> CU_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .initialCapacity(INITIAL_SIZE)
            .build();

    private final static Cache<Protocol, Map<Integer, Channel>> SERVER = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .initialCapacity(INITIAL_SIZE)
            .build();


    /**
     * Gets uid.
     *
     * @param channelId the channel id
     * @return the uid
     */
    public static Integer getUid(String channelId) {
        return CU_CACHE.getIfPresent(channelId);
    }

    /**
     * Sets channel.
     *
     * @param userId  the user id
     * @param channel the channel
     */
    public static void setChannel(Protocol protocol, Integer userId, Channel channel) {
        if (Objects.isNull(channel)) {
            return;
        }

        String cid = channel.id().asLongText();
        CU_CACHE.put(cid, userId);

        // User - Chanel
        Map<Integer, Channel> ucMap = SERVER.getIfPresent(protocol);
        if (Objects.isNull(ucMap)) {
            ucMap = new ConcurrentHashMap<>();
        }
        ucMap.put(userId, channel);
        SERVER.put(protocol, ucMap);
    }

    /**
     * Gets channel.
     *
     * @param userId the user id
     * @return the channel
     */
    public static Channel getChannel(Protocol protocol, Integer userId) {
        Map<Integer, Channel> map = SERVER.getIfPresent(protocol);
        if (Objects.isNull(map)) {
            return null;
        }

        return map.get(userId);
    }

    /**
     * Remove.
     *
     * @param cid the cid
     */
    public static void remove(Protocol protocol, String cid) {
        Integer userId = CU_CACHE.getIfPresent(cid);
        Map<Integer, Channel> map = SERVER.getIfPresent(protocol);
        if (Objects.nonNull(map)) {
            map.remove(userId);
        }
        if (Objects.nonNull(map) && map.isEmpty()) {
            SERVER.invalidate(protocol);
            return;
        }

        SERVER.put(protocol, map);
    }

    /**
     * Send message.
     *
     * @param userId  the user id
     * @param message the message
     * @return the boolean
     */
    public static boolean send(Protocol protocol, Integer userId, String message) {
        Channel channel = getChannel(protocol, userId);
        return send(channel, message);
    }

    /**
     * Send message.
     *
     * @param channel the channel
     * @param message the message
     * @return the boolean
     */
    public static boolean send(Channel channel, String message) {
        if (Objects.isNull(channel)) {
            return false;
        }

        channel.writeAndFlush(new TextWebSocketFrame(message));
        return true;
    }

    /**
     * Batch send message.
     *
     * @param channelList the channel list
     * @param message     the message
     * @return the boolean
     */
    public static boolean batchSend(List<Channel> channelList, String message) {
        if (CollectionUtils.isEmpty(channelList)) {
            return false;
        }

        for (Channel channel : channelList) {
            send(channel, message);
        }
        return true;
    }
}
