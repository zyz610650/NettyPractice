package io.chatRoom.Service.Session;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SequenceIdGenerator {
    private static AtomicInteger id=new AtomicInteger();
    public static Integer nextID()
    {
        return id.incrementAndGet();
    }
}
