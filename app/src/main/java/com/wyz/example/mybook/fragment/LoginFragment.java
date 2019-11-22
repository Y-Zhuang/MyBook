package com.wyz.example.mybook.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.activity.BookActivity;
import com.wyz.example.mybook.dao.UserDao;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private RoundedImageView IconRiv;
    private EditText NameEv;
    private EditText PwdEv;
    private Button StudentBtn;
    private Button TeacherBtn;
    private TextView RegTv;
    private CheckBox RemPwdChk;
    private UserDao userDao;
    private SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login,container,false);
        initView(rootView);
        initListener();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(!preferences.getString("name","").equals("")){
            setUser();
        }
        return rootView;
    }

    public void initView(View rootView){
        IconRiv = rootView.findViewById(R.id.icon_riv);
        NameEv = rootView.findViewById(R.id.name_ev);
        PwdEv = rootView.findViewById(R.id.pwd_ev);
        StudentBtn = rootView.findViewById(R.id.student_btn);
        TeacherBtn =  rootView.findViewById(R.id.teacher_btn);
        RegTv = rootView.findViewById(R.id.reg_tv);
        RemPwdChk = rootView.findViewById(R.id.rem_pwd_chk);
        userDao = new UserDao(getActivity());
    }

    public void initListener(){
        RegTv.setOnClickListener(this);
        StudentBtn.setOnClickListener(this);
        TeacherBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.reg_tv:
                getFragmentManager()
                    .beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out,R.anim.slide_left_in,R.anim.slide_right_out)
                    .addToBackStack(null)
                    .replace(R.id.login_reg_fl, new RegFragment()).commit();
                break;
            case R.id.student_btn:
                if(Login(NameEv.getText().toString(),PwdEv.getText().toString(),this.getString(R.string.role_student_btn_text).replaceAll(" ", ""))){
                    intentBookActivity(NameEv.getText().toString());
                }
                break;
            case R.id.teacher_btn:
                if(Login(NameEv.getText().toString(),PwdEv.getText().toString(),this.getString(R.string.role_teacher_btn_text).replaceAll(" ", ""))){
                    intentBookActivity(NameEv.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    public Boolean Login(String name, String pwd, String role){
        if(name.equals("") && pwd.equals("")){
            Toast.makeText(getActivity(),"用户名或密码不可为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if(userDao.Login(name,pwd,role)){
                SharedPreferences.Editor editor = preferences.edit();
                if(RemPwdChk.isChecked()){
                    editor.putString("name",name);
                    editor.putString("pwd",pwd);
                }
                else {
                    editor.clear();
                }
                editor.apply();
                Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
                return true;
            }
            else {
                Toast.makeText(getActivity(),"用户名密码或身份不符",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public void intentBookActivity(String name){
        Intent intent = new Intent(getActivity(), BookActivity.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }

    public void setUser(){
        RemPwdChk.setChecked(true);
        NameEv.setText(preferences.getString("name",""));
        PwdEv.setText(preferences.getString("pwd",""));
        Bitmap bitmap = userDao.getUserPictureByName(NameEv.getText().toString()).getBitmap();
        if(bitmap != null){
            IconRiv.setImageBitmap(bitmap);
        }
    }

}
