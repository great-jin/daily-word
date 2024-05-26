package xyz.ibudai.dailyword.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class WebSocketSender {

    private final static Logger log = LoggerFactory.getLogger(WebSocketSender.class);

    /**
     * 群发自定义消息
     */
    public boolean send(String key, String message) {
        if (WebSocketServer.websocketMap.isEmpty()) {
            log.error("Currently didn't have valid session.");
            return false;
        }

        boolean success = false;
        try {
            WebSocketServer server = WebSocketServer.websocketMap.get(key);
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

    public boolean batchSend(String message) {
        if (WebSocketServer.websocketMap.isEmpty()) {
            log.error("Currently didn't have valid session.");
            return false;
        }

        boolean success = true;
        for (WebSocketServer item : WebSocketServer.websocketMap.values()) {
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
