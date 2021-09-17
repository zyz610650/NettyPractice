package io.chatRoom.Service.Session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public interface GroupSession {

    Group createGroup(String groupName,Set<String> members);
    Group delGroup(String groupName);
    Group joinMember(String username,String groupName);
    Group removeMember(String username,String groupName);
    void sendToAll(String username,String groupName);
    Set<String> getMembers(String name);
    List<Channel> getMembersChannel(String name);
}
