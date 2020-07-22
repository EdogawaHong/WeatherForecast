package com.example.noti.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noti.R;
import com.example.noti.adapter.DayAdapter;
import com.example.noti.databinding.FragmentAweekBinding;
import com.example.noti.model.InfoOfDay;
import com.example.noti.model.sixteenday.SixteenDay;
import com.example.noti.network.APIManager;
import com.example.noti.network.Define;
import com.example.noti.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AweekFragment extends Fragment {

    FragmentAweekBinding bindingAweek;
    List<InfoOfDay> infoOfDays;
    DayAdapter adapter;
    double lon,lat;

    public AweekFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bindingAweek= FragmentAweekBinding.inflate(inflater, container, false);

        infoOfDays=new ArrayList<>();

        Bundle bundle=getArguments();
        if (bundle!=null){
            lon=bundle.getDouble("lon");
            lat=bundle.getDouble("lat");

            Log.e("kq","lon: "+lon);
            String url= Define.forecast+Define.daily+Define.lat+lat+Define.lon+lon+Define.lang+getString(R.string.lang)+Define.units+Define.appid;
            getDataAweek(url);
        }

        return bindingAweek.getRoot();
    }

    public void getDataAweek(String url){
        APIManager service= RetrofitClient.getRetrofitClient(Define.base_url).create(APIManager.class);
        Call<SixteenDay> call=service.getAPISixteenDay(url);
        call.enqueue(new Callback<SixteenDay>() {
            @Override
            public void onResponse(Call<SixteenDay> call, Response<SixteenDay> response) {
                SixteenDay sixteenDay=response.body();
                for(int i=0;i<7;i++){
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE, dd MMM");
                    Date d=new Date(sixteenDay.getList().get(i).getDt()*1000L);
                    String dayofweek=simpleDateFormat.format(d);


                    int max=(int) (sixteenDay.getList().get(i).getTemp().getMax()+0.5);
                    int min=(int) (sixteenDay.getList().get(i).getTemp().getMin()+0.5);
                    String status=sixteenDay.getList().get(i).getWeather().get(0).getDescription();
                    String s=status.substring(0,1).toUpperCase()+status.substring(1);
                    String icon=sixteenDay.getList().get(i).getWeather().get(0).getIcon();
                    int hum=sixteenDay.getList().get(i).getHumidity();
                    double wind=sixteenDay.getList().get(i).getSpeed();
                    int cloud=sixteenDay.getList().get(i).getClouds();
                    infoOfDays.add(new InfoOfDay(dayofweek,s,icon,max,min,hum,wind,cloud));
                }
                adapter=new DayAdapter(infoOfDays);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                bindingAweek.recyclerview.setLayoutManager(layoutManager);
                bindingAweek.recyclerview.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<SixteenDay> call, Throwable t) {
                Log.e("kq",t.getMessage()+"");
            }
        });
    }
}