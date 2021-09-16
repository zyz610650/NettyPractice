package io.chatRoom.client;


import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.chatRoom.Server.ChatServer;
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
        CountDownLatch WAIT_FOR_LOGIN=new CountDownLatch(1);
        AtomicBoolean LOGIN=new AtomicBoolean(false);
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
                ch.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("msg: {}", msg);
                        if ((msg instanceof LoginResponseMessage))
                        {
                            LoginResponseMessage res= (LoginResponseMessage) msg;
                            if (res.isSuccess())
                            {
                                LOGIN.set(true);
                            }
                            WAIT_FOR_LOGIN.countDown();
                        }
                    }
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        new Thread(()->{
                            Scanner sc=new Scanner(System.in);
                            System.out.println("请输入用户名: ");
                            String name=sc.nextLine();
                            System.out.println("请输入密码:  ");
                            String pwd=sc.nextLine();

                            LoginRequestMessage msg=new LoginRequestMessage(name,pwd);
                            ctx.writeAndFlush(msg);
                            log.debug("{}",ctx);
                            System.out.println("向服务器发送登录验证...........");
                            try {
                             WAIT_FOR_LOGIN.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!LOGIN.get())
                            {
                                ctx.channel().close();
                                return;
                            }
                            //编写菜单
                            while (true) {
                                System.out.println("==================================");
                                System.out.println("send [username] [content]");
                                System.out.println("gsend [group name] [content]");
                                System.out.println("gcreate [group name] [m1,m2,m3...]");
                                System.out.println("gmembers [group name]");
                                System.out.println("gjoin [group name]");
                                System.out.println("gquit [group name]");
                                System.out.println("quit");
                                System.out.println("==================================");
                            }
                        },"System in").start();

                    }
                });
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
