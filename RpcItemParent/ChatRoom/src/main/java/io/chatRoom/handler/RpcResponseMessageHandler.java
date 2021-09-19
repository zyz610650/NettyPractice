package io.chatRoom.handler;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import io.chatRoom.message.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Data
@ToString(callSuper = true)
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {


    public static final Map<Integer, Promise<Object>> PROMISES = new ConcurrentHashMap<>();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {

        Promise<Object> promise = PROMISES.get(msg.getSequenceId());
        if (promise!=null){
            Object returnValue = msg.getReturnValue();
            Exception exceptionValue = msg.getExceptionValue();
            if (exceptionValue==null) promise.setSuccess(returnValue);
            else promise.setFailure(exceptionValue);
        }
    }
}
