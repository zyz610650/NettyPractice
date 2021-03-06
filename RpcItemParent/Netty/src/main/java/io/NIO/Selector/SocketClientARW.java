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
public class SocketClientARW {

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

            // 1. 向客户端发送内容
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3000000; i++) {
                sb.append("a");
            }
            ByteBuffer buffer1 = Charset.defaultCharset().encode(sb.toString());
            int write = sc.write(buffer1);
            // 3. write 表示实际写了多少字节
            System.out.println("实际写入字节:" + write);
            // 4. 如果有剩余未读字节，才需要关注写事件
            if (buffer1.hasRemaining()) {
                // read 1  write 4
                // 在原有关注事件的基础上，多关注 写事件
                key1.interestOps(key1.interestOps() + SelectionKey.OP_WRITE);

                // 把 buffer 作为附件加入 sckey
                key1.attach(buffer1);
                System.out.println("仍有数据添加写事件"+key1);
                System.out.println("写事件"+selector.selectedKeys().size());
            }

            int count=0;
            int t=0;
            while (true)
            {
                selector.select();
                Iterator<SelectionKey> iter=selector.selectedKeys().iterator();
                t++;
                while (iter.hasNext())
                {
                    SelectionKey key=iter.next();
                    iter.remove();
                    System.out.println(selector.selectedKeys().size());

//                    System.out.println("事件是： "+key);
                    if (key.isConnectable()) {
                        System.out.println("完成连接"+sc.finishConnect());

                    } else if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                        count += sc.read(buffer);
                        System.out.println(selector.selectedKeys().size());
                        buffer.clear();
                      //  System.out.println(count);
//                        ByteBufferUtil.debugRead(buffer);
                    }
                }
                System.out.println(t);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SocketClientARW.start();
    }
}
