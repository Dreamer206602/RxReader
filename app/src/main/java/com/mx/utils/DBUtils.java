package com.mx.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mx.config.Config;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class DBUtils {
    private static DBUtils sDBUtils;
    private SQLiteDatabase mSQLiteDatabase;

    public DBUtils(Context context) {
        mSQLiteDatabase = new DBHelper(context, Config.DB__IS_READ_NAME + ".db").getWritableDatabase();
    }

    public static synchronized DBUtils getDBUtils(Context context) {
        if (sDBUtils == null) {
            sDBUtils = new DBUtils(context);

        }
        return sDBUtils;
    }

    public void insertHasRead(String table, String key, int value) {
        Cursor cursor = mSQLiteDatabase
                .query(table, null, null, null, null, null, "id asc");
        if (cursor.getCount() > 200) {
            if (cursor.moveToNext()) {
                mSQLiteDatabase.delete(table, "id=?",
                        new String[]{
                                String.valueOf(cursor.getInt(
                                        cursor.getColumnIndex("id")))});

            }
            cursor.close();
            ContentValues contentValues=new ContentValues();
            contentValues.put("key",key);
            contentValues.put("is_read",value);
            mSQLiteDatabase.insertWithOnConflict(table,null,contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
    }
    public boolean isRead(String table,String key,int value){
        boolean isRead=false;
        Cursor cursor=mSQLiteDatabase.
                query(table,null,"key=?", new String[]{key},null,null,null);

        if(cursor.moveToNext()&&
                (cursor.getInt(cursor.getColumnIndex("is_read"))==value)){
            isRead=true;
        }
        cursor.close();
        return isRead;
    }


    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name) {
            super(context, name, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists "
                    + Config.GUOKR + "(id integer primary key autoincrement,key text unique,is_read integer);");
            db.execSQL("create table if not exists "
                    + Config.IT + "(id integer primary key autoincrement,key text unique,is_read integer);");
            db.execSQL("create table if not exists "
                    + Config.VIDEO + "(id integer primary key autoincrement,key text unique,is_read integer);");
            db.execSQL("create table if not exists "
                    + Config.ZHIHU + "(id integer primary key autoincrement,key text unique,is_read integer);");
            db.execSQL("create table if not exists "
                    + Config.WEIXIN + "(id integer primary key autoincrement,key text unique,is_read integer);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
