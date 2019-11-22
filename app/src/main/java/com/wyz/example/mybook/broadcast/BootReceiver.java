package com.wyz.example.mybook.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.wyz.example.mybook.activity.MainActivity;

public class BootReceiver extends BroadcastReceiver {

    private static String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    public BootReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(BOOT_ACTION)){
            Intent boot = new Intent(context, MainActivity.class);
            boot.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(boot);
        }
    }
}
