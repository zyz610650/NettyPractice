package io.chatRoom.handler;

import io.chatRoom.Service.Session.Session;
import io.chatRoom.Service.Session.SessionFactory;
import io.chatRoom.Service.UserServiceFactory;
import io.chatRoom.message.LoginRequestMessage;
import io.chatRoom.message.LoginResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestMessage loginRequestMessage) throws Exception {
        //验证用户信息
        String username=loginRequestMessage.getUsername();
        String pwd=loginRequestMessage.getPassword();
        Session session= SessionFactory.getSession();

        boolean login= UserServiceFactory.getUserService().login(username,pwd);
        LoginResponseMessage msg;

        if (login)
        {
            session.bind(channelHandlerContext.channel(),username);
            msg=new LoginResponseMessage(true,"登陆成功!");
        }
        else msg=new LoginResponseMessage(false,"用户名或密码不正确");
        log.debug("{}",msg);

        channelHandlerContext.writeAndFlush(msg);

    }


}
