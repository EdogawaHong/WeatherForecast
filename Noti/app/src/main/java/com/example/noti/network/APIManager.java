package com.example.noti.network;

import com.example.noti.model.current.Current;
import com.example.noti.model.detail.Detail;
import com.example.noti.model.sixteenday.SixteenDay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface APIManager {
    @GET
    Call<Current> getAPICurrent(@Url String url);

    @GET
    Call<SixteenDay> getAPISixteenDay(@Url String url);

    @GET
    Call<Detail> getAPIDetail(@Url String url);
}
