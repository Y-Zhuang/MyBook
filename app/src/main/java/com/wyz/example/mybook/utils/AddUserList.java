package com.wyz.example.mybook.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;
import com.wyz.example.mybook.dao.UserDao;
import com.wyz.example.mybook.model.user;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddUserList {

    private Context context;
    private String url;
    private UserDao userDao;
    private List<user> list = new ArrayList<>();

    public AddUserList(Context context, String url, UserDao userDao){
        this.context = context;
        this.url = url;
        this.userDao = userDao;
         getUser();

    }

    public void getUser(){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, final Response response) {
                    if(response.isSuccessful()){
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    processJsonData(response.body().string());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(context,"链接失效",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void processJsonData(String jaon){
        try {
            JSONObject jsonObject = new JSONObject(jaon);
            JSONArray jsonArray = jsonObject.getJSONArray("user");
            for (int i = 0 ; i < jsonArray.length() ; i++){
                user u = new user();
                JSONObject object = jsonArray.getJSONObject(i);
                u.setName(object.getString("name"));
                u.setPwd(object.getString("pwd"));
                u.setRole(object.getString("role"));
                u.setPicture(BookUtils.convertByte(new BitmapDrawable(BookUtils.getImg(context,"ic_picture","drawable"))));
                list.add(u);
            }
            addUserList(list);
        }catch (Exception e){
            Toast.makeText(context,"获取失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void addUserList(List<user> list){
        if(userDao.addUserList(list)){
            Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"添加失败",Toast.LENGTH_SHORT).show();
        }
    }

}
