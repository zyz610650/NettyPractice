package io.chatRoom.Server;

import io.chatRoom.Service.UserServiceFactory;
import io.chatRoom.handler.LoginRequestMessageHandler;
import io.chatRoom.handler.LoginResponseHandler;
import io.chatRoom.message.LoginRequestMessage;
import io.chatRoom.message.LoginResponseMessage;
import io.chatRoom.protocol.MessageCodecSharable;
import io.chatRoom.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Slf4j
public class ChatServer {

    public void start()
    {

        NioEventLoopGroup boss=new NioEventLoopGroup();
        NioEventLoopGroup workers=new NioEventLoopGroup(2);
        MessageCodecSharable MESSAGE_CODECC=new MessageCodecSharable();
        LoginResponseHandler LOGIN_RESPONSE=new LoginResponseHandler();
        LoginRequestMessageHandler LOGIN_MESSAGE_REQUEST=new LoginRequestMessageHandler();
        LoggingHandler LOG_HANDLER=new LoggingHandler(LogLevel.DEBUG);
        try {
        ServerBootstrap bootstrap=new ServerBootstrap()
        .channel(NioServerSocketChannel.class)
        .group(boss,workers)
         .childHandler(new ChannelInitializer<SocketChannel>() {
               @Override
               protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(LOG_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODECC);
                    ch.pipeline().addLast(LOGIN_RESPONSE);
                    ch.pipeline().addLast(LOGIN_MESSAGE_REQUEST);
               }
              });
        Channel channel=bootstrap.bind(7788).sync().channel();
        channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
