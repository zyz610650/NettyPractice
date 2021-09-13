package io.NIO.Selector;

import io.NIO.buffer.ByteBufferUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class Worker implements Runnable{

    private Thread thread;
    public String name;
    private boolean flag;
    private final ConcurrentLinkedDeque<Runnable> tasks=new ConcurrentLinkedDeque<>();

    private Selector selector;


    public Worker(String name) {
        this.name = name;
        this.flag=false;

    }
    // 确定的是 一个key对应一个channel
//    Key和Channle的关系,一个channel可以处理多个事件吗 一个key
    public void register(SocketChannel sc)
    {
        try {
          if(!flag)
          {
              selector=Selector.open();
              this.thread=new Thread(this);
              thread.start();
              flag=true;
          }
            tasks.add(()->{
                SelectionKey sckey = null;
                try {
                    sckey = sc.register(selector, 0, null);
                    sckey.interestOps(SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } );




        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        while (true)
        {
            try {
                Runnable task=tasks.poll();
                if (task!=null)task.run();
                selector.select();

                Iterator<SelectionKey> iter=selector.selectedKeys().iterator();
                while (iter.hasNext())
                {
                    SelectionKey scKey=iter.next();
                    iter.remove();
                    try {
                        if (scKey.isReadable())
                        {
                            SocketChannel sc = (SocketChannel) scKey.channel();
                            ByteBuffer buffer=ByteBuffer.allocate(32);
                            int read=sc.read(buffer);
                            if (read==-1)
                            {
                                scKey.cancel();
                                System.out.println("客户端 "+name+" 断开");
                            }
                            else {
                                System.out.println("message from:"+sc.getRemoteAddress());
                                buffer.flip();
                                ByteBufferUtil.debugRead(buffer);
                            }
                        }
                    } catch (IOException e) {

                        System.out.println("客户端 "+name+"exception  断开");
                        scKey.cancel();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
