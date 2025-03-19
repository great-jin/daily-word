package xyz.ibudai.dailyword.socket.mode;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.dailyword.socket.enums.Protocol;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private Protocol protocol;

    private Integer userId;

    private Channel channel;
}
