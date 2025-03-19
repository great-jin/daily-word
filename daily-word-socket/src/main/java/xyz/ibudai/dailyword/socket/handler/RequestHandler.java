package xyz.ibudai.dailyword.socket.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AttributeKey;
import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;
import xyz.ibudai.dailyword.socket.adaptor.handler.RankHandler;
import xyz.ibudai.dailyword.socket.enums.AttrKey;
import xyz.ibudai.dailyword.socket.enums.Protocol;
import xyz.ibudai.dailyword.socket.manager.ChannelManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * 执行完连接建立事件后触发
     * <p>
     * ${@link RankHandler#handlerAdded(io.netty.channel.ChannelHandlerContext)}
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 解析 URL 参数
        String uri = request.uri();
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = decoder.parameters();

        boolean valid = false;
        Channel channel = ctx.channel();
        if (parameters.containsKey(AttrKey.UID.getKey())) {
            valid = true;
            // 转存会话信息
            Integer userId = Integer.parseInt(parameters.get(AttrKey.UID.getKey()).get(0));
            ChannelManager.setChannel(userId, channel);
        }

        // 根据不同地址路由
        String path = ChannelManager.getUriPath(uri);
        ChannelAdaptor adaptor = ChannelManager.ADAPTOR_MAP.get(path);
        if (Objects.isNull(adaptor)) {
            // 非法路由
            valid = false;
            adaptor = ChannelManager.ADAPTOR_MAP.get(Protocol.PREFIX.getUri() + Protocol.DEFAULT.getUri());
        }
        channel.attr(AttributeKey.valueOf(AttrKey.AUTHED.getKey())).set(valid);
        ctx.pipeline().addLast(adaptor);


        // 继续 WebSocket 握手
        ctx.fireChannelRead(request.retain());
    }
}

