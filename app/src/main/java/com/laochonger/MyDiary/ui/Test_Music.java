package com.laochonger.MyDiary.ui;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.laochonger.MyDiary.R;
import com.laochonger.MyDiary.service.MusicService;

public class Test_Music extends Activity {

    //为日志工具设置标签
    private static String TAG = "MusicService";
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, Test_Music.class);
        context.startActivity(intent);
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_music_diary);

        //输出Toast消息和日志记录
        Toast.makeText(this, "Test_Music",
                Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Test_Music");

        initlizeViews();
    }

    private void initlizeViews(){
        Button btnStart = (Button)findViewById(R.id.startMusic);
        Button btnStop = (Button)findViewById(R.id.stopMusic);
        Button btnBind = (Button)findViewById(R.id.bindMusic);
        Button btnUnbind = (Button)findViewById(R.id.unbindMusic);

        //定义点击监听器
        OnClickListener ocl = new OnClickListener() {

            @Override
            public void onClick(View v) {
                //显示指定  intent所指的对象是个   service
                Intent intent = new Intent(Test_Music.this, MusicService.class);
                switch(v.getId()){
                    case R.id.startMusic:
                        //开始服务
                        startService(intent);
                        break;
                    case R.id.stopMusic:
                        //停止服务
                        stopService(intent);
                        break;
                    case R.id.bindMusic:
                        //绑定服务
                        bindService(intent, conn, Context.BIND_AUTO_CREATE);
                        break;
                    case R.id.unbindMusic:
                        //解绑服务
                        unbindService(conn);
                        break;
                }
            }
        };

        //绑定点击监听
        btnStart.setOnClickListener(ocl);
        btnStop.setOnClickListener(ocl);
        btnBind.setOnClickListener(ocl);
        btnUnbind.setOnClickListener(ocl);
    }

    //定义服务链接对象
    final ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(Test_Music.this, "Test_Music onSeviceDisconnected"
                    , Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Test_Music onSeviceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(Test_Music.this, "Test_Music onServiceConnected"
                    ,Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Test_Music onServiceConnected");
        }
    };
}