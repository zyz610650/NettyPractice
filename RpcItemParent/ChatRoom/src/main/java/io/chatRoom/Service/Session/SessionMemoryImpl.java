package io.chatRoom.Service.Session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */

// https://www.cnblogs.com/jiataoq/p/10967534.html
public class SessionMemoryImpl implements Session{
    Map<Channel,String> channelUsernameMap=new ConcurrentHashMap<>();
    Map<String,Channel> usernameChannelMap=new ConcurrentHashMap<>();
    Map<Channel,Map<String,Object>> channelAttributesMap=new ConcurrentHashMap<>();

    @Override
    public void bind(Channel channel,String username) {
        channelUsernameMap.put(channel,username);
        usernameChannelMap.put(username,channel);
        channelAttributesMap.put(channel,new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(Channel channel) {
        // remove(kEY) 成功移除返回的是value remove(kEY，Value) 返回的是Bool
        String username=channelUsernameMap.remove(channel);
        usernameChannelMap.remove(username);
        channelAttributesMap.remove(channel);
    }

    @Override
    public Object getAttribute(Channel channel, String name) {

        return channelAttributesMap.get(channel).get(name);
    }

    @Override
    public void setAttribute(Channel channel, String name, Object value) {
        channelAttributesMap.get(channel).put(name,value);
    }

    @Override
    public Channel getChannel(String username) {
        return usernameChannelMap.get(username);
    }
}
