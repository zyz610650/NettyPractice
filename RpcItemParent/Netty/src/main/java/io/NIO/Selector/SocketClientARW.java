package io.NIO.Selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketClientARW {

    public static void start()
    {
        try (Selector selector=Selector.open()){
            SocketChannel sc=SocketChannel.open();
            sc.connect(new InetSocketAddress(9988));
            sc.register(selector, SelectionKey.OP_READ);
         int count=0;
            while (true)
            {
                selector.select();
                Iterator<SelectionKey> iter=selector.selectedKeys().iterator();
                while (iter.hasNext())
                {
                    SelectionKey key=iter.next();
                    iter.remove();
                    if (key.isConnectable()) {
                        System.out.println(sc.finishConnect());
                    } else if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                        count += sc.read(buffer);
                        buffer.clear();
                        System.out.println(count);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
