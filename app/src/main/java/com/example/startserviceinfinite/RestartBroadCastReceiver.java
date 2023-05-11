package com.example.startserviceinfinite;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class RestartBroadCastReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CheckService", "Service tried to stop");
        if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            return;
        }
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, AutoStartService.class));
        } else {
            context.startService(new Intent(context, AutoStartService.class));
        }
    }
}
