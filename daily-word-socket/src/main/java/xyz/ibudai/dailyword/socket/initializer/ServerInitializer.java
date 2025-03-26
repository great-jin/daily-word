package xyz.ibudai.dailyword.socket.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.socket.enums.Protocol;
import xyz.ibudai.dailyword.socket.handler.RequestHandler;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Integer MAX_LEN = 65536;

    private final RequestHandler requestHandler;


    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // HTTP 编解码器
        pipeline.addLast(new HttpServerCodec());
        // HTTP 消息聚合器
        pipeline.addLast(new HttpObjectAggregator(MAX_LEN));
        // 解析 URL 参数
        pipeline.addLast(requestHandler);
        // WebSocket 处理器
        // ws://${host}:${port}/websocket
        pipeline.addLast(new WebSocketServerProtocolHandler(
                Protocol.PREFIX.getUri(),
                Protocol.getSubProtocols(),
                true,
                MAX_LEN,
                false,
                true
        ));
    }
}

