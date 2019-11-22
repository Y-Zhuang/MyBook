package com.wyz.example.mybook.dao;

import android.content.Context;
import com.wyz.example.mybook.db.SQLiteDBMessage;
import com.wyz.example.mybook.model.book;

import java.util.List;

public class BookDao {

    private SQLiteDBMessage<book> message;

    public BookDao(Context context){
        message = new SQLiteDBMessage(context,book.class);
    }

    public List<book> getBookByUserId(int id){
        return message.querry("userid = ?", new String[] {String.valueOf(id)});
    }

    public book getBookById(int id){
        return message.querrySingle("id = ?", new String[] {String.valueOf(id)});
    }

    public book getBookByNameAndUserId(int id, String name){
        return message.querrySingle("userid = ? and bookname = ?", new String[]{String.valueOf(id),name});
    }

    public boolean deleteBookByid(int id){
        int i = (int)message.delete("id = ?", new String[] {String.valueOf(id)});
        if(i <= 0){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addBook(book b){
        int i = (int)message.insert(b);
        if(i <= 0){
            return false;
        }
        else {
            return true;
        }
    }

    public List<book> searchBook(int userId, String key){
        return message.querry("userid = ? and (bookname like ? or bookauthor like ?)",new String[]{String.valueOf(userId),"%" + key + "%","%" + key + "%"});
    }

}
