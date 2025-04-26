package xyz.ibudai.dailyword.auth.socket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;
import xyz.ibudai.dailyword.socket.consts.BeanConst;
import xyz.ibudai.dailyword.socket.enums.Protocol;

@Slf4j
@ChannelHandler.Sharable
@Component(BeanConst.Handler.LOGIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginHandler extends ChannelAdaptor {

    @Override
    public Protocol getProtocol() {
        return Protocol.LOGIN;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        log.info("LoginHandler::channelRead, msg: {}", frame.text());
    }
}
