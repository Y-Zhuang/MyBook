package com.wyz.example.mybook.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBMessage<T> {

    private SQLiteDBHelper dbHelper;
    private SQLiteDatabase database;
    private SQLiteDBHelperTool helperTool;
    private Class<T> clazz;
    private Cursor cursor;
    private final String TABLE_NAME;

    public SQLiteDBMessage(Context context,Class<T> clazz){
        dbHelper = new SQLiteDBHelper(context,1);
        helperTool = new SQLiteDBHelperTool<T>();
        this.clazz = clazz;
        TABLE_NAME = clazz.getSimpleName();
    }

    public static <T> SQLiteDBMessage<T> createDBMessage(Context context, Class<T> clazz) {
        return new SQLiteDBMessage<T>(context, clazz);
    }

    public long insert(T t){
        long l = 0;
        try{
            database = dbHelper.getReadableDatabase();
            database.beginTransaction();
            l = database.insert(TABLE_NAME,null,helperTool.getObjectContentValues(t));
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return l;
    }

    public long insert(List<T> list){
        long l = 0;
        try{
            database = dbHelper.getReadableDatabase();
            database.beginTransaction();
            for (T t : list) {
                l += database.insert(TABLE_NAME,null,helperTool.getObjectContentValues(t));
            }
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return l;
    }

    public long update(T t, String whereClause, String[] whereArgs){
        long l = 0;
        try{
            database = dbHelper.getReadableDatabase();
            database.beginTransaction();
            l += database.update(TABLE_NAME,helperTool.getObjectContentValues(t),whereClause,whereArgs);
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return l;
    }

    public long delete(String whereClause, String[] whereArgs){
        long l = 0;
        try{
            database = dbHelper.getReadableDatabase();
            database.beginTransaction();
            l += database.delete(TABLE_NAME,whereClause,whereArgs);
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return l;
    }

    // 查询单条数据
    public T querrySingle(String selection, String[] selectionArgs) {
        return querrySingle(selection, selectionArgs, null, null, null);
    }

    public T querrySingle(String selection, String[] selectionArgs, String orderBy) {
        return querrySingle(selection, selectionArgs, null, null, orderBy);
    }

    public T querrySingle(String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        List<T> list = querry(selection,selectionArgs,groupBy,having,orderBy);
        Log.e("Size",String.valueOf(list.size()));
        if(list != null && list.size() == 1){
            return list.get(0);
        }
        return null;
    }

    // 查询多条数据
    public List<T> querry() {
        return querry(null, null, null, null, null);
    }

    public List<T> querry(String selection, String[] selectionArgs) {
        return querry(selection, selectionArgs, null, null, null);
    }

    public List<T> querry(String selection, String[] selectionArgs, String orderBy) {
        return querry(selection, selectionArgs, null, null, orderBy);
    }

    public List<T> querry(String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        List<T> list = new ArrayList<T>();
        try {
            database = dbHelper.getReadableDatabase();
            database.beginTransaction();
            cursor = database.query(TABLE_NAME,helperTool.getAllFieldsName(clazz.newInstance()),selection,selectionArgs,groupBy,having,orderBy);
            while(cursor.moveToNext()){
                T t = clazz.newInstance();
                helperTool.setObjectData(t,cursor);
                list.add(t);
            }
            cursor.close();
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}
