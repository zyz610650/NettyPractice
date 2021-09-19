package io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketClient {


    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture=new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());

                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
                            {
                                ByteBuf buf= (ByteBuf) msg;
                                System.out.println("来自服务器: "+((ByteBuf) msg).toString(Charset.defaultCharset()));
                            }

                        });
                    }
                })
                .connect(new InetSocketAddress("localhost",7788));
        System.out.println(channelFuture.channel()); // 1
        channelFuture.sync(); // 2
        System.out.println(channelFuture.channel()); // 3
        channelFuture.channel().close();
        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();

//        new Thread(()->{
//            for (int i=0;i<5;i++)
//            {
//                channel.writeAndFlush(i+"");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("发送数据完毕");
//            channel.close();
//        }).start();
//
//      channel.closeFuture().addListener(future -> {
//          System.out.println("处理关闭后的操作");
//        group.shutdownGracefully();
//        });



    }
    public static void main(String[] args) throws InterruptedException {
        new SocketClient().start();
    }

}
