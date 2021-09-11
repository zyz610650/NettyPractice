package io.NIO.SocketChannel;

import io.NIO.buffer.ByteBufferUtil;
import io.netty.channel.ServerChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketServer {


    private static final Logger logger=  LoggerFactory.getLogger(SocketServer.class);
    public void start() throws IOException {
        ServerSocketChannel ssc=ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(7788));
        ByteBuffer buffer=ByteBuffer.allocate(16);
        List<SocketChannel> channels=new ArrayList<>();
        ssc.configureBlocking(false); // 非阻塞模式
        while (true)
        {

            SocketChannel sc= ssc.accept();
            logger.info("等待客户端连接");
            if (sc!=null)
            {
                logger.info("客户端连接建立： "+sc.getRemoteAddress());
                sc.configureBlocking(false); // 非阻塞模式
                channels.add(sc);
            }
            for (SocketChannel channel:channels)
            {

                int read=channel.read(buffer);
                if (read>0)
                {
                    logger.info("接收客户端数据： "+sc.getRemoteAddress());
                    buffer.flip();
                    // ByteBufferUtil.debugRead(buffer);
                    buffer.clear();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SocketServer socketServer=new SocketServer();
        socketServer.start();
    }
}
