package cn.itcast.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GroupSessionMemoryImpl implements GroupSession {
    private final Map<String, Group> groupMap = new ConcurrentHashMap<>();

    @Override
    public Group createGroup(String name, Set<String> members) {
        Group group = new Group(name, members);
        return groupMap.putIfAbsent(name, group);
    }

    @Override
    public Group joinMember(String name, String member) {
        return groupMap.computeIfPresent(name, (key, value) -> {
            value.getMembers().add(member);
            return value;
        });
    }

    @Override
    public Group removeMember(String name, String member) {
        return groupMap.computeIfPresent(name, (key, value) -> {
            value.getMembers().remove(member);
            return value;
        });
    }

    @Override
    public Group removeGroup(String name) {
        return groupMap.remove(name);
    }

    @Override
    public Set<String> getMembers(String name) {
        //如果没有 Group.EMPTY_GROUP 返回的是null,则调用该方法对返回的set进行遍历时，若set为空
        //需要进行判断，否则不判断直接遍历的话因为set为Null，直接遍历会报错
        return groupMap.getOrDefault(name, Group.EMPTY_GROUP).getMembers();
        // getOrDefault 意思就是当Map集合中有这个key时，就使用这个key值，如果没有就使用默认值defaultValue
    }

    // Stream https://www.runoob.com/java/java8-streams.html

    // ::  https://blog.csdn.net/zhoufanyang_china/article/details/87798829
    @Override
    public List<Channel> getMembersChannel(String name) {
        return getMembers(name).stream()
                .map(member -> SessionFactory.getSession().getChannel(member))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }
}
