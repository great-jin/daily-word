package xyz.ibudai.dailyword.server.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.dailyword.socket.enums.Protocol;
import xyz.ibudai.dailyword.socket.manager.ChannelManager;

@RestController
@RequestMapping("/api/server/websocket")
public class SocketResource {

    @GetMapping("send")
    public Boolean send(Protocol protocol, Integer userId, String message) {
        return ChannelManager.send(protocol, userId, message);
    }
}
