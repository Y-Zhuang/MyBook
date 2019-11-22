package com.wyz.example.mybook.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.dao.BookDao;
import com.wyz.example.mybook.model.book;
import com.wyz.example.mybook.utils.BookUtils;

public class IntroduceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView InNameTv;
    private ImageView InImgIv;
    private TextView InAuthorTv;
    private TextView InIntrodTv;
    private TextView InMoneyTv;
    private Button InCollectBtn;
    private boolean isCollect;
    private book b;
    private int userid;
    private String name;
    private BookDao bookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int  id = bundle.getInt("id",-1);
        if (id == -1){
            getBundle(bundle);
        }
        else {
            getBook(id);
        }

    }

    public void initView(){
        InNameTv = findViewById(R.id.in_name_tv);
        InImgIv = findViewById(R.id.in_img_iv);
        InAuthorTv = findViewById(R.id.in_author_tv);
        InIntrodTv = findViewById(R.id.in_introd_tv);
        InCollectBtn = findViewById(R.id.in_collect_btn);
        InMoneyTv = findViewById(R.id.in_money_tv);
        InIntrodTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        bookDao = new BookDao(this);
        InCollectBtn.setOnClickListener(this);
    }

    public void getBook(int id){
        b = bookDao.getBookById(id);
        if(b != null){
            setData(b);
            InCollectBtn.setBackground(new BitmapDrawable(BookUtils.getImg(this,"ic_collect_true","mipmap")));
            isCollect = true;
        }
    }

    public void getBundle(Bundle bundle){
        userid = bundle.getInt("userid",-1);
        name = bundle.getString("bookname");
        b = bookDao.getBookByNameAndUserId(userid,name);
        if(b != null){
            setData(b);
            InCollectBtn.setBackground(new BitmapDrawable(BookUtils.getImg(this,"ic_collect_true","mipmap")));
            isCollect = true;
        }
        else {
            b = new book();
            b.setUserid(userid);
            b.setBookname(name);
            b.setBookauthor(bundle.getString("bookauthor"));
            b.setBookintroduce(bundle.getString("bookintroduce"));
            b.setBookimage(bundle.getByteArray("bookimage"));
            b.setMoney(bundle.getString("money"));
            setData(b);
            InCollectBtn.setBackground(new BitmapDrawable(BookUtils.getImg(this,"ic_collect_false","mipmap")));
            isCollect = false;
        }

    }

    public void setData(book b){
        InNameTv.setText(b.getBookname());
        InImgIv.setImageDrawable(BookUtils.convertDrawable(b.getBookimage()));
        InAuthorTv.setText(b.getBookauthor());
        InIntrodTv.setText(b.getBookintroduce());
        InMoneyTv.setText(b.getMoney());
    }

    @Override
    public void onClick(View v) {
        if(isCollect){
            book b1 = bookDao.getBookById(b.getId());
            if(b1 != null){
                deleteBook(b1.getId());
            }
            else {
                b1 = bookDao.getBookByNameAndUserId(b.getUserid(),b.getBookname());
                if(b1 != null){
                    deleteBook(b1.getId());
                }
                else {
                    Toast.makeText(this,"取消失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            if(bookDao.addBook(b)){
                Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                InCollectBtn.setBackground(new BitmapDrawable(BookUtils.getImg(this,"ic_collect_true","mipmap")));
                isCollect = true;
            }
            else {
                Toast.makeText(this,"收藏失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteBook(int id){
        if(bookDao.deleteBookByid(id)){
            InCollectBtn.setBackground(new BitmapDrawable(BookUtils.getImg(this,"ic_collect_false","mipmap")));
            Toast.makeText(this,"取消收藏",Toast.LENGTH_SHORT).show();
            isCollect = false;
        }
        else {
            Toast.makeText(this,"取消失败",Toast.LENGTH_SHORT).show();
        }
    }

}
