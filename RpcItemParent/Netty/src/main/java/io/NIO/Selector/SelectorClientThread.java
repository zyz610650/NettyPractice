package io.NIO.Selector;

import io.NIO.buffer.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SelectorClientThread {

    public static void main(String[] args) {
        try ( SocketChannel sc=SocketChannel.open()){
            sc.connect(new InetSocketAddress(8899));
            ByteBuffer buffer = Charset.defaultCharset().encode("I am client ");
            sc.write(buffer);
            Thread.sleep(20000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
