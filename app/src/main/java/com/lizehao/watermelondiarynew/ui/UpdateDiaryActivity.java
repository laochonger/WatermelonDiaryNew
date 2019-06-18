package com.lizehao.watermelondiarynew.ui;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lizehao.watermelondiarynew.R;
import com.lizehao.watermelondiarynew.db.DiaryDatabaseHelper;
import com.lizehao.watermelondiarynew.service.MusicService;
import com.lizehao.watermelondiarynew.utils.AppManager;
import com.lizehao.watermelondiarynew.utils.GetDate;
import com.lizehao.watermelondiarynew.utils.MusicController;
import com.lizehao.watermelondiarynew.utils.StatusBarCompat;
import com.lizehao.watermelondiarynew.widget.LinedEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by 李 on 2017/1/26.
 */
public class UpdateDiaryActivity extends AppCompatActivity {

    @Bind(R.id.update_diary_tv_date)
    TextView mUpdateDiaryTvDate;
    @Bind(R.id.update_diary_et_title)
    EditText mUpdateDiaryEtTitle;
    @Bind(R.id.update_diary_et_content)
    LinedEditText mUpdateDiaryEtContent;
    @Bind(R.id.update_diary_fab_back)
    FloatingActionButton mUpdateDiaryFabBack;
    @Bind(R.id.update_diary_fab_add)
    FloatingActionButton mUpdateDiaryFabAdd;
    @Bind(R.id.update_diary_fab_delete)
    FloatingActionButton mUpdateDiaryFabDelete;
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
    @Bind(R.id.update_diary_tv_tag)
    TextView mTvTag;


    private DiaryDatabaseHelper mHelper;
    private static String Flag="NO";

    public static void startActivity(Context context, String title, String content, String tag, String flag) {
        Intent intent = new Intent(context, UpdateDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("tag", tag);
        Flag=flag;
        Log.v("fuckbug",Flag+" hhhhhhhhhhhhhhhhhhhh");


        context.startActivity(intent);
    }

    android.support.design.widget.FloatingActionButton floatingActionButton1;
    android.support.design.widget.FloatingActionButton floatingActionButton2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diary);

        floatingActionButton1=(android.support.design.widget.FloatingActionButton)findViewById(R.id.update_enter_music);
        floatingActionButton2=(android.support.design.widget.FloatingActionButton)findViewById(R.id.update_exit_music);
        String a="YES";

        Log.v("debug",Flag+" hhhhhhhhhhhhhhhhhhhh");
        if(Flag.equals(a)){
            floatingActionButton1.setVisibility(View.GONE);
            floatingActionButton2.setVisibility(View.VISIBLE);
        }else{
            floatingActionButton2.setVisibility(View.GONE);
            floatingActionButton1.setVisibility(View.VISIBLE);
        }


        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        initTitle();
        StatusBarCompat.compat(this, Color.parseColor("#161414"));

        Intent intent = getIntent();
        mUpdateDiaryTvDate.setText("今天，" + GetDate.getDate());
        mUpdateDiaryEtTitle.setText(intent.getStringExtra("title"));
        mUpdateDiaryEtContent.setText(intent.getStringExtra("content"));
        mTvTag.setText(intent.getStringExtra("tag"));


    }

    private void initTitle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mCommonTvTitle.setText("修改日记");
    }
//选择一个按钮进行响应事件的响应
    @OnClick({R.id.common_iv_back, R.id.update_diary_tv_date, R.id.update_diary_et_title, R.id.update_diary_et_content, R.id.update_diary_fab_back, R.id.update_diary_fab_add, R.id.update_diary_fab_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                MainActivity.startActivity(this);
            case R.id.update_diary_tv_date:
                break;
            case R.id.update_diary_et_title:
                break;
            case R.id.update_diary_et_content:
                break;
            case R.id.update_diary_fab_back:
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("确定要删除该日记吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        String title = mUpdateDiaryEtTitle.getText().toString();
                        String tag = mTvTag.getText().toString();
                        SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
                        dbDelete.delete("Diary", "tag = ?", new String[]{tag});
                        MainActivity.startActivity(UpdateDiaryActivity.this);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            case R.id.update_diary_fab_add:
                SQLiteDatabase dbUpdate = mHelper.getWritableDatabase();
                ContentValues valuesUpdate = new ContentValues();
                String title = mUpdateDiaryEtTitle.getText().toString();
                String content = mUpdateDiaryEtContent.getText().toString();
                valuesUpdate.put("title", title);
                valuesUpdate.put("content", content);
                valuesUpdate.put("flag",Flag);

                //这里有问题，数据库查询的准确度不高
                dbUpdate.update("Diary", valuesUpdate, "title = ?", new String[]{title});
//                dbUpdate.update("Diary", valuesUpdate, "content = ?", new String[]{content});
                MainActivity.startActivity(this);
                break;
            case R.id.update_diary_fab_delete:
                //不保存
                MainActivity.startActivity(this);

                break;
        }
    }

    @OnClick(R.id.common_tv_title)
    public void onClick() {
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.update_enter_music)
    public void onClick_startMusic(){
            Flag="YES";
//          FloatingActionButton floatingActionButton=(FloatingActionButton)this.findViewById(R.id.main_fab_enter_music);
            floatingActionButton1.setVisibility(View.GONE);
            floatingActionButton2.setVisibility(View.VISIBLE);
            Toast.makeText(this, "背景音乐添加成功"
                , Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.update_exit_music)
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