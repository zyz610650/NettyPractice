package io.chatRoom.Service.impl;

import io.chatRoom.Service.UserService;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class UserServiceImpl implements UserService {


    @Override
    public boolean login(String username, String password) {

        if (!username.isEmpty()&&password.equals("123"))
          return true;
        return false;
    }

}
