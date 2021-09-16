package io.chatRoom.Service;

import io.chatRoom.Service.impl.UserServiceImpl;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public abstract class UserServiceFactory {
    private static UserService userService=new UserServiceImpl();
    public static UserService getUserService(){return userService;}
}
