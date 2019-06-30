package com.laochonger.MyDiary.ui;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.laochonger.MyDiary.R;
import com.laochonger.MyDiary.service.AlarmReceiver;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Test_Clock extends Activity {
    private TextView tv1;
    private Calendar calendar;  //日期类
    private int Year;       //年
    private int month;      //月
    private int day;        //日
    private int hour;       //时
    private int minute;     //分
    private int seconds;    //秒


    public static void startActivity(Context context){
        Intent intent = new Intent(context, Test_Clock.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.e("FF","xx");
        super.onCreate(savedInstanceState);
        //加载布局文件
        setContentView(R.layout.clock);
        tv1 = (TextView) findViewById(R.id.tv1);

        //AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        //实例化日期类


        //


        //
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);//获取当前年
        month = calendar.get(Calendar.MONTH)+1;//获取月份，加1是因为月份是从0开始计算的
        day = calendar.get(Calendar.DATE);//获取日
        hour = calendar.get(Calendar.HOUR);//获取小时
        minute = calendar.get(Calendar.MINUTE);//获取分钟
        seconds = calendar.get(Calendar.SECOND);//获取秒钟
        //设置标题栏显示当前事件
        tv1.setText("当前时间："+Year+"年"+month+"月"+day+"日   "+hour+":"+minute+":"+seconds);
        //Log.v("hh","当前时间："+Year+"年"+month+"月"+day+"日   "+hour+":"+minute+":"+seconds );
        //实例化日期选择器悬浮窗
        //参数1：上下文对象
        //参数2：监听事件
        //参数3：初始化年份
        //参数4：初始化月份
        //参数5：初始化日期
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //实现监听方法
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //设置文本显示内容，i为年，i1为月，i2为日
                tv1.setText("当前时间："+i+"年"+(i1+1)+"月"+i2+"日   "+hour+":"+minute+":"+seconds);
                //以下赋值给全局变量，是为了后面的时间选择器，选择时间的时候不会获取不到日期！
                Year=i;
                month=i1+1;
                day=i2;
            }
        },Year,month,day).show();//记得使用show才能显示悬浮窗

        //实例化时间选择器
        //参数1：上下文对象
        //参数2：监听事件
        //参数3：初始化小时
        //参数4：初始化分钟
        //参数5：是否24小时制
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //实现监听方法
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                //设置文本显示内容
                tv1.setText("当前时间："+Year+"年"+month+"月"+day+"日   "+i+":"+i1);
                hour=i;minute=i1;
            }
        },hour,minute,true).show();//记得使用show才能显示！
        String a="";
        a = a + Year + month + day + hour +minute+seconds;

    }

    @OnClick(R.id.add_clock)
//    public void onClick() {
//        AddDiaryActivity.startActivity(this);
//    }
    public void onClick(){
        Log.v("fff", "ffffff");
        Intent i=new Intent(Test_Clock.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(Test_Clock.this, Integer.valueOf(1) , i, 0);//通过getBroadcast第二个参数区分闹钟，将查询得到的note的ID值作为第二个参数。

        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        String aaa="";
        String bbb="";
        aaa = aaa + calendar.get(Calendar.YEAR)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.DATE)+calendar.get(Calendar.HOUR)
                    +calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND);
        calendar.set(Year,month-1,day,hour,minute,seconds);
        bbb = bbb + Year + month + day +hour + minute +seconds;
        Log.e("before", aaa);
        Log.e("after",bbb);
        String a = "";
        //a = a+Year+month+day+hour+minute+seconds;

        long x=calendar.getTimeInMillis();
        long y=System.currentTimeMillis();
        String b = "";
        b = b + y;
        a = a + x;
        Log.e("FF",a);
        Log.e("ff",b);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pi);//calendar.getTimeInMillis(), pi);//设置闹铃 

    }
}
//1564388986268
//1561796934024