package com.wyz.example.mybook.adapter;

import android.content.Context;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.base.BaseArrayAdapter;
import com.wyz.example.mybook.base.BaseViewHolder;
import com.wyz.example.mybook.model.user;
import com.wyz.example.mybook.utils.BookUtils;

import java.util.List;

public class DisplayAdapter extends BaseArrayAdapter<user> {

    public DisplayAdapter(Context context, int resource, List<user> Data) {
        super(context, resource, Data);
    }

    @Override
    public void convert(BaseViewHolder holder, user item) {
        holder.setBackground(R.id.dis_icon_riv, BookUtils.convertDrawable(item.getPicture()))
                .setText(R.id.dis_name_tv,item.getName())
                .setText(R.id.dis_pwd_tv,item.getPwd())
                .setText(R.id.dis_role_tv,item.getRole());
    }
}
