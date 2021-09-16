package io.chatRoom.protocol;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import io.chatRoom.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageCodec;
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
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf,Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {
        ByteBuf out=channelHandlerContext.alloc().buffer();
        out.writeBytes(new byte[]{1,2,3,4});
        out.writeByte(0);
        out.writeByte(0);
        out.writeByte(message.getMessageType());
        out.writeInt(message.getSequenceId());
        out.writeByte(0xff);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream ois=new ObjectOutputStream(bos);
        ois.writeObject(message);
        byte[] bytes=bos.toByteArray();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        list.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int len=in.readInt();
        byte[] bytes=new byte[len];
        in.readBytes(bytes,0,len);
        ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message= (Message) ois.readObject();
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, len);
        log.debug("{}", message);
        out.add(message);

    }
}
