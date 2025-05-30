package xyz.ibudai.dailyword.socket.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.basic.encrypt.AESUtil;
import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;
import xyz.ibudai.dailyword.socket.enums.AttrKey;
import xyz.ibudai.dailyword.socket.enums.Protocol;
import xyz.ibudai.dailyword.socket.manager.ChannelManager;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Map<String, ChannelAdaptor> adaptorMap;


    /**
     * 执行完连接建立事件后触发
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 解析 URL
        String uri = request.uri();
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = decoder.parameters();
        Protocol protocol = Protocol.getByUri(new URI(uri).getPath());

        boolean valid = false;
        Channel channel = ctx.channel();
        if (!Objects.equals(protocol, Protocol.DEFAULT)) {
            if (parameters.containsKey(AttrKey.UID.getKey())) {
                valid = true;
                // 转存会话信息
                String idStr = parameters.get(AttrKey.UID.getKey()).get(0).trim();
                idStr = idStr.replace(" ", "+");
                ChannelManager.setChannel(
                        protocol,
                        Integer.parseInt(AESUtil.desEncrypt(idStr).trim()),
                        channel
                );
            }
        }
        channel.attr(AttributeKey.valueOf(AttrKey.AUTHED.getKey())).set(valid);

        // 服务路由
        ctx.pipeline().addLast(adaptorMap.get(Protocol.getBeanName(protocol)));

        // 继续 WebSocket 握手
        ctx.fireChannelRead(request.retain());
    }
}

