package com.wyz.example.mybook.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;


public class ChangeUserPictures {

    private Fragment fragment;
    public static final int ALBUM_REQUEST_CODE = 1;
    public static final int CROP_REQUEST_CODE = 2;

    public ChangeUserPictures(Fragment fragment){
        this.fragment = fragment;
    }

    public void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        fragment.startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    public void cropPhoto(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        fragment.startActivityForResult(intent, CROP_REQUEST_CODE);
    }

}
