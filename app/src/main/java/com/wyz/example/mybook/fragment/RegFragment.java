package com.wyz.example.mybook.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.dao.UserDao;
import com.wyz.example.mybook.utils.BookUtils;
import com.wyz.example.mybook.utils.ChangeUserPictures;

import static android.app.Activity.RESULT_OK;

public class RegFragment extends Fragment implements View.OnClickListener{

    private RoundedImageView RegIconRiv;
    private EditText RegNameEv;
    private EditText RegPwdEv;
    private EditText RegPwd2Ev;
    private Button ClearBtn;
    private Button NextBtn;
    private TextView LoginTv;
    private ChangeUserPictures changeUserPictures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reg,container,false);
        initView(rootView);
        initListener();
        return rootView;
    }

    public void initView(View rootView){
        RegIconRiv = rootView.findViewById(R.id.reg_icon_riv);
        RegNameEv = rootView.findViewById(R.id.reg_name_ev);
        RegPwdEv = rootView.findViewById(R.id.reg_pwd_ev);
        RegPwd2Ev = rootView.findViewById(R.id.reg_pwd2_ev);
        ClearBtn = rootView.findViewById(R.id.clear_btn);
        NextBtn =  rootView.findViewById(R.id.next_btn);
        LoginTv = rootView.findViewById(R.id.login_tv);
        changeUserPictures = new ChangeUserPictures(this);
    }

    public void initListener(){
        RegIconRiv.setOnClickListener(this);
        LoginTv.setOnClickListener(this);
        ClearBtn.setOnClickListener(this);
        NextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_tv:
                getFragmentManager()
                    .popBackStack();
                break;
            case R.id.clear_btn:
                clearText();
                break;
            case R.id.next_btn:
                if(isNamePwdNull(RegNameEv.getText().toString(),RegPwdEv.getText().toString(),RegPwd2Ev.getText().toString())){
                    if(RegPwdEv.getText().toString().equals(RegPwd2Ev.getText().toString())){
                        if(!new UserDao(getActivity()).isNameSame(RegNameEv.getText().toString())){
                            RoleFragment roleFragment = new RoleFragment();
                            RegIconRiv.setOval(true);
                            Bundle bundle = new Bundle();
                            bundle.putString("name",RegNameEv.getText().toString());
                            bundle.putString("pwd",RegPwdEv.getText().toString());
                            bundle.putByteArray("icon", BookUtils.convertByte(RegIconRiv.getDrawable()));
                            roleFragment.setArguments(bundle);
                            getFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out,R.anim.slide_left_in,R.anim.slide_right_out)
                                    .addToBackStack(null)
                                    .replace(R.id.login_reg_fl, roleFragment).commit();
                        }
                        else {
                            Toast.makeText(getActivity(),"该用户名已被使用，请修改",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(),"密码不一致",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.reg_icon_riv:
                changeUserPictures.getPicFromAlbm();
                break;
            default:
                break;
        }
    }

    public void clearText(){
        RegNameEv.setText(null);
        RegPwdEv.setText(null);
        RegPwd2Ev.setText(null);
    }

    public boolean isNamePwdNull(String name, String pwd, String pwd2){
        if(!name.equals("")){
            if(!pwd.equals("")){
                if(!pwd2.equals("")){
                    return true;
                }
                else {
                    Toast.makeText(getActivity(),"请确认密码",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getActivity(),"密码不可为空",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getActivity(),"用户名不可为空",Toast.LENGTH_SHORT).show();
        }
        return false;
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
                    RegIconRiv.setImageBitmap(image);
                }
                break;
            default:
                break;
        }
    }

}
