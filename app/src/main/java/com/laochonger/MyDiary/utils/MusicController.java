package com.laochonger.MyDiary.utils;


/**
 * 本来作为音乐控制开关，后被否定
 */
public class MusicController{
    private static boolean b=true;

    public static boolean getB(){
        return MusicController.b;
    }
    public static void setB(boolean c){
        MusicController.b= c;
    }

}