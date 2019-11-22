package com.wyz.example.mybook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.activity.IntroduceActivity;
import com.wyz.example.mybook.base.BaseViewAdapter;
import com.wyz.example.mybook.base.BaseViewHolder;
import com.wyz.example.mybook.dao.BookDao;
import com.wyz.example.mybook.model.book;
import com.wyz.example.mybook.utils.BookUtils;

import java.util.List;

public class CollectAdapter extends BaseViewAdapter<book> implements BaseViewHolder.ItemClickLongClickListener {

    private Context context;
    private BookDao bookDao;

    public CollectAdapter(Context context, List<book> data, int layoutId, BookDao bookDao) {
        super(context, data, layoutId);
        this.context = context;
        this.bookDao = bookDao;
    }

    @Override
    public void convert(BaseViewHolder holder, book item) {
        holder.setId(item.getId())
                .setBackground(R.id.book_img_iv, BookUtils.convertDrawable(item.getBookimage()))
                .setText(R.id.book_name_tv,item.getBookname())
                .setText(R.id.book_author_tv,item.getBookauthor())
                .setText(R.id.money_tv,item.getMoney())
                .setOnClickLongClickListener(R.id.delete_btn,"Click")
                .setItemClickListener()
                .setItemClickLongClickListener(this);
    }

    @Override
    public void ClickItem(View view, BaseViewHolder baseViewHolder) {
        if (view.getId() == R.id.delete_btn){
            if(bookDao.deleteBookByid(baseViewHolder.getId())){
                baseViewHolder.setDeleteItemAnimator();
                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Intent intent = new Intent(context, IntroduceActivity.class);
            intent.putExtra("id",baseViewHolder.getId());
            context.startActivity(intent);
        }
    }

    @Override
    public void LongClickItem(View view, BaseViewHolder baseViewHolder) {

    }
}
