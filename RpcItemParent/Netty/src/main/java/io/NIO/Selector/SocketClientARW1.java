package io.NIO.Selector;

import io.NIO.buffer.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketClientARW1 {

    public static void start()
    {
        try (Selector selector=Selector.open()){
            SocketChannel sc=SocketChannel.open();
            sc.connect(new InetSocketAddress(9988));
            sc.configureBlocking(false);
            SelectionKey key1=sc.register(selector, SelectionKey.OP_READ);
//            ByteBuffer buffer1= Charset.defaultCharset().encode("Hello world");
//            int write=sc.write(buffer1);
//            System.out.println("发送数据字节数: "+write);



            int count=0;
            int t=0;
            while (true)
            {
                System.out.println("start "+selector.selectedKeys().size());
                selector.select();
                Iterator<SelectionKey> iter=selector.selectedKeys().iterator();
                System.out.println(selector.selectedKeys().size());
                while (iter.hasNext())
                {
                    SelectionKey key=iter.next();
        //            iter.remove();
                    System.out.println(selector.selectedKeys().size());

//                    System.out.println("事件是： "+key);
                    if (key.isConnectable()) {
                        System.out.println("完成连接"+sc.finishConnect());

                    } else if (key.isReadable()) {
                        System.out.println("Read");
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        count += sc.read(buffer);

                        buffer.clear();
                        System.out.println("读取多少字节： " +count);
                        buffer.flip();
                      ByteBufferUtil.debugAll(buffer);
                    }
                    System.out.println(selector.selectedKeys().size());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SocketClientARW1.start();
    }
}
