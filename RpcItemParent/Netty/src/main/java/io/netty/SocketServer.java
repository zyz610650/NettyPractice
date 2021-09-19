package io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import javax.naming.ldap.SortKey;
import java.nio.charset.Charset;


/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Slf4j
public class SocketServer {

    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(group,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new LoggingHandler());
                            nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                    ByteBuf rec = (ByteBuf) msg;
                                    //log(rec);
                                    System.out.println(nioSocketChannel.toString() + " " + rec.toString(Charset.defaultCharset()));
                                    ByteBuf res = ctx.alloc().buffer();
                                    res.writeBytes(rec);
                                    ctx.writeAndFlush(res);

                                }
                            });

                        }
                    }).bind(7788);
            channelFuture.sync();
            Channel channel=channelFuture.channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭");
            group.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new SocketServer().start();
        System.out.println(3);
    }
}
