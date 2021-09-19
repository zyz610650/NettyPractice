package io.chatRoom.UserService;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class UserServiceImpl implements UserService{
    @Override
    public String say(String name) {
        return "Hello "+name;
    }
}
