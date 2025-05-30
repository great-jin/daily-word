package xyz.ibudai.dailyword.client.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.socket.initializer.ServerInitializer;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSocketServer {

    @Value("${websocket.port}")
    private Integer port;

    private final ServerInitializer serverInitializer;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    @PostConstruct
    public void start() {
        CompletableFuture.runAsync(() -> {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();

            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(serverInitializer);
                ChannelFuture future = bootstrap.bind(port).sync();
                log.info("Websocket initialized with port(s): {}", port);

                future.channel().closeFuture().sync();
            } catch (Exception e) {
                log.error("Websocket server error", e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
    }
}

