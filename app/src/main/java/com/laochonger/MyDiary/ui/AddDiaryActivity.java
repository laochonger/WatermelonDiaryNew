package com.laochonger.MyDiary.ui;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laochonger.MyDiary.R;
import com.laochonger.MyDiary.db.DiaryDatabaseHelper;
import com.laochonger.MyDiary.utils.AppManager;
import com.laochonger.MyDiary.utils.GetDate;
import com.laochonger.MyDiary.utils.StatusBarCompat;
import com.laochonger.MyDiary.widget.LinedEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

/**
 * 添加新日记
 */


public class AddDiaryActivity extends AppCompatActivity {

    @Bind(R.id.add_diary_tv_date)
    TextView mAddDiaryTvDate;
    @Bind(R.id.add_diary_et_title)
    EditText mAddDiaryEtTitle;
    @Bind(R.id.add_diary_et_content)
    LinedEditText mAddDiaryEtContent;
    @Bind(R.id.add_diary_fab_back)
    FloatingActionButton mAddDiaryFabBack;
    @Bind(R.id.add_diary_fab_add)
    FloatingActionButton mAddDiaryFabAdd;

    @Bind(R.id.right_labels)
    FloatingActionsMenu mRightLabels;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.common_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;

    private DiaryDatabaseHelper mHelper;

    //是否设置歌曲
    private static String Flag="NO";

    //用于添加/删除歌曲的两个按钮
    android.support.design.widget.FloatingActionButton floatingActionButton1;
    android.support.design.widget.FloatingActionButton floatingActionButton2;
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String title, String content) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局
        setContentView(R.layout.activity_add_diary);
        //控件实例化
        floatingActionButton1=(android.support.design.widget.FloatingActionButton)findViewById(R.id.add_enter_music);
        floatingActionButton2=(android.support.design.widget.FloatingActionButton)findViewById(R.id.add_exit_music);

        AppManager.getAppManager().addActivity(this);
        //绑定View
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        mAddDiaryEtTitle.setText(intent.getStringExtra("title"));
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        //设置布局中的文字
        mCommonTvTitle.setText("添加日记");
        mAddDiaryTvDate.setText("今天，" + GetDate.getDate());
        mAddDiaryEtContent.setText(intent.getStringExtra("content"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
    }

    /**
     * 一个悬浮按钮，用于选择保存与删除按钮
     * @param view
     */
    @OnClick({R.id.common_iv_back, R.id.add_diary_et_title, R.id.add_diary_et_content, R.id.add_diary_fab_back, R.id.add_diary_fab_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                MainActivity.startActivity(this);
            case R.id.add_diary_et_title:
                break;
            case R.id.add_diary_et_content:
                break;
                //确定添加新的日记
            case R.id.add_diary_fab_back:
                String date = GetDate.getDate().toString();
                String tag = String.valueOf(System.currentTimeMillis());
                String title = mAddDiaryEtTitle.getText().toString() + "";
                String content = mAddDiaryEtContent.getText().toString() + "";
                if (!title.equals("") || !content.equals("")) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", date);
                    values.put("title", title);
                    values.put("content", content);
                    values.put("tag", tag);
                    values.put("flag", Flag);
                    values.put("clock",0);
                    //插入数据库
                    db.insert("Diary", null, values);
                    values.clear();
                }
                MainActivity.startActivity(this);
                break;
                //选择删除或者保存
            case R.id.add_diary_fab_add:
                final String dateBack = GetDate.getDate().toString();
                final String titleBack = mAddDiaryEtTitle.getText().toString();
                final String contentBack = mAddDiaryEtContent.getText().toString();
                if(!titleBack.isEmpty() || !contentBack.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("是否保存日记内容？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("date", dateBack);
                            values.put("title", titleBack);
                            values.put("content", contentBack);
                            values.put("flag", Flag);
                            values.put("clock",0);
                            Log.v("fuck", Flag +"wwww");
                            db.insert("Diary", null, values);
                            values.clear();
                            MainActivity.startActivity(AddDiaryActivity.this);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //返回main
                            MainActivity.startActivity(AddDiaryActivity.this);
                        }
                    }).show();
                }else{
                    //返回main
                    MainActivity.startActivity(this);
                }
                break;
        }
    }


    /**
     * 添加背景音乐
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.add_enter_music)
    public void onClick_startMusic(){
        Flag="YES";
//          FloatingActionButton floatingActionButton=(FloatingActionButton)this.findViewById(R.id.main_fab_enter_music);
        floatingActionButton1.setVisibility(View.GONE);
        floatingActionButton2.setVisibility(View.VISIBLE);
        Toast.makeText(this, "背景音乐添加成功"
                , Toast.LENGTH_SHORT).show();
    }

    /**
     * 删除背景音乐
     */
    @OnClick(R.id.add_exit_music)
    public void onClick_stopMusic(){
        Flag="NO";
        floatingActionButton1.setVisibility(View.VISIBLE);
        floatingActionButton2.setVisibility(View.GONE);
        Toast.makeText(this, "背景音乐移除成功"
                , Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.startActivity(this);
    }
}











