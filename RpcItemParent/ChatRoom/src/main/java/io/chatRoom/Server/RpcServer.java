package io.chatRoom.Server;

import io.chatRoom.handler.RpcRequestMessageHandler;
import io.chatRoom.message.RpcRequestMessage;
import io.chatRoom.message.RpcResponseMessage;
import io.chatRoom.protocol.MessageCodecSharable;
import io.chatRoom.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Slf4j
public class RpcServer {


    void start(){
        NioEventLoopGroup boos=new NioEventLoopGroup();
        NioEventLoopGroup worker=new NioEventLoopGroup();

        LoggingHandler LOGGING_HANDLER=new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGECODEC_HANDLER=new MessageCodecSharable();
        RpcRequestMessageHandler RPC_HANDLER=new RpcRequestMessageHandler();
        try {
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(boos,worker);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProcotolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGECODEC_HANDLER);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });
            ChannelFuture channelFuture = serverBootstrap.bind(7788).sync();
            Channel channel=channelFuture.channel();

            channel.writeAndFlush(new RpcResponseMessage(true,"客户端消息",null));
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boos.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new RpcServer().start();
    }
}
