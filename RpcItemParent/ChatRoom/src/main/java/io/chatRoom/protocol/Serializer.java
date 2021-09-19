package io.chatRoom.protocol;


import com.alibaba.fastjson.JSON;

import java.io.*;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public interface Serializer {

    <T> T desrialize(Class<T> clazz,byte[] bytes);

    <T> byte[] serialize(T object);

    // 枚举 https://blog.csdn.net/echizao1839/article/details/80890490
    enum SerializerAlgorithm implements Serializer{

        Java{
            @Override
            public <T> T desrialize(Class<T> clazz, byte[] bytes)  {
                System.out.println("序列化");
                try {
                    ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
                    ObjectInputStream ois=new ObjectInputStream(bis);
                    return (T)ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public <T> byte[] serialize(T object) {
                System.out.println("序列化");
                try {
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    ObjectOutputStream oos=new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new byte[0];
            }
        },

        Json{
            @Override
            public <T> T desrialize(Class<T> clazz, byte[] bytes) {
                return JSON.parseObject(bytes,clazz);
            }

            @Override
            public <T> byte[] serialize(T object) {
               return JSON.toJSONBytes(object);
            }
        };
        public static SerializerAlgorithm  getByInt(int type)
        {
            SerializerAlgorithm[] array=SerializerAlgorithm.values();
            if (type < 0 || type > array.length - 1) {
                throw new IllegalArgumentException("超过 SerializerAlgorithm 范围");
            }
            return array[type];
        }

    }
}
