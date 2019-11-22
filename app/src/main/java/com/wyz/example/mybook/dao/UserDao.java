package com.wyz.example.mybook.dao;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import com.wyz.example.mybook.db.SQLiteDBMessage;
import com.wyz.example.mybook.model.user;
import com.wyz.example.mybook.utils.BookUtils;

import java.util.List;

public class UserDao {

    private SQLiteDBMessage<user> message;

    public UserDao(Context context){
        message = new SQLiteDBMessage(context, user.class);
    }

    public boolean Reg(user u){
        int i = (int)message.insert(u);
        if(i <= 0){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean Login(String name, String pwd, String role){
        user u = message.querrySingle("name = ?", new String[] {name});
        if(u != null){
            if(u.getPwd().equals(pwd)){
                if(u.getRole().equals(role)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNameSame(String name){
        if(message.querrySingle("name = ?", new String[] {name}) != null){
            return true;
        }
        return false;
    }

    public BitmapDrawable getUserPictureByName(String name){
        user u = message.querrySingle("name = ?", new String[] {name});
        if(u != null){
            return BookUtils.convertDrawable(u.getPicture());
        }
        return null;
    }

    public user getUserById(int id){
        user u = message.querrySingle("id = ?", new String[] {String.valueOf(id)});
        if(u != null){
            return u;
        }
        return null;
    }

    public user getUserByName(String name){
        user u = message.querrySingle("name = ?", new String[] {name});
        if(u != null){
            return u;
        }
        return null;
    }

    public boolean updateUser(user u){
        int i = (int)message.update(u,"id = ?", new String[] {String.valueOf(u.getId())});
        if(i <= 0){
            return false;
        }
        else {
            return true;
        }
    }

    public List<user> getUserAll(){
        return message.querry();
    }

    public boolean addUserList(List<user> list){
        int i = (int)message.insert(list);
        if(i <= 0){
            return false;
        }
        else {
            return true;
        }
    }

}
