package xyz.ibudai.dailyword.socket.adaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import xyz.ibudai.dailyword.model.base.ResponseData;
import xyz.ibudai.dailyword.socket.enums.AttrKey;
import xyz.ibudai.dailyword.socket.enums.Protocol;
import xyz.ibudai.dailyword.socket.manager.ChannelManager;

/**
 * The interface Channel adaptor.
 */
@Slf4j
public abstract class ChannelAdaptor extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private ObjectMapper objectMapper;

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
                ResponseData res = new ResponseData(HttpStatus.UNAUTHORIZED.value());
                ChannelManager.send(channel, objectMapper.writeValueAsString(res));
                channel.close();
                return;
            }

            ResponseData res = new ResponseData(HttpStatus.OK.value());
            ChannelManager.send(channel, objectMapper.writeValueAsString(res));
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