package com.example.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.databinding.ItemAlarmtoneBinding;
import com.example.config.ConfigString;
import com.example.fragment.addAlarmFragment;
import com.example.fragment.timerFragment;
import com.example.model.ToneAlarmModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class toneAlarmAdapter extends RecyclerView.Adapter<toneAlarmAdapter.toneAlarmHolder> {
    private Context context;
    private String nameFragment;
    public MediaPlayer my_player;
    private List<ToneAlarmModel> list = new ArrayList<>();

    public toneAlarmAdapter(Context context, String nameFragment) {
        this.context = context;
        this.nameFragment = nameFragment;
    }

    public void setData(List<ToneAlarmModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public toneAlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new toneAlarmAdapter.toneAlarmHolder(LayoutInflater.from(context).inflate(R.layout.item_alarmtone, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull toneAlarmHolder holder, int position) {
        ToneAlarmModel toneAlarmModel = list.get(position);
        holder.binding.tvNameTone.setText(toneAlarmModel.getName());
        if (nameFragment.equals(ConfigString.timerFragment)) {
            if (timerFragment.binding.tvTimerToneAlarm.getText().toString().equals(toneAlarmModel.getName())) {
                holder.binding.imgToneAlarmSelected.setVisibility(View.VISIBLE);
            } else {
                holder.binding.imgToneAlarmSelected.setVisibility(View.INVISIBLE);
            }
        }
        if (nameFragment.equals(ConfigString.alarmFragment)) {
            if (addAlarmFragment.binding.tvAlarmTone.getText().toString().equals(toneAlarmModel.getName())) {
                holder.binding.imgToneAlarmSelected.setVisibility(View.VISIBLE);
            } else {
                holder.binding.imgToneAlarmSelected.setVisibility(View.INVISIBLE);
            }
        }

        holder.binding.linearToneAlarm.setOnClickListener(view -> {
            startMedia(toneAlarmModel);
            if (nameFragment.equals(ConfigString.timerFragment)) {
                timerFragment.binding.tvTimerToneAlarm.setText(toneAlarmModel.getName());
            }
            if (nameFragment.equals(ConfigString.alarmFragment)) {
                addAlarmFragment.binding.tvAlarmTone.setText(toneAlarmModel.getName());
            }
            notifyDataSetChanged();
        });
        if(position == list.size()-1){
            holder.binding.viewToneAlarm.setVisibility(View.GONE);
        }else{
            holder.binding.viewToneAlarm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class toneAlarmHolder extends RecyclerView.ViewHolder {
        private ItemAlarmtoneBinding binding;

        public toneAlarmHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAlarmtoneBinding.bind(itemView);
        }
    }
    public void startMedia(ToneAlarmModel toneAlarmModel){
        try {
            if (my_player != null) {
                if (my_player.isPlaying()) {
                    my_player.stop();
                }
                my_player.reset();
                my_player.release();
            }
            my_player = new MediaPlayer();
            Uri alarmUri = ContentUris.withAppendedId(Uri.parse("content://media/internal/audio/media"), toneAlarmModel.getId());
            my_player.setDataSource(context, alarmUri);
            my_player.prepare();
            my_player.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
