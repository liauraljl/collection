package com.ljl.note.collection.imClient.framework.netty;

import com.ljl.note.collection.imClient.framework.netty.handler.ClientHandler;
import com.ljl.note.collection.imCommon.codec.MarshallingCodeCFactory;
import com.ljl.note.collection.imCommon.entity.TranslatorData;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NettyClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 10087;

    //扩展 完善 池化: ConcurrentHashMap<KEY -> String, Value -> Channel>
    private Channel channel;

    //1. 创建工作线程组: 用于实际处理业务的线程组
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    @PostConstruct
    public void nettyMain(){
        new Thread(()->{

            //2 辅助类(注意Client 和 Server 不一样)
            Bootstrap bootstrap = new Bootstrap();
            try {

                bootstrap.group(workGroup)
                        .channel(NioSocketChannel.class)
                        //表示缓存区动态调配（自适应）
                        .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                        //缓存区 池化操作
                        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel sc) throws Exception {
                                sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                                sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                                sc.pipeline().addLast(new ClientHandler());
                            }
                        });
                //绑定端口，同步等等请求连接
                ChannelFuture cf = bootstrap.connect(HOST, PORT).sync();
                System.err.println("Client connected...");

                //接下来就进行数据的发送, 但是首先我们要获取channel:
                this.channel = cf.channel();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendData(){

        for(int i =0; i <10; i++){
            TranslatorData request = new TranslatorData();
            request.setId("" + i);
            request.setName("请求消息名称 " + i);
            request.setMessage("请求消息内容 " + i);
            this.channel.writeAndFlush(request);
        }
    }

    public void close() throws Exception {
        this.channel.closeFuture().sync();
        //优雅停机
        workGroup.shutdownGracefully();
        System.err.println("Sever ShutDown...");
    }
}
