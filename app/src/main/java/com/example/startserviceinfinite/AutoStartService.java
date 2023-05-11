package com.example.startserviceinfinite;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class AutoStartService extends Service {


    private static final String TAG = "CheckService";
    public int counter = 0;
    private Timer timer;
    private TimerTask timerTask;
    public AutoStartService() {
    }

    public AutoStartService(int counter) {
        Log.d(TAG, "AutoStartService: Here we go.....");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            startMyOwnForeground();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    public void startTimer() {
        timer = new Timer();
        initialiseTimerTask();
        timer.schedule(timerTask, 1000, 1000);
    }

    public void initialiseTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Timer is running " + counter++);
            }
        };
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        try {
            Intent contentIntent = new Intent(AutoStartService.this, MainActivity.class);
            String body = "Notification service running...";
            String packageName = getApplicationContext().getPackageName();
            NotificationChannel notificationChannel = new NotificationChannel(packageName, "Service is running", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.setLockscreenVisibility(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                notificationChannel.setAllowBubbles(true);
            }
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
            NotificationCompat.Builder smallIcon = new NotificationCompat.Builder(this, packageName).setSmallIcon(R.mipmap.ic_launcher);
            String title = getResources().getString(R.string.app_name);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntent(contentIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_UPDATE_CURRENT |
                            PendingIntent.FLAG_IMMUTABLE);
            smallIcon.setContentIntent(resultPendingIntent);
            startForeground(3, smallIcon.setContentTitle(title).setContentText(body).setPriority(Notification.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_EVENT).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}