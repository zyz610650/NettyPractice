package io.chatRoom.handler;

import io.chatRoom.Service.Session.Session;
import io.chatRoom.Service.Session.SessionFactory;
import io.chatRoom.message.ChatRequestMessage;
import io.chatRoom.message.ChatResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        Session session= SessionFactory.getSession();
        String to=msg.getTo();
        String from=msg.getFrom();
        String content=msg.getContent();
        Channel toChannel=session.getChannel(to);

        if (toChannel!=null)
            // 在线，利用toChannel发送给对方
            toChannel.writeAndFlush(new ChatResponseMessage(from,content));
        else
            // 不在线，服务器利用ctx（发消息人的channel）给发消息方进行回应
            ctx.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或者不在线"));
    }
}
