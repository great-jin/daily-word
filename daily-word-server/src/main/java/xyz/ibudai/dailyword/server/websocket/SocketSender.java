package xyz.ibudai.dailyword.server.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * The type Web socket sender.
 */
@Component
public class SocketSender {

    private final static Logger log = LoggerFactory.getLogger(SocketSender.class);

    /**
     * 群发自定义消息
     *
     * @param key     the key
     * @param message the message
     * @return the boolean
     */
    public boolean send(String key, String message) {
        if (SocketServer.websocketMap.isEmpty()) {
            log.error("Currently didn't have valid session.");
            return false;
        }

        boolean success = false;
        try {
            SocketServer server = SocketServer.websocketMap.get(key);
            if (Objects.isNull(server)) {
                log.info("The session of [{}] not existed.", key);
                return false;
            }

            server.sendMessage(message);
            success = true;
        } catch (IOException e) {
            log.error("Session send() error, stack tree: ", e);
        }
        return success;
    }

    /**
     * Batch send boolean.
     *
     * @param message the message
     * @return the boolean
     */
    public boolean batchSend(String message) {
        if (SocketServer.websocketMap.isEmpty()) {
            log.error("Currently didn't have valid session.");
            return false;
        }

        boolean success = true;
        for (SocketServer item : SocketServer.websocketMap.values()) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                success = false;
                log.error("Session batchSend() error, stack tree: ", e);
            }
        }
        return success;
    }
}
