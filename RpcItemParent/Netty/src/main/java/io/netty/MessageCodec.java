package io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.message.LoginRequestMessage;
import io.netty.message.Message;
import lombok.extern.slf4j.Slf4j;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    // 出战时调用
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(new byte[]{1,2,3,4});
        byteBuf.writeByte(1);
        byteBuf.writeByte(0);
        byteBuf.writeByte(message.getMessageType());
        byteBuf.writeInt(message.getSequenceId());
        byteBuf.writeByte(0xff);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes=bos.toByteArray();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        
    }
    // 入战时调用
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        System.out.println("!");
        int magicNum=in.readInt();
        byte version=in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int len=in.readInt();
        byte[] bytes=new byte[len];
        in.readBytes(bytes,0,len);
        ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message msg= (Message) ois.readObject();
        log.debug("start");
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, len);
        log.debug("{}", msg);
        list.add(msg);//加入是为了给一个handler，要么下一个hadnler拿不到这个message
    }

    public static void main(String[] args) throws Exception {

        EmbeddedChannel channel=new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,12,4,0,0)
                ,new LoggingHandler()
                ,new MessageCodec());
        LoginRequestMessage message=new LoginRequestMessage("zhangsan","123");
     // channel.writeAndFlush(message);
    //channel.writeInbound(message);
      ByteBuf buf= ByteBufAllocator.DEFAULT.buffer();
     new MessageCodec().encode(null,message,buf);
     channel.writeInbound(buf);
    }
}
