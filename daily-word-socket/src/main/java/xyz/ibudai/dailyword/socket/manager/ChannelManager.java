package xyz.ibudai.dailyword.socket.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ChannelManager {

    // TODO Cache 添加类型

    private final static Cache<Integer, String> UC_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .initialCapacity(12)
            .maximumSize(128)
            .build();

    private final static Cache<String, Channel> CC_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .initialCapacity(12)
            .maximumSize(128)
            .build();

    public static final Map<String, ChannelAdaptor> ADAPTOR_MAP = new ConcurrentHashMap<>();

    static {
        ServiceLoader<ChannelAdaptor> loaders = ServiceLoader.load(ChannelAdaptor.class);
        for (ChannelAdaptor loader : loaders) {
            ADAPTOR_MAP.put(loader.getUri(), loader);
        }
    }


    public static String getUriPath(String uri) {
        try {
            return new URI(uri).getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets channel.
     *
     * @param userId the user id
     * @return the channel
     */
    public static Channel getChannel(Integer userId) {
        return CC_CACHE.getIfPresent(UC_CACHE.getIfPresent(userId));
    }

    /**
     * Sets channel.
     *
     * @param userId  the user id
     * @param channel the channel
     */
    public static void setChannel(Integer userId, Channel channel) {
        if (Objects.isNull(channel)) {
            return;
        }

        String cid = channel.id().asLongText();
        UC_CACHE.put(userId, cid);
        CC_CACHE.put(cid, channel);
    }

    /**
     * Remove.
     *
     * @param cid the cid
     */
    public static void remove(String cid) {
        CC_CACHE.invalidate(cid);
    }

    /**
     * Send boolean.
     *
     * @param userId  the user id
     * @param message the message
     * @return the boolean
     */
    public static boolean send(Integer userId, String message) {
        Channel channel = getChannel(userId);
        if (Objects.isNull(channel)) {
            return false;
        }

        channel.writeAndFlush(new TextWebSocketFrame(message));
        return true;
    }
}
