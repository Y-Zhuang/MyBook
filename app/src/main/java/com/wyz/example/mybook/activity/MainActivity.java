package com.wyz.example.mybook.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView PhoneTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login_reg_fl, new LoginFragment())
                .commit();
        }
        initView();
        setListener();
    }

    public void initView(){
        PhoneTv = findViewById(R.id.phone_tv);
    }

    public void setListener(){
        PhoneTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phone_tv:
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + PhoneTv.getText().subSequence(6,PhoneTv.getText().length())));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
