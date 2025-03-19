package xyz.ibudai.dailyword.socket.adaptor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import xyz.ibudai.dailyword.socket.enums.AttrKey;
import xyz.ibudai.dailyword.socket.manager.ChannelManager;

/**
 * The interface Channel adaptor.
 */
@Slf4j
public abstract class ChannelAdaptor extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * Gets uri.
     *
     * @return the uri
     */
    public abstract String getUri();


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            Channel channel = ctx.channel();
            Object authed = channel.attr(AttributeKey.valueOf(AttrKey.AUTHED.getKey())).get();
            if (Boolean.FALSE.equals(authed)) {
                ctx.writeAndFlush(new TextWebSocketFrame("Not Authenticated"));
                channel.close();
                return;
            }

            ctx.writeAndFlush(new TextWebSocketFrame("Handshake success"));
            return;
        }

        super.userEventTriggered(ctx, evt);
    }

    /**
     * 连接断开事件
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        String cId = ctx.channel().id().asLongText();
        ChannelManager.remove(cId);
        log.info("ChannelAdaptor::disconnected, cid: {}", cId);
    }
}