package com.laochonger.MyDiary.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 用于定时提醒的广播接收器
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(this, "MusicSevice onUnbind()"
//                , Toast.LENGTH_SHORT).show();
        //当系统到我们设定的时间点的时候会发送广播，执行这里
        Toast.makeText(context, intent.getStringExtra("title"), Toast.LENGTH_LONG).show();
        //Log.e("fuck", "nmsl");
    }
}