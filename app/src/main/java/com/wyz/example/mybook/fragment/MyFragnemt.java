package com.wyz.example.mybook.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.activity.BookActivity;
import com.wyz.example.mybook.adapter.DisplayAdapter;
import com.wyz.example.mybook.dao.UserDao;
import com.wyz.example.mybook.model.user;
import com.wyz.example.mybook.utils.AddUserList;
import com.wyz.example.mybook.utils.BookUtils;
import com.wyz.example.mybook.utils.ChangeUserPictures;

import static android.app.Activity.RESULT_OK;

public class MyFragnemt extends Fragment implements View.OnClickListener {

    private RoundedImageView MyIconRiv;
    private TextView MyNameTv;
    private Button MyDisplayBtn;
    private Button MyImportBtn;
    private EditText ImpUrlEt;
    private Button ImpAddBtn;
    private Button ImpCancelBtn;
    private int usetId;
    private user u;
    private View view;
    private LayoutInflater inflater;
    private Dialog dialog;
    private UserDao userDao;
    private ChangeUserPictures changeUserPictures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my,container,false);
        initView(rootView);
        initListener();
        initData();
        return rootView;
    }

    public void initView(View rootView){
        userDao = new UserDao(getContext());
        MyIconRiv = rootView.findViewById(R.id.my_icon_riv);
        MyNameTv = rootView.findViewById(R.id.my_name_tv);
        MyDisplayBtn = rootView.findViewById(R.id.my_display_btn);
        MyImportBtn = rootView.findViewById(R.id.my_import_btn);
        changeUserPictures = new ChangeUserPictures(this);
    }

    public void initListener(){
        MyIconRiv.setOnClickListener(this);
        MyDisplayBtn.setOnClickListener(this);
        MyImportBtn.setOnClickListener(this);
    }

    public void initData(){
        usetId = getArguments().getInt("id",-1);
        u = userDao.getUserById(usetId);
        if(u != null){
            MyIconRiv.setOval(true);
            MyIconRiv.setImageBitmap(BookUtils.convertDrawable(u.getPicture()).getBitmap());
            MyNameTv.setText(u.getName() + " " + (u.getRole().equals(getContext().getString(R.string.role_student_btn_text).replace(" ", "")) ? "同学" : "老师"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_icon_riv:
                changeUserPictures.getPicFromAlbm();
                break;
            case R.id.my_display_btn:
                inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.layout_display_dialog, null);
                ListView DialogLv = (ListView) view.findViewById(R.id.dialog_lv);
                DialogLv.setAdapter(new DisplayAdapter(getActivity(),R.layout.layout_display_item,userDao.getUserAll()));
                BookActivity.createUserDialog(getActivity(),view).show();
                break;
            case R.id.my_import_btn:
                inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.layout_import_dialog, null);
                ImpUrlEt = (EditText)view.findViewById(R.id.imp_url_et);
                ImpAddBtn = (Button)view.findViewById(R.id.imp_add_btn);
                ImpAddBtn.setOnClickListener(this);
                ImpCancelBtn = (Button)view.findViewById(R.id.imp_cancel_btn);
                ImpCancelBtn.setOnClickListener(this);
                dialog = BookActivity.createUserDialog(getActivity(),view);
                dialog.show();
                break;
            case R.id.imp_add_btn:
                if(!ImpUrlEt.getText().toString().trim().equals("")){
                    new AddUserList(getActivity(),ImpUrlEt.getText().toString(),userDao);
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(),"连接不可为空",Toast.LENGTH_SHORT).show();
                break;
            case R.id.imp_cancel_btn:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ChangeUserPictures.ALBUM_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    changeUserPictures.cropPhoto(uri);
                }
                break;
            case ChangeUserPictures.CROP_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bitmap image = data.getExtras().getParcelable("data");
                    MyIconRiv.setOval(true);
                    MyIconRiv.setImageBitmap(image);
                    u.setPicture(BookUtils.convertByte(MyIconRiv.getDrawable()));
                    if(userDao.updateUser(u)){
                        Toast.makeText(getActivity(),"更换成功",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(),"更换失败",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }


}
