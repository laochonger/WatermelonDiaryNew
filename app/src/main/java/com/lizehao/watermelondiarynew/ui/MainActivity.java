package com.lizehao.watermelondiarynew.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lizehao.watermelondiarynew.R;
//import com.lizehao.watermelondiarynew.TestActivity;
import com.lizehao.watermelondiarynew.bean.DiaryBean;
import com.lizehao.watermelondiarynew.db.DiaryDatabaseHelper;
import com.lizehao.watermelondiarynew.event.StartUpdateDiaryEvent;
import com.lizehao.watermelondiarynew.event.StarLookDiaryEvent;
import com.lizehao.watermelondiarynew.utils.AppManager;
import com.lizehao.watermelondiarynew.utils.GetDate;
import com.lizehao.watermelondiarynew.utils.SpHelper;
import com.lizehao.watermelondiarynew.utils.StatusBarCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
//R.id.myTextView1是指向布局中的空间TextView,这个Textview的id被设置成了"@+id/myTextView1",你到layout文件夹下找main.layout就能看到它。
    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;
    @Bind(R.id.common_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.main_iv_circle)
    ImageView mMainIvCircle;
    @Bind(R.id.main_tv_date)
    TextView mMainTvDate;
    @Bind(R.id.main_tv_content)
    TextView mMainTvContent;
    @Bind(R.id.item_ll_control)
    LinearLayout mItemLlControl;

    @Bind(R.id.main_rv_show_diary)
    RecyclerView mMainRvShowDiary;
    @Bind(R.id.main_fab_enter_edit)
    FloatingActionButton mMainFabEnterEdit;
    @Bind(R.id.main_rl_main)
    RelativeLayout mMainRlMain;
    @Bind(R.id.item_first)
    LinearLayout mItemFirst;
    @Bind(R.id.main_ll_main)
    LinearLayout mMainLlMain;
    private List<DiaryBean> mDiaryBeanList;

    private DiaryDatabaseHelper mHelper;

    private static String IS_WRITE = "true";

    private int mEditPosition = -1;

    /**
     * 标识今天是否已经写了日记
     */
    private boolean isWrite = false;
    private static TextView mTvTest;


    //从其他页面启动该页面时的数据传递
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        EventBus.getDefault().register(this);
        SpHelper spHelper = SpHelper.getInstance(this);
        getDiaryBeanList();
        initTitle();
        mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
        mMainRvShowDiary.setAdapter(new DiaryAdapter(this, mDiaryBeanList));
        mTvTest = new TextView(this);
        mTvTest.setText("hello world");
    }

    private void initTitle() {
        mMainTvDate.setText("今天，" + GetDate.getDate());
        mCommonTvTitle.setText("日记");
        mCommonIvBack.setVisibility(View.INVISIBLE);
        mCommonIvTest.setVisibility(View.INVISIBLE);

    }
//获得日记列表
    private List<DiaryBean> getDiaryBeanList() {

        mDiaryBeanList = new ArrayList<>();
        List<DiaryBean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String dateSystem = GetDate.getDate().toString();
                if (date.equals(dateSystem)) {
                    mMainLlMain.removeView(mItemFirst);
                    break;
                }
            } while (cursor.moveToNext());
        }


        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                String flag=cursor.getString(cursor.getColumnIndex("flag"));
//                String flag=cursor.getString(4);

//                String flag="YES";
                mDiaryBeanList.add(new DiaryBean(date, title, content, tag, flag));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    //geteventbuds
     @Subscribe
     public void startLookDiaryActivity(StarLookDiaryEvent event) {
        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
        String content = mDiaryBeanList.get(event.getPosition()).getContent();
        String tag = mDiaryBeanList.get(event.getPosition()).getTag();
        String flag = mDiaryBeanList.get(event.getPosition()).getFlag();
        LookDiaryActivity.startActivity(this, title, content, tag,flag);
    }
    @Subscribe
    public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
        String content = mDiaryBeanList.get(event.getPosition()).getContent();
        String tag = mDiaryBeanList.get(event.getPosition()).getTag();
        String flag = mDiaryBeanList.get(event.getPosition()).getFlag();
        UpdateDiaryActivity.startActivity(this, title, content, tag, flag);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //下面的那个加号
    @OnClick(R.id.main_fab_enter_edit)
    public void onClick() {
        AddDiaryActivity.startActivity(this);
    }

    //这里增加过了
    //上面的那个加号
    @OnClick(R.id.main_test)
    public void onClick2() {
        Fuck.startActivity(this);
    }

    //super关键字的核心就是指代当前类的父类
    //按下机身返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(this);
    }
}