package io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;

import javax.naming.ldap.SortKey;
import java.nio.charset.Charset;

import static io.netty.Logger.log;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketServer {

    public void start()
    {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new LoggingHandler());
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter()
                        {
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                ByteBuf rec= (ByteBuf) msg;
                                log(rec);
                                System.out.println(  nioSocketChannel.toString()+" "+rec.toString(Charset.defaultCharset()));
                                ByteBuf res = ctx.alloc().buffer();
                                res.writeBytes(rec);
                                ctx.writeAndFlush(res);
                            }
                        });


                    }
                })
                .bind(7788);
    }

    public static void main(String[] args) {
        new SocketServer().start();
    }
}
