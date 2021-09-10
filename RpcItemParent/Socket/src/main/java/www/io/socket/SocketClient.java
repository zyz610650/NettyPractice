package www.io.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketClient {

   // private static final Logger logger= LoggerFactory.getLogger(SocketClient.class);

    public void start(int port)
    {

        try(Socket socketClient=new Socket("localhost",1111)) {
//            SocketAddress socketAddress=new InetSocketAddress("localhost",1111);
//          socketClient.connect(socketAddress,20000);

            try( ObjectOutputStream objectOutputStream=new ObjectOutputStream(socketClient.getOutputStream());
                ObjectInputStream inputStream=new ObjectInputStream(socketClient.getInputStream()))
          {

              objectOutputStream.writeObject(new User("zyz",18));

              User str= (User) inputStream.readObject();
              System.out.println(str);
          } catch (ClassNotFoundException e) {
              e.printStackTrace();
          }


        } catch (IOException e) {
            e.printStackTrace();
         //   logger.error("error Exception",e);
        }
    }

    public static void main(String[] args) {
        SocketClient socketClient=new SocketClient();
        socketClient.start(1112);
    }
}
