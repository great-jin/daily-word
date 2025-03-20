package xyz.ibudai.dailyword.socket.adaptor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import xyz.ibudai.dailyword.socket.enums.AttrKey;
import xyz.ibudai.dailyword.socket.enums.Protocol;
import xyz.ibudai.dailyword.socket.manager.ChannelManager;

/**
 * The interface Channel adaptor.
 */
@Slf4j
public abstract class ChannelAdaptor extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * Gets protocol.
     *
     * @return the protocol
     */
    public abstract Protocol getProtocol();


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            Channel channel = ctx.channel();
            Object authed = channel.attr(AttributeKey.valueOf(AttrKey.AUTHED.getKey())).get();
            if (Boolean.FALSE.equals(authed)) {
                log.warn("The socket not authenticated");
                ChannelManager.send(channel, "Not Authenticated");
                channel.close();
                return;
            }

            ChannelManager.send(channel, "Connect success");
            return;
        }

        super.userEventTriggered(ctx, evt);
    }

    /**
     * 连接断开事件
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        ChannelManager.remove(getProtocol(), ctx.channel().id().asLongText());
    }

    /**
     * 异常事件处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.handlerRemoved(ctx);
        ChannelManager.send(ctx.channel(), cause.getMessage());

        super.exceptionCaught(ctx, cause);
    }
}