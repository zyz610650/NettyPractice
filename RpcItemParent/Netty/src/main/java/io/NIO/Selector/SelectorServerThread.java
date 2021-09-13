package io.NIO.Selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public  class SelectorServerThread implements Runnable{



    @Override
    public void run() {
        try (ServerSocketChannel ssc=ServerSocketChannel.open()){
            ssc.bind(new InetSocketAddress(8899));
            ssc.configureBlocking(false);
            Selector selector=Selector.open();

            SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT);

            int cpus=3;
            Worker[] workers=new Worker[cpus];
            for (int i=0;i<cpus;i++)
                workers[i]=new Worker("Worker-0"+i);
            AtomicInteger index=new AtomicInteger();
            while (true)
            {
                System.out.println("等待客户端连接....");
                selector.select();
                if (sscKey.isAcceptable())
                {
                    SocketChannel sc=ssc.accept();
                    sc.configureBlocking(false);
                    Worker worker=workers[index.getAndIncrement()%cpus];
                    System.out.println("客户端："+ worker.name+" 已连接");
                    worker.register(sc);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SelectorServerThread server=new SelectorServerThread();
        new Thread(server).start();

    }
}
