package com.laochonger.MyDiary.ui;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.laochonger.MyDiary.R;
import com.laochonger.MyDiary.db.DiaryDatabaseHelper;
import com.laochonger.MyDiary.service.AlarmReceiver;
import com.laochonger.MyDiary.utils.AppManager;
import com.laochonger.MyDiary.utils.Cnt;
import com.laochonger.MyDiary.utils.StatusBarCompat;
import com.laochonger.MyDiary.widget.LinedEditText;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

/**
 * 添加备忘录
 */
public class AddNoteActivity extends AppCompatActivity {

    @Bind(R.id.add_note_tv_date)
    TextView mAddDiaryTvDate;
    @Bind(R.id.add_note_et_title)
    EditText mAddDiaryEtTitle;
    @Bind(R.id.add_note_et_content)
    LinedEditText mAddDiaryEtContent;
    @Bind(R.id.add_note_fab_back)
    FloatingActionButton mAddDiaryFabBack;
    @Bind(R.id.add_note_fab_add)
    FloatingActionButton mAddDiaryFabAdd;

    @Bind(R.id.note_right_labels)
    FloatingActionsMenu mRightLabels;
    @Bind(R.id.note_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.note_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.note_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.note_iv_test)
    ImageView mCommonIvTest;

    private Calendar calendar;  //日期类
    private int Year;       //年
    private int month;      //月
    private int day;        //日
    private int hour;       //时
    private int minute;     //分
    private int seconds;    //秒
    private String hSetTime = "";

    private DiaryDatabaseHelper mHelper;
    private static String Flag="NO";

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddNoteActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String title, String content) {
        Intent intent = new Intent(context, AddNoteActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局
        setContentView(R.layout.activity_add_note);



        AppManager.getAppManager().addActivity(this);
        //绑定View
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        mAddDiaryEtTitle.setText(intent.getStringExtra("title"));
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        //提醒时间的初始化
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);//获取当前年
        month = calendar.get(Calendar.MONTH)+1;//获取月份，加1是因为月份是从0开始计算的
        day = calendar.get(Calendar.DATE);//获取日
        hour = calendar.get(Calendar.HOUR);//获取小时
        minute = calendar.get(Calendar.MINUTE);//获取分钟
        seconds = calendar.get(Calendar.SECOND);//获取秒钟
        //日历的悬浮窗口
        //设置标题栏显示当前事件
        mAddDiaryTvDate.setText("提醒时间："+Year+"年"+month+"月"+day+"日   "+hour+":"+minute+":"+seconds);
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
                hSetTime = hSetTime + i+"年"+i1+"月"+i2+"日"+hour+":"+minute+":"+seconds;
                //calendar.set(i,i1,i2,hour,minute,seconds);
                mAddDiaryTvDate.setText("提醒时间："+i+"年"+(i1+1)+"月"+i2+"日   "+hour+":"+minute+":"+seconds);
//                Toast.makeText(AddNoteActivity.this, "???"
//                        , Toast.LENGTH_SHORT).show();
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

                mAddDiaryTvDate.setText("提醒时间："+Year+"年"+month+"月"+day+"日   "+i+":"+i1);
                hour=i;minute=i1;
            }
        },hour,minute,true).show();//记得使用show才能显示！

