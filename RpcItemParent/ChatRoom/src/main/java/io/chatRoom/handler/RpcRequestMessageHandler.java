package io.chatRoom.handler;

import io.chatRoom.Server.RpcServer;
import io.chatRoom.Service.ServicesFactory;
import io.chatRoom.Service.UserService;
import io.chatRoom.message.RpcRequestMessage;
import io.chatRoom.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */



@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
        String interfaceName = msg.getInterfaceName();
        String methodName= msg.getMethodName();
        Class[] parameterTypes=msg.getParameterTypes();
        Object[] parameterValue = msg.getParameterValue();
        Class<?> returnType= msg.getReturnType();
        RpcResponseMessage res=new RpcResponseMessage();
        res.setSequenceId(msg.getSequenceId());
        try {
//            Class<?> serviceImpl = (Class<?>) ServicesFactory.getService(Class.forName(interfaceName));
            Class<?> service= (Class<?>) ServicesFactory.getService(Class.forName(interfaceName));
            Method method=service.getMethod(methodName,parameterTypes);
            Object newInstance = service.newInstance();
            Object invoke = method.invoke(newInstance,parameterValue);
            res.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            res.setExceptionValue(new Exception(e.getCause().getMessage()));
        }finally {
            ctx.writeAndFlush(res);
        }


    }
}
