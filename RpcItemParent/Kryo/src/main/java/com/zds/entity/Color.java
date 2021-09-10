package com.zds.entity;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class Color {


        public static void main(String[] args) {



            WeakReference<String> sr = new WeakReference<String>(new String("hello"));



            System.out.println(sr.get());

            System.gc();                //通知JVM的gc进行垃圾回收

            System.out.println(sr.get());



        }


}
