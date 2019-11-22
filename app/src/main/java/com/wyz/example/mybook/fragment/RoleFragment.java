package com.wyz.example.mybook.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.dao.UserDao;
import com.wyz.example.mybook.model.user;

public class RoleFragment extends Fragment implements View.OnClickListener {

    private String Name;
    private String Pwd;
    private byte[] Icon;
    private Button RoleStudentBtn;
    private Button RoleTeacherBtn;
    private user u = new user();

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_role,container,false);
        initView(rootView);
        initListener();
        setData();
        return rootView;
    }

    public void initView(View rootView){
        RoleStudentBtn = rootView.findViewById(R.id.role_student_btn);
        RoleTeacherBtn = rootView.findViewById(R.id.role_teacher_btn);
    }

    public void initListener(){
        RoleStudentBtn.setOnClickListener(this);
        RoleTeacherBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.role_student_btn:
                setUser(getContext().getString(R.string.role_student_btn_text).replace(" ", ""));
                Login(u);
                break;
            case R.id.role_teacher_btn:
                setUser(getContext().getString(R.string.role_teacher_btn_text).replace(" ", ""));
                Login(u);
                break;
            default:
                break;
        }
    }

    public void setData() {
        Name = getArguments().getString("name");
        Pwd = getArguments().getString("pwd");
        Icon = getArguments().getByteArray("icon");
    }

    public void Login(user u){
        UserDao userDao = new UserDao(getActivity());
        if(userDao.Reg(u)){
            Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack(new RegFragment().getId(),1);
        }
        else {
            Toast.makeText(getActivity(),"注册失败",Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
        }
    }

    public void setUser(String role){
        u.setName(Name);
        u.setPwd(Pwd);
        u.setRole(role);
        u.setPicture(Icon);
    }

}
