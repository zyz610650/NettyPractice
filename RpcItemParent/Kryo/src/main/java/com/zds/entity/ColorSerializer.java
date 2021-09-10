package com.zds.entity;


import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.Serializer;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;


import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class ColorSerializer extends Serializer {


    public void write(Kryo kryo, Output output, Object o) {

    }

    public Object read(Kryo kryo, Input input, Class aClass) {
        return null;
    }
}
