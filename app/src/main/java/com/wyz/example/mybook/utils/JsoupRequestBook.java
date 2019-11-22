package com.wyz.example.mybook.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.wyz.example.mybook.model.book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JsoupRequestBook {

    private Context context;
    private String Url;
    private int Pager;
    private int index = -1;
    private List<book> list = new ArrayList<>();
    private List<String> imgUrl = new ArrayList<>();
    private OnAddDataSuccessListener onAddDataSuccessListener;
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    public static final int GET_URl_SUCCESS = 4;

    public JsoupRequestBook(Context context, String url){
        this.context = context;
        this.Url = url;
        initSum();
    }

    public void initSum(){
        Random random = new Random();
        Pager = random.nextInt(100) + 1;
        getHtmlContent();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    list.get(index).setBookimage(BookUtils.convertByte(new BitmapDrawable((Bitmap) msg.obj)));
                    if(onAddDataSuccessListener != null && index == 19){
                        onAddDataSuccessListener.Success(list);
                    }
                    break;
                case GET_URl_SUCCESS:
                    index++;
                    book b = new book();
                    b.setBookname(msg.getData().get("bookname").toString());
                    b.setBookauthor(msg.getData().get("bookauthor").toString());
                    b.setMoney(msg.getData().get("money").toString());
                    if(msg.getData().get("bookintroduce").toString().equals("")){
                        b.setBookintroduce("暂无简介……");
                    }
                    else{
                        b.setBookintroduce(msg.getData().get("bookintroduce").toString());
                    }
                    imgUrl.add(msg.getData().get("bookimage").toString());
                    list.add(b);
                    getImage(index);
                    break;
                case NETWORK_ERROR:
                    if(onAddDataSuccessListener != null){
                        onAddDataSuccessListener.Success(null);
                    }
//                    Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    if(onAddDataSuccessListener != null){
                        onAddDataSuccessListener.Success(null);
                    }
//                    Toast.makeText(context,"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void getHtmlContent() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for(int i = 2 ; i <= 21 ; i++){
                    int item = i;
                    try {
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        Document doc = Jsoup.connect(insertStr(Url,Pager)).get();
                        Elements elements = doc.getElementsByClass("line" + item);
                        for(Element element : elements){
                            bundle.putString("bookimage",element.getElementsByTag("img").attr("data-original"));
                            bundle.putString("bookname",element.getElementsByTag("img").attr("alt"));
                            bundle.putString("bookauthor",elements.select("p.search_book_author > span > a:nth-child(1)").first().attr("title"));
                            bundle.putString("money",element.getElementsByClass("search_pre_price").text());
                            bundle.putString("bookintroduce",element.select("p.detail").text());
                        }
                        msg.what = GET_URl_SUCCESS;
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                        handler.sendEmptyMessage(NETWORK_ERROR);
                    }
                }
            }
        }.start();
    }

    public String insertStr(String string, int sum) {
        StringBuilder sb = new StringBuilder(string);
        sb.insert(31, sum);
        return sb.toString();
    }

    public synchronized void getImage(final int item) {
        //开启一个线程用于联网
        new Thread() {
            @Override
            public void run() {
                try {
                    //把传过来的路径转成URL
                    URL url = new URL(imgUrl.get(item));
                    //获取连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //使用GET方法访问网络
                    connection.setRequestMethod("GET");
                    //超时时间为10秒
                    connection.setConnectTimeout(10000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        //使用工厂把网络的输入流生产Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        //利用Message把图片发给Handler
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        inputStream.close();
                    }else {
                        //服务启发生错误
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //网络连接错误
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
    }

    public interface OnAddDataSuccessListener {
        void Success(List<book> list);
    }

    public void setOnAddDataSuccessListener(OnAddDataSuccessListener listener){
        this.onAddDataSuccessListener = listener;
    }

}