//        hSetTime = hSetTime + Year + "年" + month + "月" + day + "日"+ hour+"时"+ minute+ "分";
        Log.e("ff", hSetTime);
        mCommonTvTitle.setText("添加备忘录");
        //mAddDiaryTvDate.setText("今天，" + GetDate.getDate());
        mAddDiaryEtContent.setText(intent.getStringExtra("content"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        mAddDiaryTvDate.setText("提醒时间："+Year+"年"+month+"月"+day+"日   "+hour+":"+minute+":"+seconds);
        Toast.makeText(AddNoteActivity.this, hSetTime
                , Toast.LENGTH_SHORT).show();
    }

    //选择悬浮按钮
    @OnClick({R.id.note_iv_back, R.id.add_note_et_title, R.id.add_note_et_content, R.id.add_note_fab_back, R.id.add_note_fab_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.note_iv_back:
                ShowNoteActivity.startActivity(this);
            case R.id.add_note_et_title:
                break;
            case R.id.add_note_et_content:
                break;
            case R.id.add_note_fab_back://保存
//                String date = GetDate.getDate().toString();
                String date = hSetTime;
                String tag = String.valueOf(System.currentTimeMillis());
                final String title = mAddDiaryEtTitle.getText().toString() + "";
                final String content = mAddDiaryEtContent.getText().toString() + "";
                if (!title.equals("") || !content.equals("")) {

                    /**
                     * 开启定时提醒服务
                     */
                    int a= Cnt.getA();
                    Cnt.setA(a+1);
                    Intent i=new Intent(AddNoteActivity.this, AlarmReceiver.class);
                    i.putExtra("title", title);
                    i.putExtra("content", content);
                    PendingIntent pi = PendingIntent.getBroadcast(AddNoteActivity.this, Integer.valueOf(1) , i, 0);//通过getBroadcast第二个参数区分闹钟，将查询得到的note的ID值作为第二个参数。
                    AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                    calendar.set(Year,month-2,day,hour,minute,seconds);

                    Log.e("fff", hSetTime);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pi);//calendar.getTimeInMillis(), pi);//设置闹铃 

//                    Intent ii=new Intent(AddNoteActivity.this, AlarmReceiver.class);
//                    PendingIntent pii = PendingIntent.getBroadcast(AddNoteActivity.this, Integer.valueOf(1) , ii, 0);
//                    am.cancel(pii);

                    AddNoteActivity.startActivity(this);
                    Log.e("fff", hSetTime);
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", date);
                    values.put("title", title);
                    values.put("content", content);
                    values.put("tag", tag);
                    values.put("flag", Flag);
                    values.put("clock",a);
                    db.insert("Diary", null, values);
                    values.clear();
                }
                ShowNoteActivity.startActivity(this);
                break;
                //点击红色X后，选择删除或者保存
            case R.id.add_note_fab_add:
//                final String dateBack = GetDate.getDate().toString();
                final String dateBack  = hSetTime;
                final String titleBack = mAddDiaryEtTitle.getText().toString();
                final String contentBack = mAddDiaryEtContent.getText().toString();
                if(!titleBack.isEmpty() || !contentBack.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("是否保存备忘录内容？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            /**
                             * 开启定时提醒服务
                             */
                            int a= Cnt.getA();
                            Cnt.setA(a+1);
                            Intent i=new Intent(AddNoteActivity.this, AlarmReceiver.class);
                            i.putExtra("title", titleBack);
                            i.putExtra("content", contentBack);
                            PendingIntent pi = PendingIntent.getBroadcast(AddNoteActivity.this, Integer.valueOf(++a) , i, 0);//通过getBroadcast第二个参数区分闹钟，将查询得到的note的ID值作为第二个参数。
                            AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                            calendar.set(Year,month-2,day,hour,minute,seconds);
                            Log.e("fff", "fffff");
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pi);//calendar.getTimeInMillis(), pi);//设置闹铃 
//                            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+10*1000,pi);

                            //插入数据库
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("date", dateBack);
                            values.put("title", titleBack);
                            values.put("content", contentBack);
                            values.put("flag", Flag);
                            values.put("clock",a);
                            Log.v("fuck", Flag +"wwww");
                            db.insert("Diary", null, values);
                            values.clear();
                            ShowNoteActivity.startActivity(AddNoteActivity.this);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //返回
                            ShowNoteActivity.startActivity(AddNoteActivity.this);
                        }
                    }).show();
                }else{
                    //返回
                    ShowNoteActivity.startActivity(this);
                }
                break;
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ShowNoteActivity.startActivity(this);
    }
}