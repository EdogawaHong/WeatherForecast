package com.example.noti.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noti.R;
import com.example.noti.model.InfoDetail;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.Viewhoder> {

    List<InfoDetail> infoDetails;

    public DetailAdapter(List<InfoDetail> infoDetails) {
        this.infoDetails = infoDetails;
    }

    @NonNull
    @Override
    public Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_detail,parent,false);

        Viewhoder viewhoder=new Viewhoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhoder holder, int position) {
        InfoDetail infoDetail=infoDetails.get(position);
        holder.tvTemp.setText(infoDetail.getTemp()+"°");
        holder.tvHum.setText(infoDetail.getHum()+" %");
        holder.tvWind.setText(infoDetail.getWind()+" m/s");
        holder.tvTime.setText(infoDetail.getTime());
        holder.tvDate.setText(infoDetail.getDate());
        Picasso.with(holder.imgStatus.getContext()).load("https://openweathermap.org/img/wn/"+infoDetail.getIconDetail()+"@2x.png").into(holder.imgStatus);
    }

    @Override
    public int getItemCount() {
        return infoDetails.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder {
        TextView tvTemp,tvDate,tvHum,tvWind,tvRain,tvTime;
        ImageView imgStatus;
        public Viewhoder(@NonNull View itemView) {
            super(itemView);
            tvTemp=itemView.findViewById(R.id.tvTempDetail);
            tvHum=itemView.findViewById(R.id.tvHumDetail);
            tvWind=itemView.findViewById(R.id.tvWindDetail);
            tvTime=itemView.findViewById(R.id.tvTimeDetail);
            imgStatus=itemView.findViewById(R.id.imgDetail);
            tvDate=itemView.findViewById(R.id.tvDateDetail);
        }
    }
}
