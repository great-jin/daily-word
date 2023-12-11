package xyz.ibudai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.websocket.WebSocketSender;

@RestController
@RequestMapping("/api/word/websocket")
public class WebSocketController {

    @Autowired
    private WebSocketSender webSocketSender;

    @GetMapping("send")
    public Boolean send(@RequestParam("userId") String userId, @RequestParam("message") String message) {
        webSocketSender.send(userId, message);
        return true;
    }
}
