package com.example.noti;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class ServiceNotification extends Service {
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("abc","start");

        Calendar calendar=Calendar.getInstance();

        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);

        intent=new Intent(getBaseContext(),AlarmReceiver.class);
        intent.setAction("Notification");
        sendBroadcast(intent);

        pendingIntent= PendingIntent.getBroadcast(getBaseContext(),10,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
