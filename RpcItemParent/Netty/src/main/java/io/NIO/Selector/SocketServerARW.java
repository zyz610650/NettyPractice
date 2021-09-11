package io.NIO.Selector;

import io.NIO.buffer.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
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
               selector.select();
               Set<SelectionKey> keys=selector.keys();
               for (SelectionKey key:keys)
               {
                   if (key.isAcceptable())
                   {
                       ServerSocketChannel  c= (ServerSocketChannel) key.channel();
                       SocketChannel sc=c.accept();
                       sc.configureBlocking(false);
                       sc.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(128));
                       logger.info("连接已建立： "+sc);
                   }
                   if (key.isReadable())
                   {
                       SocketChannel sc= (SocketChannel) key.channel();
                       ByteBuffer buffer= (ByteBuffer) key.attachment();
                       try {
                           int read=sc.read(buffer);
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
                                   logger.error("Channel expand capacity!");
                               }
                           }

                       } catch (IOException e) {
                           key.cancel();
                           logger.error("client exception break!");
                       }

                   }

                   if (key.isWritable())
                   {
                       SocketChannel sc= (SocketChannel) key.channel();
                       ByteBuffer buffer= (ByteBuffer) key.attachment();
                       int write=sc.write(buffer);
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
}
