package com.wyz.example.mybook.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import com.wyz.example.mybook.annotation.SqlConstraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SQLiteDBHelperTool<T> {

    public void setObjectData(T t, Cursor cursor){
        Field[] fd = t.getClass().getDeclaredFields();
        try {
            for (Field f : fd){
                f.setAccessible(true);
                String methodName = f.getType().getName();
                if(f.getType() == String.class){
                    methodName = "String";
                }
                else if(f.getType() == Bundle.class){
                    methodName = "Extras";
                }
                else if(f.getType() == byte[].class){
                    methodName = "Blob";
                }
                else {
                    methodName = methodName.substring(0,1).toUpperCase() + methodName.substring(1);
                }
                Method method = cursor.getClass().getMethod("get" + methodName,int.class);
                f.set(t,method.invoke(cursor,cursor.getColumnIndex(f.getName())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ContentValues getObjectContentValues(T t){
        ContentValues values = new ContentValues();
        Field[] fd = t.getClass().getDeclaredFields();
        try{
            for (Field f : fd){
                f.setAccessible(true);
                if(!isIdentity(f)){
                    Class clazz = f.getType();
                    if(f.getType() == int.class){
                        clazz = Integer.class;
                    }
                    Method method = values.getClass().getDeclaredMethod("put",String.class,clazz);
                    method.invoke(values,new Object[]{f.getName(),f.get(t)});
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return values;
    }

    public String[] getAllFieldsName(T t){
        Field[] fd = t.getClass().getDeclaredFields();
        String[] fieldName = new String[fd.length];
        for(int i = 0 ; i < fd.length ; i++){
            fieldName[i] = fd[i].getName();
        }
        return fieldName;
    }

    public boolean isIdentity(Field field){
        Annotation annotation = field.getAnnotation(SqlConstraint.class);
        if(annotation != null){
            if(((SqlConstraint) annotation).isIdentity()){
                return true;
            }
        }
        return false;
    }

}
