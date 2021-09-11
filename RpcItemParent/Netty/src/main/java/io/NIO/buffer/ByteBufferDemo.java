package io.NIO.buffer;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class ByteBufferDemo {
    public static void main(String[] args) throws FileNotFoundException {

        ByteBuffer source=ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split1(source);
        source.put("w are you?\nhaha!\n".getBytes());
        split1(source);
    }
    public static void split1(ByteBuffer source)
    {
        source.flip();
        for (int i=0;i<source.limit();i++)
        {

            if (source.get(i)!='\n') continue;
            int len=i-source.position()+1;
            ByteBuffer target=ByteBuffer.allocate(len);
            for (int j=0;j<len;j++)
                target.put(source.get());

            ByteBufferUtil.debugAll(source);
        }

        source.compact();

    }
    private static void split(ByteBuffer source) {
        source.flip();
        int oldLimit = source.limit();
        for (int i = 0; i < oldLimit; i++) {
            if (source.get(i) == '\n') {
                System.out.println(i);
                ByteBuffer target = ByteBuffer.allocate(i + 1 - source.position());
                // 0 ~ limit
                source.limit(i + 1);
                System.out.println("source1");
                ByteBufferUtil.debugAll(source);
                target.put(source); // 从source 读，向 target 写
                System.out.println("source2");
                ByteBufferUtil.debugAll(source);
                System.out.println("target");
                ByteBufferUtil.debugAll(target);
                System.out.println("==================");
                source.limit(oldLimit);
            }
        }
        source.compact();
    }
}
