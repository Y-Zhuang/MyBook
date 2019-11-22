package com.wyz.example.mybook.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import com.wyz.example.mybook.R;
import com.wyz.example.mybook.adapter.ShelfAdapter;
import com.wyz.example.mybook.model.book;
import com.wyz.example.mybook.utils.JsoupRequestBook;

import java.util.ArrayList;
import java.util.List;

public class ShelfFragment extends Fragment implements JsoupRequestBook.OnAddDataSuccessListener, SwipeRefreshLayout.OnRefreshListener {

    private GridView ShelfRv;
    private int usetId;
    private boolean isError;
    private SwipeRefreshLayout ShelfSrl;
    public static List<book> data = new ArrayList<>();
    private JsoupRequestBook jsoupRequestBook;
    private static final String URL = "http://category.dangdang.com/pg-cp01.54.00.00.00.00.html";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shelf,container,false);
        initView(rootView);
        initData();
        return rootView;
    }

    public void initView(View rootView){
        ShelfRv = (GridView)rootView.findViewById(R.id.shelf_rv);
        ShelfSrl = (SwipeRefreshLayout)rootView.findViewById(R.id.shelf_srl);
        ShelfSrl.setColorSchemeColors(Color.RED,Color.BLUE, Color.GREEN);
    }

    public void initData(){
        usetId = getArguments().getInt("id", -1);
        jsoupRequestBook = new JsoupRequestBook(getActivity(),URL);
        jsoupRequestBook.setOnAddDataSuccessListener(this);
        ShelfSrl.setOnRefreshListener(this);
        ShelfSrl.setRefreshing(true);
        isError = true;
    }

    @Override
    public void Success(List<book> list) {
        if(list != null && getActivity() != null ){
            data = list;
            ShelfRv.setAdapter(new ShelfAdapter(getActivity(),R.layout.layout_shelf_item,data,usetId));
            ShelfSrl.setRefreshing(false);
        }
        if(list == null && getActivity() != null && isError) {
            Toast.makeText(getActivity(),"请检查网络",Toast.LENGTH_SHORT).show();
            isError = false;
        }
    }

    @Override
    public void onRefresh() {
        jsoupRequestBook.initSum();
    }

}
