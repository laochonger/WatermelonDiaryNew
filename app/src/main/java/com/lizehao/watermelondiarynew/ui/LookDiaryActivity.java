package com.lizehao.watermelondiarynew.ui;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by 李 on 2017/1/26.
 */
public class LookDiaryActivity extends AppCompatActivity {

    @Bind(R.id.look_diary_tv_date)
    TextView mLookDiaryTvDate;
    @Bind(R.id.look_diary_et_title)
    EditText mLookDiaryEtTitle;
    @Bind(R.id.look_diary_et_content)
    LinedEditText mLookDiaryEtContent;
    @Bind(R.id.look_diary_fab_back)
    FloatingActionButton mLookDiaryFabBack;
    @Bind(R.id.look_diary_fab_add)
    FloatingActionButton mLookDiaryFabAdd;
    @Bind(R.id.look_diary_fab_delete)
    FloatingActionButton mLookDiaryFabDelete;
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
    @Bind(R.id.look_diary_tv_tag)
    TextView mTvTag;

//    android.support.design.widget.FloatingActionButton mUpEnMu;
//    @Bind(R.id.main_fab_enter_music)
//    FloatingActionButton mMainFabEnterMusic;
//    @Bind(R.id.main_fab_exit_music)
//    FloatingActionButton mMainFabExitMusic;

    private DiaryDatabaseHelper mHelper;
    private static String Flag="NO";

    public static void startActivity(Context context, String title, String content, String tag, String flag) {
        Flag=flag;
        Log.v("hellobug",Flag+" hhhhhhhhhhhhhhhhhhhh");

        Intent intent = new Intent(context, LookDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("tag", tag);
        context.startActivity(intent);
    }
    android.support.design.widget.FloatingActionButton floatingActionButton1;
    android.support.design.widget.FloatingActionButton floatingActionButton2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //拉布局
        setContentView(R.layout.activity_look_diary);

        floatingActionButton1=(android.support.design.widget.FloatingActionButton)findViewById(R.id.main_fab_enter_music);
        floatingActionButton2=(android.support.design.widget.FloatingActionButton)findViewById(R.id.main_fab_exit_music);

        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        initTitle();
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        //将该项目中包含的原始intent检索出来，
        Intent intent = getIntent();
        mLookDiaryTvDate.setText("今天，" + GetDate.getDate());
        mLookDiaryEtTitle.setText(intent.getStringExtra("title"));
        mLookDiaryEtContent.setText(intent.getStringExtra("content"));
        mTvTag.setText(intent.getStringExtra("tag"));


    }

    private void initTitle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mCommonTvTitle.setText("查看日记");
    }

    @OnClick({R.id.common_iv_back, R.id.look_diary_tv_date, R.id.look_diary_et_title, R.id.look_diary_et_content, R.id.look_diary_fab_back, R.id.look_diary_fab_add, R.id.look_diary_fab_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                super.finish();
                MainActivity.startActivity(this);
            case R.id.look_diary_tv_date:
                break;
            case R.id.look_diary_et_title:
                break;
            case R.id.look_diary_et_content:
                break;
            case R.id.look_diary_fab_back:
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("确定要删除该日记吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        String title = mLookDiaryEtTitle.getText().toString();
                        String tag = mTvTag.getText().toString();
                        SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
                        dbDelete.delete("Diary", "tag = ?", new String[]{tag});
                        MainActivity.startActivity(LookDiaryActivity.this);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            case R.id.look_diary_fab_add:
                SQLiteDatabase dbLook = mHelper.getWritableDatabase();
                ContentValues valuesLook = new ContentValues();
                String title = mLookDiaryEtTitle.getText().toString();
                String content = mLookDiaryEtContent.getText().toString();
                valuesLook.put("title", title);
                valuesLook.put("content", content);
                valuesLook.put("flag", Flag);
                dbLook.update("Diary", valuesLook, "title = ?", new String[]{title});
                //dbLook.update("Diary", valuesLook, "content = ?", new String[]{content});
                MainActivity.startActivity(this);
                break;
            case R.id.look_diary_fab_delete:
                MainActivity.startActivity(this);

                break;
        }
    }

    //留下点击标题的空白方法
    @OnClick(R.id.common_tv_title)
    public void onClick() {
    }
    Intent intent;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.main_fab_enter_music)
    public void onClick_startMusic(){
//        final MusicController app = (MusicController) getApplication();
       // boolean flag=MusicController.getB();
        String a="YES";
        if(Flag.equals(a)){
            //包名重了可还行？？
            //改头换面失败 X 3
//            final FloatingActionButton floatingActionButton=(FloatingActionButton)this.findViewById(R.id.main_fab_enter_music);
//            floatingActionButton.setBackgroundResource(R.drawable.add);
//            (FloatingActionButton)this.findViewById(R.id.main_fab_enter_music).setBackground(R.drawable.delete_new)
            intent= new Intent(LookDiaryActivity.this, MusicService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
//            final FloatingActionButton floatingActionButton=(FloatingActionButton)this.findViewById(R.id.main_fab_enter_music);
            floatingActionButton1.setVisibility(View.GONE);
//            final FloatingActionButton floatingActionButton2=(FloatingActionButton)this.findViewById(R.id.main_fab_exit_music);
            floatingActionButton2.setVisibility(View.VISIBLE);

        }else{
            Toast.makeText(this, "没添加你就想听歌？你在想屁吃？"
                    , Toast.LENGTH_SHORT).show();

        }

    }
    @OnClick(R.id.main_fab_exit_music)
    public void onClick_stopMusic(){
//        Intent intent = new Intent(LookDiaryActivity.this, MusicService.class);
        unbindService(conn);
        floatingActionButton1.setVisibility(View.VISIBLE);
        floatingActionButton2.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.startActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    final ServiceConnection conn = new ServiceConnection() {

        private String TAG = "MusicService";

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(LookDiaryActivity.this, "MusicServiceActivity onSeviceDisconnected"
                    , Toast.LENGTH_SHORT).show();
            Log.e(TAG, "MusicServiceActivity onSeviceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(LookDiaryActivity.this, "MusicServiceActivity onServiceConnected"
                    , Toast.LENGTH_SHORT).show();
            Log.e(TAG, "MusicServiceActivity onServiceConnected");
        }
    };
}