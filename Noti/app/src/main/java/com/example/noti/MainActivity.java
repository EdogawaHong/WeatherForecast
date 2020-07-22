package com.example.noti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.example.noti.adapter.ViewPagerAdapter;
import com.example.noti.databinding.ActivityMainBinding;
import com.example.noti.fragment.AweekFragment;
import com.example.noti.fragment.DetailFragment;
import com.example.noti.fragment.TodayFragment;
import com.example.noti.location.GPSLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    String[] places = {"Hà Nội", "Hải Phòng", "Đà Nẵng", "Huế", "Thanh Hóa", "Nha Trang","Hải Dương","TP Hồ Chí Minh","Quảng Ninh"};

    public String place;
    public double lon = 0, lat = 0;

    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        startService(new Intent(getBaseContext(),ServiceNotification.class));

        binding.autoComplete.setCursorVisible(false);
        binding.autoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.autoComplete.setCursorVisible(true);
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
        binding.autoComplete.setAdapter(adapter);
        binding.autoComplete.setThreshold(0);

        if (!checkRequiredPermissions()) checkRequiredPermissions();
        final GPSLocation gpsLocation = new GPSLocation(this);
        if (gpsLocation.canGetLocation) {
            gpsLocation.getLocation();
            lon = gpsLocation.getLongitude();
            lat = gpsLocation.getLatitude();
            //Log.e("abc",lon+" "+lat);
        } else {
            dialogConfirm();
        }
        bundle = new Bundle();
        final TodayFragment todayFragment = new TodayFragment();
        final DetailFragment detailFragment = new DetailFragment();
        final AweekFragment aweekFragment = new AweekFragment();

        bundle.putDouble("lon", lon);
        bundle.putDouble("lat", lat);
        todayFragment.setArguments(bundle);
        detailFragment.setArguments(bundle);
        aweekFragment.setArguments(bundle);

        addTabs(binding.viewpager, todayFragment, detailFragment, aweekFragment);
        binding.tablayout.setupWithViewPager(binding.viewpager);

        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place = binding.autoComplete.getText().toString();
                //Log.e("kq", place);
                LatLng latLng = gpsLocation.getLocationFromAddress(getBaseContext(), place);
                if (latLng != null) {
                    lon = latLng.longitude;
                    lat = latLng.latitude;

                    bundle.putDouble("lon", lon);
                    bundle.putDouble("lat", lat);
                    todayFragment.setArguments(bundle);
                    detailFragment.setArguments(bundle);
                    aweekFragment.setArguments(bundle);

                    addTabs(binding.viewpager, todayFragment, detailFragment, aweekFragment);
                    binding.tablayout.setupWithViewPager(binding.viewpager);

                    binding.autoComplete.setCursorVisible(false);
                } else {
                    dialogNotification();
                }
                //ẩn bàn phím
                hideKeyboard(MainActivity.this);
            }
        });

        binding.imgPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.autoComplete.setText("");
                if (gpsLocation.canGetLocation) {
                    gpsLocation.getLocation();
                    lon = gpsLocation.getLongitude();
                    lat = gpsLocation.getLatitude();
                    bundle.putDouble("lon", lon);
                    bundle.putDouble("lat", lat);
                    todayFragment.setArguments(bundle);
                    detailFragment.setArguments(bundle);
                    aweekFragment.setArguments(bundle);

                    addTabs(binding.viewpager, todayFragment, detailFragment, aweekFragment);
                    binding.tablayout.setupWithViewPager(binding.viewpager);
                } else {
                    dialogConfirm();
                }
            }
        });

    }
    private boolean checkRequiredPermissions() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.WAKE_LOCK};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "can cap quyen",
                    20000, perms);
            return false;
        }
        return true;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public void addTabs(ViewPager viewPager, TodayFragment todayFragment, DetailFragment detailFragment, AweekFragment aweekFragment) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.add(todayFragment, getString(R.string.today) + "");
        viewPagerAdapter.add(detailFragment, getString(R.string.detail) + "");
        viewPagerAdapter.add(aweekFragment, getString(R.string.aweek) + "");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void dialogConfirm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.notification);
        dialog.setMessage(R.string.message_confirm);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void dialogNotification() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.notification);
        dialog.setMessage(R.string.message_notification);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}