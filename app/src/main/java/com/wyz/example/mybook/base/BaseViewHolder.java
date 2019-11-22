package com.wyz.example.mybook.base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, ValueAnimator.AnimatorUpdateListener {

    private SparseArray<View> views;
    private Context context;
    private int Id;
    private String BooAauthor;
    private String BookIntroduce;
    private int height;
    private View convertView;
    private ItemClickLongClickListener itemClickLongClickListener;

    public BaseViewHolder(Context context, @NonNull View itemView, ViewGroup viewGroup) {
        super(itemView);
        this.context = context;
        this.views = new SparseArray<View>();
        convertView = itemView;
    }

    public <T extends View> T retrieveView(int viewId){
        View view = views.get(viewId);
        if(view == null){
            view = convertView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T) view;
    }

    public <T extends TextView> BaseViewHolder setText(int viewId, CharSequence valeue){
        T t = retrieveView(viewId);
        t.setText(valeue);
        return this;
    }

    public <T extends TextView> String getText(int viewId){
        T t = retrieveView(viewId);
        return t.getText().toString();
    }

    public BaseViewHolder setOnClickLongClickListener(int viewId, String name){
        Button button = retrieveView(viewId);
        button.setOnClickListener(this);
        if(name.equals("LongClick")){
            button.setOnLongClickListener(this);
        }
        return this;
    }

    public BaseViewHolder setItemClickListener(){
        convertView.setOnClickListener(this);
        return this;
    }

    public <T extends View> BaseViewHolder setBackground(int viewId, BitmapDrawable bitmapDrawable){
        ((T)retrieveView(viewId)).setBackground(bitmapDrawable);
        return this;
    }

    public <T extends View> Drawable getBackground(int viewId){
        return ((T)retrieveView(viewId)).getBackground();
    }

    @Override
    public void onClick(View v) {
        if(itemClickLongClickListener != null){
            itemClickLongClickListener.ClickItem(v,this);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(itemClickLongClickListener != null){
            itemClickLongClickListener.LongClickItem(v,this);
        }
        return true;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation.getAnimatedFraction() >= 1) {
            convertView.setVisibility(View.GONE);
        }
        else {
            convertView.getLayoutParams().height = height - (int) (height * animation.getAnimatedFraction());
            convertView.requestLayout();
        }
    }

    public interface ItemClickLongClickListener {
        void ClickItem(View view, BaseViewHolder baseViewHolder);
        void LongClickItem(View view, BaseViewHolder baseViewHolder);
    }

    public void setItemClickLongClickListener(ItemClickLongClickListener listener){
        this.itemClickLongClickListener = listener;
    }

    public BaseViewHolder setId(int id){
        this.Id = id;
        return this;
    }

    public int getId(){
        return this.Id;
    }

    public String getBooAauthor() {
        return BooAauthor;
    }

    public BaseViewHolder setBooAauthor(String booAauthor) {
        this.BooAauthor = booAauthor;
        return this;
    }

    public String getBookIntroduce() {
        return BookIntroduce;
    }

    public BaseViewHolder setBookIntroduce(String bookIntroduce) {
        this.BookIntroduce = bookIntroduce;
        return this;
    }

    public BaseViewHolder setDeleteItemAnimator(){
        AnimatorSet animatorSet = new AnimatorSet();
        Animator anima = ObjectAnimator.ofFloat(convertView, "rotationX", 0, 90);
        Animator animb = ObjectAnimator.ofFloat(convertView, "alpha", 1, 0);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        height = convertView.getMeasuredHeight();
        valueAnimator.addUpdateListener(this);
        anima.setDuration(300);
        animb.setDuration(300);
        valueAnimator.setDuration(300 + 300 + 100);
        animatorSet.playTogether(anima, animb, valueAnimator);
        animatorSet.start();
        return this;
    }

}
