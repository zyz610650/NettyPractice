package www.io.socket;


import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class SocketServer {
    private static final Logger logger= LoggerFactory.getLogger(SocketServer.class);

    public void start(int port)
    {
        try(ServerSocket server=new ServerSocket(port))
        {
            Socket socket ;
            logger.info("正在等待连接中..........");
            while ((socket=server.accept())!=null)
            {
                logger.info("client connected",socket.getPort());
                try(ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream()))
                {
                    User message= (User) objectInputStream.readObject();
                    Integer a=0;
                    if(a==null)
                    {
                        throw new RuntimeException("a is null");
                    }
                logger.info("server receive message:"+message);
                objectOutputStream.writeObject(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.info("occur exception ",e);
        }
    }

    public static void main(String[] args) {
        SocketServer socketServer=new SocketServer();
        socketServer.start(1111);
    }
}
