package com.zds.Serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zds.entity.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 * 中文开发文档：
 * https://blog.csdn.net/fanjunjaden/article/details/72823866?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-12.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-12.control
 */
public class kryoSerializableDemo {

    public static void main(String[] args) {
        output();
        input();
    }

    public static void output()  {
        User user=new User();
        user.setId(1000);
        user.setName("测试数据");
        Kryo kryo=new Kryo();
        Output output=null;

        try {
            output=new Output(new FileOutputStream("D:\\project\\java\\RpcItem\\Kryo\\src\\main\\java\\com\\zds\\Serializable\\user.txt"));
            kryo.writeObject(output,user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            output.close();
        }

    }
    public static void input()
    {
        Input input = null;
        Kryo kryo=new Kryo();
        try {
            input=new Input(new FileInputStream(("D:\\project\\java\\RpcItem\\Kryo\\src\\main\\java\\com\\zds\\Serializable\\user.txt")));
            User user=kryo.readObject(input,User.class);
            System.out.println(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            input.close();
        }
    }
    }
