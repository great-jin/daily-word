package xyz.ibudai.dailyword.server.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint(value = "/websocket")
public class SocketServer {

    private final static Logger log = LoggerFactory.getLogger(SocketServer.class);

    /**
     * 当前在线连接数
     */
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 存放客户端对应的 WebSocket 对象
     */
    public static final Map<String, SocketServer> websocketMap = new ConcurrentHashMap<>();

    /**
     * 会话标识
     */
    private String userId;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        // 添加会话至容器
        this.session = session;
        String queryString = session.getQueryString();
        userId = queryString.split("=")[1];
        websocketMap.put(userId, this);
        // 计数器自增
        onlineCount.incrementAndGet();

        try {
            log.info("Receive new session, current connected number: {}", getOnlineCount());
            sendMessage("You have successfully connected.");
        } catch (IOException e) {
            log.error("Websocket IO exception, stack tree: ", e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 从容器中删除会话
        websocketMap.remove(userId);
        // 计数器自减
        onlineCount.decrementAndGet();
        log.info("New session close, current connected number: {}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Receive from client, message: {}", message);

        // 群发消息
        for (SocketServer item : websocketMap.values()) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                log.error("Session onMessage, stack tree: ", e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Session {} onError,", session, error);
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote()
                .sendText(message);
    }

    public int getOnlineCount() {
        return onlineCount.get();
    }
}
