package io.NIO.Selector;

import io.NIO.buffer.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketServerARW {
    private static final Logger logger= LoggerFactory.getLogger(SocketServerARW.class);
    public static void start()
    {
        try( ServerSocketChannel ssc=ServerSocketChannel.open()){
           ssc.bind(new InetSocketAddress(9988));
           ssc.configureBlocking(false);
           Selector selector=Selector.open();
           SelectionKey sscKey=ssc.register(selector,0,null);
           sscKey.interestOps(SelectionKey.OP_ACCEPT);
           while (true)
           {

               System.out.println("等待连接");
               selector.select();
               Set<SelectionKey> keys = selector.selectedKeys();
               System.out.println("keys："+keys.size());
               Iterator<SelectionKey> iter = keys.iterator();
               while (iter.hasNext())
               {
                   System.out.println(selector.selectedKeys().size());
                   SelectionKey key = iter.next();
                   iter.remove();
                   System.out.println(key);

                   if (key.isAcceptable())
                   {
                       ServerSocketChannel  c= (ServerSocketChannel) key.channel();
                       SocketChannel sc=c.accept();
                       sc.configureBlocking(false);
                       sc.register(selector,SelectionKey.OP_READ+SelectionKey.OP_WRITE, ByteBuffer.allocate(128));

                       System.out.println("连接已建立： "+sc);
                       // 1. 向客户端发送内容
                       StringBuilder sb = new StringBuilder();
//                       sb.append("1111");
                       for (int i = 0; i < 3000000; i++) {
                           sb.append("a");
                       }
                       ByteBuffer buffer1 = Charset.defaultCharset().encode(sb.toString());
                       int write = sc.write(buffer1);
                       // 3. write 表示实际写了多少字节
                       System.out.println("实际写入字节:" + write);
                       key.attach(buffer1);

                   }
                   if (key.isReadable())
                   {
                       System.out.println("读事件 "+key);

                       SocketChannel sc= (SocketChannel) key.channel();
                       ByteBuffer buffer= ByteBuffer.allocate(128);

                       try {

                           int read=sc.read(buffer);
                           System.out.println("Channel start read!");
                           System.out.println("读取的字节数： "+read);
                           if (read==-1){//正常关闭
                               key.cancel();
                           }else {
                               split(buffer);
                               if (buffer.position()==buffer.limit())
                               {
                                   ByteBuffer newBuffer=ByteBuffer.allocate(buffer.capacity()*2);
                                   buffer.flip();
                                   newBuffer.put(buffer);
                                   key.attach(buffer);
                                   System.out.println("Channel expand capacity!");

                               }
                           }


                       } catch (IOException e) {

                           key.cancel();
                           logger.error("client exception break!");
                       }

                   }
                   else if (key.isWritable())
                   {
                       System.out.println("写事件"+key);
                       SocketChannel sc= (SocketChannel) key.channel();
                       ByteBuffer buffer= (ByteBuffer) key.attachment();

                       int write=sc.write(buffer);
                       System.out.println(selector.selectedKeys().size());

                       System.out.println();
                       logger.error("实际发送字节数为:"+write);

                       if (!buffer.hasRemaining())
                       {
                           key.interestOps(key.interestOps()-SelectionKey.OP_WRITE);
                           key.attach(null);
                       }
                   }


               }
           }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void split(ByteBuffer buffer)
    {
        buffer.flip();
        for (int i=0;i<buffer.limit();i++)
        {
            if (buffer.get(i)!='\n') continue;
            int len=i-buffer.position()+1;
            ByteBuffer target=ByteBuffer.allocate(len);
            for (int j=0;j<len;j++)
            {
                target.put(buffer.get());
            }
            ByteBufferUtil.debugRead(target);
        }
        buffer.compact();
    }

    public static void main(String[] args) {
        SocketServerARW.start();
    }
}
