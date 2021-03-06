package io.chatRoom.protocol;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import io.chatRoom.config.Config;
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
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list)  {

        ByteBuf out=channelHandlerContext.alloc().buffer();
        out.writeBytes(new byte[]{1,2,3,4});
        out.writeByte(0);
        out.writeByte(0);
        out.writeByte(message.getMessageType());
        out.writeInt(message.getSequenceId());
        out.writeByte(0xff);

        byte[] bytes= new byte[0];

        try {
            bytes = Config.getSerializerAlgorithm().serialize(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        list.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out)  {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int len=in.readInt();
        byte[] bytes=new byte[len];
        in.readBytes(bytes,0,len);

        Class<?extends Message> clasType=Message.getMessageClass(messageType);
        Message message = Config.getSerializerAlgorithm().desrialize(clasType,bytes);

        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, len);
        log.debug("{}", message);
        out.add(message);

    }
}
