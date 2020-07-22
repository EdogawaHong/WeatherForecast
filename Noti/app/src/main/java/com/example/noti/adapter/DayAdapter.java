package com.example.noti.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noti.R;
import com.example.noti.model.InfoOfDay;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.Viewhoder> {

    List<InfoOfDay> infoOfDays;

    public DayAdapter(List<InfoOfDay> infoOfDays) {
        this.infoOfDays = infoOfDays;
    }

    @NonNull
    @Override
    public Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_weather,parent,false);

        Viewhoder viewhoder=new Viewhoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhoder holder, int position) {
        InfoOfDay infoOfDay=infoOfDays.get(position);
        holder.tvDayofWeek.setText(infoOfDay.getDayofweek());
        holder.tvStatus.setText(infoOfDay.getStatus());
        holder.tvHum.setText(infoOfDay.getHum()+" %");
        holder.tvWind.setText(infoOfDay.getWind()+" m/s");
        holder.tvClouds.setText(infoOfDay.getClouds()+" %");
        holder.tvTemMin.setText(infoOfDay.getTempMin()+"°");
        holder.tvTempMax.setText(infoOfDay.getTempMax()+"°");
        Picasso.with(holder.imgStatus.getContext()).load("https://openweathermap.org/img/wn/"+infoOfDay.getIconStatus()+"@2x.png").into(holder.imgStatus);
    }

    @Override
    public int getItemCount() {
        return infoOfDays.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder {
        TextView tvStatus,tvTempMax,tvTemMin,tvDayofWeek,tvHum,tvWind,tvClouds;
        ImageView imgStatus;
        public Viewhoder(@NonNull View itemView) {
            super(itemView);

            tvDayofWeek=itemView.findViewById(R.id.tvDayofWeek);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvHum=itemView.findViewById(R.id.tvHumOfDay);
            tvWind=itemView.findViewById(R.id.tvWinOfDay);
            tvClouds=itemView.findViewById(R.id.tvCloudOfDay);
            tvTempMax=itemView.findViewById(R.id.tvTempMax);
            tvTemMin=itemView.findViewById(R.id.tvTempMin);
            imgStatus=itemView.findViewById(R.id.imgStatus);
        }
    }
}
