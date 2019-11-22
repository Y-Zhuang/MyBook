package com.wyz.example.mybook.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import com.makeramen.roundedimageview.RoundedDrawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BookUtils {

    public static byte[] convertByte(Drawable drawable) {
        if(drawable == null) {
            return null;
        }
        Bitmap bitmap = null;
        if(drawable.getClass() == RoundedDrawable.class){
            RoundedDrawable rd = (RoundedDrawable)drawable;
            bitmap = rd.toBitmap();
        }
        else {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bitmap = bd.getBitmap();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public static BitmapDrawable convertDrawable(byte[] b){
        if(b == null) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
        return new BitmapDrawable(bitmap);
    }

    public static String convertString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        return new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
    }

    public static Bitmap convertImage(String img){
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(img.getBytes(), Base64.DEFAULT));
        return BitmapFactory.decodeStream(bais);
    }

    public static Bitmap getImg(Context context,String name,String packageName) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        int resID = context.getResources().getIdentifier(name,packageName,appInfo.packageName);
        return BitmapFactory.decodeResource(context.getResources(), resID);
    }

}
