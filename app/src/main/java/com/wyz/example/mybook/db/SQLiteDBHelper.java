package com.wyz.example.mybook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private String createUser = "create table user(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,pwd TEXT,role TEXT,picture BLOB)";
    private String createBook = "create table book(id INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER,bookname TEXT,bookauthor TEXT,bookintroduce TEXT,money TEXT,bookimage BLOB)";

    public SQLiteDBHelper(Context context, int version){
        super(context,"mybook.db",null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUser);
        db.execSQL(createBook);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("数据库更新");
    }
}
