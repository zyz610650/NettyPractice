package io.NIO.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketClient {
    static Logger logger= LoggerFactory.getLogger(SocketClient.class);
    public  void start() throws IOException {
        SocketChannel sc=SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",7788));

        sc.write(ByteBuffer.wrap("Hello,Im ".getBytes()));
    }

    public static void main(String[] args) throws IOException {
        SocketClient sc=new SocketClient();
        sc.start();
    }
}
