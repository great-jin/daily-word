package xyz.ibudai.dailyword.socket.adaptor.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;
import xyz.ibudai.dailyword.socket.consts.BeanConst;
import xyz.ibudai.dailyword.socket.enums.Protocol;

@Slf4j
@ChannelHandler.Sharable
@Component(BeanConst.Handler.DEFAULT)
public class DefaultHandler extends ChannelAdaptor {

    @Override
    public Protocol getProtocol() {
        return Protocol.DEFAULT;
    }

    /**
     * 接收消息事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        log.info("DefaultHandler::channelRead, msg: {}", frame.text());
    }
}
