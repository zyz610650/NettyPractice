package io.chatRoom.Service.Session;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class GroupSessionFactory {
    private static GroupSession groupSession=new GroupSessionMemoryImpl();
    public static GroupSession getGroupSession()
    {
        return groupSession;
    }

}
