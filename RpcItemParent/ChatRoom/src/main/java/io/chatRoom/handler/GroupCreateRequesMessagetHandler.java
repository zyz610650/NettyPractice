package io.chatRoom.handler;

import io.chatRoom.Service.Session.Group;
import io.chatRoom.Service.Session.GroupSession;
import io.chatRoom.Service.Session.GroupSessionFactory;
import io.chatRoom.message.GroupCreateRequestMessage;
import io.chatRoom.message.GroupCreateResponseMessage;
import io.netty.channel.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@ChannelHandler.Sharable
public class GroupCreateRequesMessagetHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName=msg.getName();
        Set<String> members=msg.getMembers();
        GroupSession session= GroupSessionFactory.getGroupSession();
        Group group=session.createGroup(groupName,members);
        if (group==null)
        {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false,groupName+"已经存在"));
        }else{
            ctx.writeAndFlush(new GroupCreateResponseMessage(true,groupName+"创建成功"));
            List<Channel> channels=session.getMembersChannel(groupName);
            for (Channel channel:channels)
            {
                channel.writeAndFlush(new GroupCreateResponseMessage(true,"您已被拉入"+groupName));
            }
        }
    }
}
