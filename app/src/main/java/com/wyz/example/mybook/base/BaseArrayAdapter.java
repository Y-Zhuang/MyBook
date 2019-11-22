package com.wyz.example.mybook.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {

    private Context context;
    private int layoutResourceId;
    private List<T> Data;

    public BaseArrayAdapter(Context context, int resource, List<T> Data) {
        super(context, resource, Data);
        this.context = context;
        this.layoutResourceId = resource;
        this.Data = Data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder viewHolder;
        if(convertView == null){
            convertView = ((Activity)context).getLayoutInflater().inflate(layoutResourceId,null);
            viewHolder = new BaseViewHolder(context,convertView,parent);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (BaseViewHolder) convertView.getTag();
        }
        convert(viewHolder,Data.get(position));
        return convertView;
    }

    public abstract void convert(BaseViewHolder holder,T item);

    public void setData(List<T> Data) {
        this.Data = Data;
        notifyDataSetChanged();
    }

}
