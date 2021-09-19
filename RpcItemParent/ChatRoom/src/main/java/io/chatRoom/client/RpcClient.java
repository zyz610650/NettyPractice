package io.chatRoom.client;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.chatRoom.Service.Session.SequenceIdGenerator;
import io.chatRoom.UserService.UserService;
import io.chatRoom.handler.RpcResponseMessageHandler;
import io.chatRoom.message.RpcRequestMessage;
import io.chatRoom.message.RpcResponseMessage;
import io.chatRoom.protocol.MessageCodecSharable;
import io.chatRoom.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Slf4j
public class RpcClient {

    public static void main(String[] args) {
        UserService proxyService = RpcClient.getProxyService(UserService.class);
        System.out.println(proxyService.say("zyz"));
        System.out.println(proxyService.say("mzd"));
    }
    private static Channel channel=null;
    private static void InitChannel()
    {
        LoggingHandler LOGGING_HANDLER=new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGECODEC_HANDLER=new MessageCodecSharable();
        RpcResponseMessageHandler RPCRES_HANDLER=new RpcResponseMessageHandler();
        NioEventLoopGroup worker=new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProcotolFrameDecoder());
                    socketChannel.pipeline().addLast(LOGGING_HANDLER);
                    socketChannel.pipeline().addLast(MESSAGECODEC_HANDLER);
                    socketChannel.pipeline().addLast(RPCRES_HANDLER);
                }
            });
            channel=bootstrap.connect("localhost",7788).sync().channel();
            channel.closeFuture().addListener(future -> {
                worker.shutdownGracefully();
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("{}",e);
        }
    }

    private static final Object LOCK = new Object();

    // 获取唯一的 channel 对象
    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) { //  t2
            if (channel != null) { // t1
                return channel;
            }
            InitChannel();
            return channel;
        }
    }
    public static <T> T  getProxyService(Class<T> serviceClass)
    {
        ClassLoader loader=serviceClass.getClassLoader();
        Class<?>[] interfaces=new Class[]{serviceClass};
        Object o= Proxy.newProxyInstance(loader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               int seqId= SequenceIdGenerator.nextID();
                RpcRequestMessage msg=new RpcRequestMessage(
                  seqId,
                  serviceClass.getName(),
                  method.getName(),
                  method.getReturnType(),
                  method.getParameterTypes(),
                  args
                );
                getChannel().writeAndFlush(msg);
                DefaultPromise<Object> promise=new DefaultPromise<>(getChannel().eventLoop());
                RpcResponseMessageHandler.PROMISES.put(seqId,promise);
                promise.await();
                if (promise.isSuccess())
                {
                    return promise.getNow();
                }else {
                    throw new RuntimeException(promise.cause().getMessage());
                }

            }
        });
        return (T) o;
    }
}
