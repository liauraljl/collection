package com.ljl.note.collection.support.framework;

import com.ljl.note.collection.support.framework.handler.WebsocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class WebsocketServer {

    @Autowired
    private WebsocketHandler websocketHandler;

    @PostConstruct
    public void nettyMain(){
        new Thread(() -> {
            int port = 8888;
            NioEventLoopGroup boss = new NioEventLoopGroup();
            NioEventLoopGroup work = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,work);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    //SSLContext sslContext = SslUtil.createSSLContext("JKS","D://wss.jks","netty123");
                    //SSLEngine engine = sslContext.createSSLEngine();
                    //engine.setUseClientMode(false);
                    //ch.pipeline().addLast(new SslHandler(engine));
                    pipeline.addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new ChunkedWriteHandler());
                    pipeline.addLast(new HttpObjectAggregator(65536));
                    pipeline.addLast(new WebSocketServerProtocolHandler("/plugin/live/mob/ws/live/"));
                    pipeline.addLast(websocketHandler);
                }
            });
            try {
                ChannelFuture sync = serverBootstrap.bind(port).sync();
                log.info("netty启动websocket服务端，占用端口：{}",port);
                sync.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("netty启动websocket服务端报错！");
            }finally {
                boss.shutdownGracefully();
                work.shutdownGracefully();
            }
        }).start();
    }

}
