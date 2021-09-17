package io.chatRoom.Service.Session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class GroupSessionMemoryImpl implements GroupSession {
    private final Map<String,Group> groupMap =new ConcurrentHashMap<>();
    @Override
    public Group createGroup(String groupName, Set<String> members) {
        Group group=new Group(groupName,members);
        return groupMap.put(groupName,group);

    }

    @Override
    public Group delGroup(String groupName) {
        return  groupMap.remove(groupName);

    }

    @Override
    public Group joinMember(String username, String groupName) {
        return groupMap.computeIfPresent(groupName,(key,value)->{
           value.getMembers().add(username);
           return value;
        });
        //代替下面
//
//        groupMap.get(groupName).getMembers().add(username);
//            return groupMap.get(groupName);
    }

    @Override
    public Group removeMember(String username, String groupName) {
            Group group=groupMap.get(groupName);
            group.getMembers().remove(username);
            return group;
    }

    @Override
    public void sendToAll(String username, String groupName) {

    }

    @Override
    public Set<String> getMembers(String name) {
        return  groupMap.getOrDefault(name,Group.EMPTY_GROUP).getMembers();

    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        return getMembers(name).stream().
                map(member->SessionFactory.getSession().getChannel(member))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }
}
