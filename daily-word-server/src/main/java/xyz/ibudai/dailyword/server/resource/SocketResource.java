package xyz.ibudai.dailyword.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.dailyword.server.websocket.SocketSender;

@RestController
@RequestMapping("/api/server/websocket")
public class SocketResource {

    @Autowired
    private SocketSender socketSender;


    /**
     * Send boolean.
     *
     * @param userId  the user id
     * @param message the message
     * @return the boolean
     */
    @GetMapping("send")
    public Boolean send(@RequestParam("userId") String userId,
                        @RequestParam("message") String message) {
        socketSender.send(userId, message);
        return true;
    }
}
