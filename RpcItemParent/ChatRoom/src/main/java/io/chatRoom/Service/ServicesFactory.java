package io.chatRoom.Service;

import io.chatRoom.config.Config;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Getter
public class ServicesFactory {
    private static Properties properties;
    private static Map<Class<?>,Class<?>>map=new ConcurrentHashMap<>();
    static {
        try(InputStream in= Config.class.getResourceAsStream("/application.properties"))
        {
            properties=new Properties();
            properties.load(in);
            Set<String> names = properties.stringPropertyNames();
            for (String name:names)
            {
                if (name.endsWith("Service"))
                {
                    Class<?> interfaceClass=Class.forName(name);
                    Class<?> instanceClass=Class.forName(properties.getProperty(name));
                    map.put(interfaceClass,instanceClass);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getService(Class<T> interfaceClass){
        return (T) map.get(interfaceClass);
    }
}
