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
import com.example.noti.adapter.DetailAdapter;
import com.example.noti.databinding.FragmentDetailBinding;
import com.example.noti.model.InfoDetail;
import com.example.noti.model.detail.Detail;
import com.example.noti.network.APIManager;
import com.example.noti.network.Define;
import com.example.noti.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    FragmentDetailBinding bindingDetail;
    List<InfoDetail> infoDetails;
    DetailAdapter adapter;
    double lon,lat;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingDetail=FragmentDetailBinding.inflate(inflater, container, false);

        infoDetails=new ArrayList<>();

        Bundle bundle=getArguments();
        if (bundle!=null){
            lon=bundle.getDouble("lon");
            lat=bundle.getDouble("lat");

            Log.e("kq","lon: "+lon);
            String url= Define.forecast+"?"+Define.lat+lat+Define.lon+lon+Define.lang+getString(R.string.lang)+Define.units+Define.appid;
            getDetail(url);
        }


        return bindingDetail.getRoot();
    }

    public void getDetail(String url){
        APIManager service= RetrofitClient.getRetrofitClient(Define.base_url).create(APIManager.class);
        Call<Detail> call=service.getAPIDetail(url);
        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                Detail detail=response.body();
                for(int i=0;i<10;i++){
                    String icon=detail.getList().get(i).getWeather().get(0).getIcon();
                    String desc=detail.getList().get(i).getWeather().get(0).getDescription();
                    String s=desc.substring(0,1).toUpperCase()+desc.substring(1);
                    int temp=(int) (detail.getList().get(i).getMain().getTemp()+0.5);
                    int hum=detail.getList().get(i).getMain().getHumidity();
                    double wind=detail.getList().get(i).getWind().getSpeed();
                    String time=detail.getList().get(i).getDtTxt();
                    String t=time.substring(11,13)+"h ";
                    String d=time.substring(8,10)+"/"+time.substring(5,7);
                    infoDetails.add(new InfoDetail(t,d,icon,s,temp,hum,wind));
                }
                adapter=new DetailAdapter(infoDetails);
                //RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                bindingDetail.recyclerviewDetail.setHasFixedSize(true);
                bindingDetail.recyclerviewDetail.setLayoutManager(layoutManager);
                bindingDetail.recyclerviewDetail.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Log.e("kq",t.getMessage()+"");
            }
        });
    }
}