package io.chatRoom.handler;

import io.chatRoom.Service.Session.Group;
import io.chatRoom.Service.Session.GroupSession;
import io.chatRoom.Service.Session.GroupSessionFactory;
import io.chatRoom.Service.Session.Session;
import io.chatRoom.message.GroupJoinRequestMessage;
import io.chatRoom.message.GroupJoinResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String username=msg.getUserName();
        String groupname=msg.getGroupName();
        GroupSession session= GroupSessionFactory.getGroupSession();
        Group group = session.joinMember(username, groupname);
        if (group==null) ctx.writeAndFlush(new GroupJoinResponseMessage(false,msg.getGroupName() +"群不存在"));
        else  ctx.writeAndFlush(new GroupJoinResponseMessage(false,msg.getGroupName() +"群加入成功"));
    }
}
