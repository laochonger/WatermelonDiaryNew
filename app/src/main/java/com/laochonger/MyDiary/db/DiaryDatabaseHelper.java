package com.laochonger.MyDiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DiaryDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 创建一个简单的表
     * key 时间 标题 主体 时间（毫秒） 标记（是否有背景音乐） 时钟（的id）
     */
    public static final String CREATE_DIARY = "create table Diary("
            + "id integer primary key autoincrement, "
            + "date text, "
            + "title text, "
            + "tag text, "
            + "content text,"
            + "flag text,"
            +"clock integer)";

    private Context mContext;
    public DiaryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists Diary");
        onCreate(db);
    }
}
