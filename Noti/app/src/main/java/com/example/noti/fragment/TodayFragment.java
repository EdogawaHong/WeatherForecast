package com.example.noti.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noti.R;
import com.example.noti.databinding.FragmentTodayBinding;
import com.example.noti.model.current.Current;
import com.example.noti.model.detail.Detail;
import com.example.noti.model.sixteenday.SixteenDay;
import com.example.noti.network.APIManager;
import com.example.noti.network.Define;
import com.example.noti.network.RetrofitClient;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayFragment extends Fragment {
    FragmentTodayBinding bindingToday;
    List<Integer> temps = new ArrayList<>();
    List<String> times = new ArrayList<>();
    double lon, lat;

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingToday = FragmentTodayBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            lon = bundle.getDouble("lon");
            lat = bundle.getDouble("lat");

            String url = Define.weather + Define.lat + lat + Define.lon + lon + Define.lang + getString(R.string.lang) + Define.units + Define.appid;
            getDataCurrent(url);

            String url2 = Define.forecast + Define.daily + Define.lat + lat + Define.lon + lon + Define.lang + getString(R.string.lang) + Define.units + Define.appid;
            getMinMax(url2);

            String url3 = Define.forecast + "?" + Define.lat + lat + Define.lon + lon + Define.lang + getString(R.string.lang) + Define.units + Define.appid;
            getDetailLineTemps(url3);

            Log.e("url", url + "\n" + url2 + "\n" + url3);

        }

        return bindingToday.getRoot();
    }

    public void getDataCurrent(String url) {
        APIManager service = RetrofitClient.getRetrofitClient(Define.base_url).create(APIManager.class);
        Call<Current> call = service.getAPICurrent(url);
        call.enqueue(new Callback<Current>() {
            @Override
            public void onResponse(Call<Current> call, Response<Current> response) {
                Current current = response.body();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date d = new Date(current.getDt() * 1000L);
                String date = simpleDateFormat.format(d);
                bindingToday.tvTime.setText(date);

                bindingToday.tvPlace.setText(current.getName());

                bindingToday.tvTemp.setText(((int) (current.getMain().getTemp() + 0.5)) + "°");
                bindingToday.tvFeelLikes.setText(((int) (current.getMain().getFeelsLike() + 0.5)) + "°");
                String desc = current.getWeather().get(0).getDescription();
                String status = desc.substring(0, 1).toUpperCase() + desc.substring(1);
                bindingToday.tvDecs.setText(status);

                bindingToday.tvHum.setText(current.getMain().getHumidity() + " %");
                bindingToday.tvClouds.setText(current.getClouds().getAll()+" %");
                bindingToday.tvWindspeed.setText(current.getWind().getSpeed()+" m/s");
                bindingToday.tvWinddeg.setText(current.getWind().getDeg()+"°");
                bindingToday.tvPress.setText(current.getMain().getPressure()+" hPa");

                SimpleDateFormat simple = new SimpleDateFormat("HH:mm");
                Date t1 = new Date(current.getSys().getSunrise() * 1000L);
                String sunrise = simple.format(t1);
                bindingToday.tvSunrise.setText(sunrise);

                Date t2 = new Date(current.getSys().getSunset() * 1000L);
                String sunset = simple.format(t2);
                bindingToday.tvSunset.setText(sunset);

                Picasso.with(getContext()).load("https://openweathermap.org/img/wn/" + current.getWeather().get(0).getIcon() + "@2x.png").into(bindingToday.imgIcon);

            }

            @Override
            public void onFailure(Call<Current> call, Throwable t) {
                Log.e("kq", t.getMessage() + "");
            }
        });
    }

    public void getMinMax(String url) {
        APIManager service = RetrofitClient.getRetrofitClient(Define.base_url).create(APIManager.class);
        Call<SixteenDay> call = service.getAPISixteenDay(url);
        call.enqueue(new Callback<SixteenDay>() {
            @Override
            public void onResponse(Call<SixteenDay> call, Response<SixteenDay> response) {
                SixteenDay sixteenDay = response.body();
                bindingToday.tvMinMax.setText("↑"+((int) (sixteenDay.getList().get(0).getTemp().getMax() + 0.5)) + "° - ↓" +
                        ((int) (sixteenDay.getList().get(0).getTemp().getMin() + 0.5)) + "°");
            }

            @Override
            public void onFailure(Call<SixteenDay> call, Throwable t) {

            }
        });
    }

    public void getDetailLineTemps(String url) {
        times.clear();
        temps.clear();
        APIManager service = RetrofitClient.getRetrofitClient(Define.base_url).create(APIManager.class);
        Call<Detail> call = service.getAPIDetail(url);
        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                Detail detail = response.body();
                for (int i = 0; i < 9; i++) {
                    String time = detail.getList().get(i).getDtTxt();
                    String t = time.substring(11, 13) + "h";
                    int temp = (int) (detail.getList().get(i).getMain().getTemp() + 0.5);
                    times.add(t);
                    temps.add(temp);
                }

                //gán dữ liệu mảng temps vào datapoints
                DataPoint[] dataPoints = new DataPoint[temps.size()];
                for (int i = 0; i < temps.size(); i++) {
                    dataPoints[i] = new DataPoint(i, temps.get(i));
                }
                //Log.e("kq1",dataPoints.length+"");

                //vẽ biểu đồ bằng graphview
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                series.setDrawBackground(true);
                series.setBackgroundColor(getResources().getColor(R.color.graphview));
                series.setColor(Color.BLACK);
                series.setThickness(4);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(6);

                bindingToday.graphview.addSeries(series);

                //hiển thị dữ liệu tại tọa độ
                PointsGraphSeries<DataPoint> seriesPoints = new PointsGraphSeries<>(dataPoints);
                seriesPoints.setCustomShape(new PointsGraphSeries.CustomShape() {
                    @Override
                    public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(35);
                        canvas.drawText(String.valueOf((int) dataPoint.getY()) + "°", x - 30, y - 15, paint);
                    }
                });
                bindingToday.graphview.addSeries(seriesPoints);

                bindingToday.graphview.getViewport().setXAxisBoundsManual(true);
                bindingToday.graphview.getViewport().setMinX(0);
                //bindingToday.graphview.getViewport().setMaxX(4);
                //bindingToday.graphview.getViewport().setScrollable(true);

                bindingToday.graphview.getViewport().setYAxisBoundsManual(true);
                bindingToday.graphview.getViewport().setMinY(0);
                bindingToday.graphview.getViewport().setMaxY(45);
                // bindingToday.graphview.getGridLabelRenderer().setGridColor(Color.BLACK);
                //màu dữ liệu trục x
                bindingToday.graphview.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);

                //ẩn dữ liệu trục y
                bindingToday.graphview.getGridLabelRenderer().setVerticalLabelsVisible(false);
                //ẩn dữ liệu trục x
                //bindingToday.graphview.getGridLabelRenderer().setHorizontalLabelsVisible(false);


                //bindingToday.graphview.getViewport().setScalable(true);
                bindingToday.graphview.getViewport().setDrawBorder(false);//ẩn trục
                bindingToday.graphview.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);//ẩn đường lưới

                bindingToday.graphview.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            return times.get((int) value % times.size());
                        }
                        return super.formatLabel(value, isValueX);
                    }
                });
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Log.e("kq", t.getMessage() + "");
            }
        });
    }


}