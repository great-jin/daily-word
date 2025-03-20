package xyz.ibudai.dailyword.socket.manager;

import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;
import xyz.ibudai.dailyword.socket.enums.Protocol;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class AdaptorManager {

    private static final Map<String, ChannelAdaptor> MAP = new ConcurrentHashMap<>();

    static {
        String prefix = Protocol.PREFIX.getUri();
        ServiceLoader<ChannelAdaptor> loaders = ServiceLoader.load(ChannelAdaptor.class);
        for (ChannelAdaptor loader : loaders) {
            MAP.put(prefix + loader.getProtocol().getUri(), loader);
        }
    }


    /**
     * Gets adaptor.
     *
     * @param protocol the protocol
     * @return the adaptor
     */
    public static ChannelAdaptor getAdaptor(Protocol protocol) {
        return MAP.get(Protocol.PREFIX.getUri() + protocol.getUri());
    }
}
