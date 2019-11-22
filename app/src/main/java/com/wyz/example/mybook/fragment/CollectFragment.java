package com.wyz.example.mybook.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.adapter.CollectAdapter;
import com.wyz.example.mybook.dao.BookDao;
import com.wyz.example.mybook.model.book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectFragment extends Fragment {

    private RecyclerView CollectRv;
    private BookDao bookDao;
    private int usetId;
    private List<book> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collect,container,false);
        initView(rootView);
        initData();
        setAdapter();
        return rootView;
    }

    public void initView(View rootView){
        bookDao = new BookDao(getContext());
        CollectRv = (RecyclerView)rootView.findViewById(R.id.collect_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        CollectRv.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
    }

    public void initData(){
        usetId = getArguments().getInt("id",-1);
        if(usetId != -1){
            data = bookDao.getBookByUserId(usetId);
            Collections.reverse(data);
        }
    }

    public void setAdapter(){
        CollectRv.setAdapter(new CollectAdapter(getContext(),data,R.layout.layout_collect_item,bookDao));
    }

}
