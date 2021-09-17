package io.chatRoom.handler;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.chatRoom.message.ChatRequestMessage;
import io.chatRoom.message.LoginRequestMessage;
import io.chatRoom.message.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class LoginRequestHandler extends ChannelInboundHandlerAdapter {
    private CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
    private AtomicBoolean LOGIN = new AtomicBoolean(false);

        @Override
        public void channelRead (ChannelHandlerContext ctx, Object msg) throws Exception {
            log.debug("msg: {}", msg);
            if ((msg instanceof LoginResponseMessage)) {
                LoginResponseMessage res = (LoginResponseMessage) msg;
                if (res.isSuccess()) {
                    LOGIN.set(true);
                }
                WAIT_FOR_LOGIN.countDown();
            }
        }
        @Override
        public void channelActive (ChannelHandlerContext ctx) throws Exception {
            new Thread(() -> {
                Scanner sc = new Scanner(System.in);
                System.out.println("请输入用户名: ");
                String name = sc.nextLine();
                System.out.println("请输入密码:  ");
                String pwd = sc.nextLine();

                LoginRequestMessage msg = new LoginRequestMessage(name, pwd);
                ctx.writeAndFlush(msg);
                log.debug("{}", ctx);
                System.out.println("向服务器发送登录验证...........");
                try {
                    WAIT_FOR_LOGIN.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!LOGIN.get()) {

                    System.out.println("登陆失败,关闭连接");
                    ctx.channel().close();
                    return;
                }
                //编写菜单

                while (true)
                {
                    System.out.println("==================================");
                    System.out.println("send [username] [content]");
                    System.out.println("gsend [group name] [content]");
                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                    System.out.println("gmembers [group name]");
                    System.out.println("gjoin [group name]");
                    System.out.println("gquit [group name]");
                    System.out.println("quit");
                    System.out.println("==================================");
                    String command=sc.nextLine();
                    String[] s=command.split(" ");
                    switch (s[0])
                    {
                        case "send":
                            ctx.writeAndFlush(new ChatRequestMessage(name,s[1],s[2]));
                    }

                }

            }, "System in").start();
        }

}