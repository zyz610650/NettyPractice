package io.NIO.buffer;

import java.nio.ByteBuffer;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class ByteBufferWriteRead {

    public static void main(String[] args) {

        ByteBuffer buffer= ByteBuffer.allocate(10);
        buffer.put((byte) 0x61);
        ByteBufferUtil.debugAll(buffer);
        buffer.put(new byte[]{0x62,0x63,0x64}); //b c d
        ByteBufferUtil.debugAll(buffer);
        System.out.println(buffer.get());
        buffer.flip();
        System.out.println(buffer.get()); //get获取当前position所指向的位置，因为position指向的是下一次可插入的位置
        //所以获取的是0
        buffer.compact();
        ByteBufferUtil.debugAll(buffer);
        buffer.put(new byte[]{0x65,0x6f});
        ByteBufferUtil.debugAll(buffer);

    }
}
