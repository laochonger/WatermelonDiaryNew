package com.laochonger.MyDiary.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.laochonger.MyDiary.R;
import com.laochonger.MyDiary.utils.AppManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李 on 2017/1/26.
 */
public class BaseClockActivity extends AppCompatActivity {

    @Bind(R.id.tv_event)
    TextView mtitile;
    private static String Title="";


    public static void startActivity(Context context,String title) {
        Title = title;
        Intent intent = new Intent(context, AddDiaryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_clock);



        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        mtitile.setText(Title);
//        mAddDiaryEtTitle.setText(intent.getStringExtra("title"));
//        StatusBarCompat.compat(this, Color.parseColor("#161414"));

//        mCommonTvTitle.setText("添加日记");
//        mAddDiaryTvDate.setText("今天，" + GetDate.getDate());
//        mAddDiaryEtContent.setText(intent.getStringExtra("content"));
//        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
    }

    @OnClick(R.id.btn_confirm)
    public void onclick(){

    }





}











