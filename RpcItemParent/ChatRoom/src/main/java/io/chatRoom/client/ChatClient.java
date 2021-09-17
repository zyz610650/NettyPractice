package io.chatRoom.client;


import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.chatRoom.Server.ChatServer;
import io.chatRoom.handler.LoginRequestHandler;
import io.chatRoom.message.LoginRequestMessage;
import io.chatRoom.message.LoginResponseMessage;
import io.chatRoom.protocol.MessageCodecSharable;
import io.chatRoom.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Slf4j
public class ChatClient {

    public void start()
    {
        MessageCodecSharable MESSAGE_CODECC=new MessageCodecSharable();
        LoggingHandler LOG_HANDLER=new LoggingHandler(LogLevel.DEBUG);
        NioEventLoopGroup group=new NioEventLoopGroup();

        try {
        Bootstrap bootstrap=new Bootstrap()
        .channel(NioSocketChannel.class)
        .group(group)
        .handler(new ChannelInitializer<SocketChannel>() {
         @Override
         protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProcotolFrameDecoder());
                ch.pipeline().addLast(LOG_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODECC);
                ch.pipeline().addLast(new LoginRequestHandler());
            }
        });

            Channel channel=bootstrap.connect("localhost",7788).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error", e);
        }finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new ChatClient().start();
    }
}
