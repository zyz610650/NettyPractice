package io.chatRoom.handler;

import io.chatRoom.Service.Session.GroupSession;
import io.chatRoom.Service.Session.GroupSessionFactory;
import io.chatRoom.message.GroupChatRequestMessage;
import io.chatRoom.message.GroupChatResponseMessage;
import io.chatRoom.message.GroupCreateResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {

        String username=msg.getUserName();
        String groupname=msg.getGroupName();
        String content=msg.getContent();

        GroupSession session= GroupSessionFactory.getGroupSession();
        List<Channel> channels=session.getMembersChannel(groupname);
        for (Channel channel:channels)
        {
            channel.writeAndFlush(new GroupChatResponseMessage(username,content));
        }
    }
}
