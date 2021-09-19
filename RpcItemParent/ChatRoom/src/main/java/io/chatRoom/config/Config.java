package io.chatRoom.config;

import io.chatRoom.protocol.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
// getResourceAsStream https://www.cnblogs.com/macwhirr/p/8116583.html
public abstract class Config {
    static Properties properties;
    static {
        try(InputStream in=Config.class.getResourceAsStream("/application.properties"))
        {
            properties=new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Serializer.SerializerAlgorithm getSerializerAlgorithm()
    {
        String value=properties.getProperty("serializer.algorithm");
        System.out.println(value);
        if (value==null) return Serializer.SerializerAlgorithm.Java;
        else return Serializer.SerializerAlgorithm.valueOf(value);
    }
}
