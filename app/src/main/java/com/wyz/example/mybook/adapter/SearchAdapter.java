package com.wyz.example.mybook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.activity.BookActivity;
import com.wyz.example.mybook.activity.IntroduceActivity;
import com.wyz.example.mybook.base.BaseArrayAdapter;
import com.wyz.example.mybook.base.BaseViewHolder;
import com.wyz.example.mybook.model.book;
import com.wyz.example.mybook.utils.BookUtils;

import java.util.List;

public class SearchAdapter extends BaseArrayAdapter<book> implements BaseViewHolder.ItemClickLongClickListener {

    private Context context;
    private int userId;

    public SearchAdapter(Context context, int resource, List<book> Data, int userId) {
        super(context, resource, Data);
        this.context = context;
        this.userId = userId;
    }

    @Override
    public void convert(BaseViewHolder holder, book item) {
        holder.setBackground(R.id.sea_img_iv, BookUtils.convertDrawable(item.getBookimage()))
                .setText(R.id.sea_name_tv,item.getBookname())
                .setText(R.id.sea_author_tv,item.getBookauthor())
                .setText(R.id.sea_money_tv,item.getMoney())
                .setBookIntroduce(item.getBookintroduce())
                .setItemClickListener()
                .setItemClickLongClickListener(this);
    }

    @Override
    public void ClickItem(View view, BaseViewHolder baseViewHolder) {
        Intent intent = new Intent(context, IntroduceActivity.class);
        intent.putExtras(intent.putExtras(BookActivity.setBookBundle(userId,baseViewHolder.getText(R.id.sea_name_tv),baseViewHolder.getText(R.id.sea_author_tv),baseViewHolder.getBookIntroduce(),baseViewHolder.getText(R.id.sea_money_tv),baseViewHolder.getBackground(R.id.sea_img_iv))));
        context.startActivity(intent);
    }

    @Override
    public void LongClickItem(View view, BaseViewHolder baseViewHolder) {

    }
}
