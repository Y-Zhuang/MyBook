package com.wyz.example.mybook.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.adapter.SearchAdapter;
import com.wyz.example.mybook.dao.BookDao;
import com.wyz.example.mybook.dao.UserDao;
import com.wyz.example.mybook.fragment.CollectFragment;
import com.wyz.example.mybook.fragment.MyFragnemt;
import com.wyz.example.mybook.fragment.ShelfFragment;
import com.wyz.example.mybook.model.book;
import com.wyz.example.mybook.utils.BookUtils;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements View.OnClickListener, Animator.AnimatorListener {

    private String UserName;
    private TextView TitleTv;
    private EditText SearchEv;
    private Button ShelfBtn;
    private Button CollectBtn;
    private Button MyBtn;
    private Button SearchBtn;
    private Bundle bundle;
    private BookDao bookDao;
    private int id;
    private Dialog dialog;
    private boolean isInvisible;
    private long firstTime = 0;
    private List<book> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        initView();
        initListener();
        UserName = getIntent().getStringExtra("name");
        TitleTv.setText(this.getString(R.string.shelf_text));
        initFragment();
    }

    public void initView(){
        TitleTv = (TextView) findViewById(R.id.title_tv);
        SearchEv = (EditText) findViewById(R.id.search_ev);
        SearchBtn = (Button) findViewById(R.id.search_btn);
        ShelfBtn = (Button) findViewById(R.id.shelf_btn);
        CollectBtn = (Button) findViewById(R.id.collect_btn);
        MyBtn = (Button) findViewById(R.id.my_btn);
        bookDao = new BookDao(this);
        isInvisible = false;
    }

    public void initListener(){
        ShelfBtn.setOnClickListener(this);
        CollectBtn.setOnClickListener(this);
        MyBtn.setOnClickListener(this);
        SearchBtn.setOnClickListener(this);
    }

    public void initFragment(){
        id = new UserDao(this).getUserByName(UserName).getId();
        bundle = new Bundle();
        bundle.putInt("id",id);
        switchFragment(new ShelfFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shelf_btn:
                TitleTv.setText(this.getString(R.string.shelf_text));
                switchFragment(new ShelfFragment());
                break;
            case R.id.collect_btn:
                TitleTv.setText(this.getString(R.string.collect_text));
                switchFragment(new CollectFragment());
                break;
            case R.id.my_btn:
                TitleTv.setText(this.getString(R.string.my_text));
                switchFragment(new MyFragnemt());
                break;
            case R.id.search_btn:
                if(SearchEv.getVisibility() == View.INVISIBLE){
                    SearchEv.setVisibility(View.VISIBLE);
                    setSuccessAnimation(0,1);
                }
                else {
                    if(!SearchEv.getText().toString().trim().equals("")){
                        SearchData(SearchEv.getText().toString());
                    }
                    else {
                        Toast.makeText(this,"搜索内容不可为空",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void switchFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        List<Fragment> childFragments = getSupportFragmentManager().getFragments();
        for (Fragment childFragment : childFragments) {
            fragmentTransaction.hide(childFragment);
        }
        if(!childFragments.contains(fragment)){
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.book_fl,fragment);
        }else{
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
    }

    public void SearchData(String key){
        List<book> l;
        list = bookDao.searchBook(id,key);
        l = processBookData(key);
        if(l.size() != 0){
            list.addAll(l);
        }
        if(list.size() != 0){
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.layout_display_dialog, null);
            ListView DialogLv = (ListView) view.findViewById(R.id.dialog_lv);
            DialogLv.setAdapter(new SearchAdapter(this,R.layout.layout_search_item,list,id));
            dialog = BookActivity.createUserDialog(this,view);
            dialog.show();
        }
        else {
            Toast.makeText(this,"没有与“" + key + "”相关的数据",Toast.LENGTH_SHORT).show();
        }
    }

    public void setSuccessAnimation(float valueStart, float valueEnd){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(SearchEv,"scaleX",valueStart,valueEnd);
        SearchEv.setPivotX(SearchEv.getMeasuredWidth());
        scaleX.addListener(this);
        scaleX.setDuration(300);
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(SearchEv,"alpha",valueStart,valueEnd);
        alpha1.setDuration(300);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(TitleTv,"alpha",valueEnd,valueStart);
        alpha2.setDuration(300);
        scaleX.start();
        alpha1.start();
        alpha2.start();
    }

    public static Dialog createUserDialog(Context context, View view) {
        Dialog dialog = new Dialog(context,R.style.loading_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        return dialog;
    }

    public List<book> processBookData(String key){
        List<book> l = new ArrayList<>();
        if(ShelfFragment.data.size() != 0){
            for(book b : ShelfFragment.data) {
                if(b.getBookname().toLowerCase().indexOf(key.toLowerCase()) != -1){
                    if(list.indexOf(b) == -1){
                        l.add(b);
                    }
                }
                else if(b.getBookauthor().toLowerCase().indexOf(key.toLowerCase()) != -1){
                    if(list.indexOf(b) == -1){
                        l.add(b);
                    }
                }
            }
        }
        return l;
    }

    public static Bundle setBookBundle(int UserId, String BookName, String BookAuthor, String BookIntroduce,String money, Drawable BookImage){
        Bundle bundle = new Bundle();
        bundle.putInt("userid",UserId);
        bundle.putString("bookname",BookName);
        bundle.putString("bookauthor",BookAuthor);
        bundle.putString("bookintroduce",BookIntroduce);
        bundle.putString("money",money);
        bundle.putByteArray("bookimage", BookUtils.convertByte(BookImage));
        return bundle;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && (System.currentTimeMillis() - firstTime) > 300){
            if(SearchEv.getVisibility() == View.VISIBLE){
                setSuccessAnimation(1,0);
                isInvisible = true;
            }
            else {
                finish();
            }
            firstTime = System.currentTimeMillis();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(isInvisible){
            SearchEv.setVisibility(View.INVISIBLE);
            isInvisible = false;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

}
