package io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class ByteBufDemo {
    public static void main(String[] args) {
        ByteBuf buf= ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a','b','c','d','e','f','g','h','i','j'});
        ByteBuf f1=buf.slice(0,5);
        ByteBuf f2=buf.slice(5,5);
//        f1.retain();
       f2.retain();
        buf.release();
        System.out.println(f1);
        System.out.println(f2);
    }
}
