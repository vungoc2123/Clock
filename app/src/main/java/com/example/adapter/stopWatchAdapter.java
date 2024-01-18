package com.example.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.databinding.ItemStopwatchBinding;

import java.util.ArrayList;
import java.util.List;

public class stopWatchAdapter extends RecyclerView.Adapter<stopWatchAdapter.stopWatchViewHolder> {

    private Context context;
    private List<String> times = new ArrayList<>();
    private long min, max;
    public stopWatchAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> times,Long min , Long max){
        this.times = times;
        this.min = min;
        this.max =max;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public stopWatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new stopWatchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stopwatch, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull stopWatchViewHolder holder, int position) {
        String time = times.get(position);
        String timeStr = times.get(position).replaceAll("\\D", "");
        long stopWatch = Long.parseLong(timeStr);
        if(times.size()>=2){
            if(stopWatch == min){
                holder.binding.tvItemStopWatchRing.setTextColor(Color.parseColor("#24B13B"));
                holder.binding.tvItemStopWatchTime.setTextColor(Color.parseColor("#24B13B"));
            } else if(stopWatch == max) {
                holder.binding.tvItemStopWatchRing.setTextColor(Color.parseColor("#ad2f2f"));
                holder.binding.tvItemStopWatchTime.setTextColor(Color.parseColor("#ad2f2f"));
            } else{
                holder.binding.tvItemStopWatchRing.setTextColor(Color.parseColor("#fdf2f3"));
                holder.binding.tvItemStopWatchTime.setTextColor(Color.parseColor("#fdf2f3"));
            }
        }else{
            holder.binding.tvItemStopWatchRing.setTextColor(Color.parseColor("#fdf2f3"));
            holder.binding.tvItemStopWatchTime.setTextColor(Color.parseColor("#fdf2f3"));
        }
        holder.binding.tvItemStopWatchRing.setText(holder.itemView.getContext().getString(R.string.lap)+" "+(position+1));
        holder.binding.tvItemStopWatchTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    class  stopWatchViewHolder extends RecyclerView.ViewHolder{
        private ItemStopwatchBinding binding;
        public stopWatchViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStopwatchBinding.bind(itemView);
        }
    }

}
