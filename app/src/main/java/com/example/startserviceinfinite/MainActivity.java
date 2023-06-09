package com.example.startserviceinfinite;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Intent mServiceIntent;
    private AutoStartService mAutoStartService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoStartService = new AutoStartService(1);
        mServiceIntent = new Intent(this, AutoStartService.class);
        if (!isMyServiceRunning(AutoStartService.class)) {
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.d("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}