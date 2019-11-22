package com.wyz.example.mybook.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wyz.example.mybook.model.book;

import java.util.List;

public abstract class BaseViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<T> Data;
    private int LayoutId;

    public BaseViewAdapter(Context context, List<T> data, int layoutId){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.Data = data;
        this.LayoutId = layoutId;
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(LayoutId,viewGroup,false);
        return new BaseViewHolder(context,view,viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        convert(baseViewHolder,Data.get(i));
    }

    public abstract void convert(BaseViewHolder holder,T item);

    @Override
    public int getItemCount() {
        return Data.size();
    }

}
