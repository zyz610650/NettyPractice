package io.chatRoom.Service.Session;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public abstract class SessionFactory {

    private static Session session=new SessionMemoryImpl();
    public  static Session getSession() {return session;}

}
