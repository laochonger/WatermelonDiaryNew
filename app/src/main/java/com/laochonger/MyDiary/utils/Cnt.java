package com.laochonger.MyDiary.utils;


/**
 * 作为全局变量，用于取消对应的定时提醒
 */
public class Cnt {
    private static int a=1;

    public static int getA() {
        return a;
    }

    public static void setA(int a) {
        Cnt.a = a;
    }
}
