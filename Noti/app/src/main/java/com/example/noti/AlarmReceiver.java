package com.example.noti;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.noti.location.GPSLocation;
import com.example.noti.model.current.Current;
import com.example.noti.network.APIManager;
import com.example.noti.network.Define;
import com.example.noti.network.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlarmReceiver extends BroadcastReceiver {
    public static final String id = "mynotification";
    public static final String name = "mynotification";
    double lon, lat;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //getNotification(context,"this is title","this is text");
        if (!checkRequiredPermissions(context)) checkRequiredPermissions(context);
        final GPSLocation gpsLocation = new GPSLocation(context);
        if (gpsLocation.canGetLocation) {
            gpsLocation.getLocation();
            lon = gpsLocation.getLongitude();
            lat = gpsLocation.getLatitude();

            String url = Define.weather + Define.lat + lat + Define.lon + lon + Define.lang + "vi" + Define.units + Define.appid;
            //Log.e("abc", url);
            APIManager service = RetrofitClient.getRetrofitClient(Define.base_url).create(APIManager.class);
            Call<Current> call = service.getAPICurrent(url);
            call.enqueue(new Callback<Current>() {
                @Override
                public void onResponse(Call<Current> call, Response<Current> response) {
                    Current current = response.body();

                    String title = ((int) (current.getMain().getTemp() + 0.5)) + "° " + current.getName() + ", " + current.getSys().getCountry();

                    String desc = current.getWeather().get(0).getDescription();
                    String status = desc.substring(0, 1).toUpperCase() + desc.substring(1);
                    String text = "Cảm giác như " + ((int) (current.getMain().getFeelsLike() + 0.5)) + "° " + status;
//                    Log.e("abc", title);
//                    Log.e("abc", text);

                    getNotification(context, title, text);
                }

                @Override
                public void onFailure(Call<Current> call, Throwable t) {
                    Log.e("kq", t.getMessage() + "");
                }

            });
        }
    }

    private void getNotification(Context context, String ti, String te) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id)
                .setContentTitle(ti)
                .setSmallIcon(R.drawable.rainclouds)
                .setAutoCancel(true)
                .setContentText(te);
        PendingIntent intent=PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(999, builder.build());
    }

    private boolean checkRequiredPermissions(Context context) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.WAKE_LOCK};
        if (!EasyPermissions.hasPermissions(context, perms)) {
            // Do not have permissions, request them now
            return false;
        }
        return true;
    }
}
