package io.chatRoom.Service.Session;

import io.netty.channel.Channel;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public interface Session {

    public void bind(Channel channel,String username);

    public void unbind(Channel channel);

    Object getAttribute(Channel channel,String name);

    void setAttribute(Channel channel,String name,Object value);

    Channel getChannel(String username);

}
