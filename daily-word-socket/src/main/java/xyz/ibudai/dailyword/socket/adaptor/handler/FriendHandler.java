package xyz.ibudai.dailyword.socket.adaptor.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;
import xyz.ibudai.dailyword.socket.enums.Protocol;

@Slf4j
@ChannelHandler.Sharable
public class FriendHandler extends ChannelAdaptor {

    @Override
    public Protocol getProtocol() {
        return Protocol.FRIEND;
    }

    /**
     * 接收消息事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String msg = "FriendHandler received: " + frame.text();

        // 发送响应消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }
}
