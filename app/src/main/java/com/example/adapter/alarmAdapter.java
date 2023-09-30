package com.example.adapter;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.broadcast.MyBroadcast;
import com.example.clock.R;
import com.example.clock.databinding.ItemAlarmBinding;
import com.example.config.ConfigAlarmManager;
import com.example.database.AlarmDAO;
import com.example.fragment.internationalFragment;
import com.example.model.AlarmModel;
import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class alarmAdapter extends RecyclerView.Adapter<alarmAdapter.alarmViewHolder> {

    private Context context;
    private List<AlarmModel> alarmModels = new ArrayList<>();
    private AlarmDAO alarmDAO;
    private Boolean check = false;
    private Boolean checkAnimator = false;

    private IOnItemClick iOnItemClick;

    private ConfigAlarmManager configAlarmManager = new ConfigAlarmManager();

    public void onItemClick(IOnItemClick iOnItemClick) {
        this.iOnItemClick = iOnItemClick;
    }

    public alarmAdapter(Context context) {
        this.context = context;
        alarmDAO = new AlarmDAO(context);
    }

    public void setData(List<AlarmModel> list, boolean check) {
        this.alarmModels = list;
        this.check = check;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public alarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new alarmAdapter.alarmViewHolder(LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull alarmViewHolder holder, int position) {
        setWidth(holder.binding.linearAlarm);
        AlarmModel alarmModel = alarmModels.get(position);
        holder.binding.tvAlarmTime.setText(alarmModel.getTime());
        holder.binding.tvAlarmLabel.setText(alarmModel.getLabel());
        holder.binding.switchAlarm.setChecked(alarmModel.getStatus() == 1 ? true : false);

        if(check){
            holder.binding.imgDelete.setVisibility(View.VISIBLE);
            holder.binding.imgDelete.setOnClickListener(view -> {
                animator(holder.itemView, 0, -160);
                holder.binding.switchAlarm.setVisibility(View.GONE);
                checkAnimator = true;
            });
        }
        holder.binding.getRoot().setOnClickListener(view -> {
            if(!check){
                iOnItemClick.onItemClick(alarmModel);
            }else{
                if(checkAnimator){
                    animator(holder.itemView, -160, 0);
                    holder.binding.switchAlarm.setVisibility(View.VISIBLE);
                    checkAnimator = false;
                }
            }
        });
        holder.binding.switchAlarm.setOnClickListener(view -> {
            if( holder.binding.switchAlarm.isChecked()){
                alarmModel.setStatus(1);
                holder.binding.tvAlarmLabel.setTextColor(Color.parseColor("#fdf2f3"));
                holder.binding.tvAlarmTime.setTextColor(Color.parseColor("#fdf2f3"));
            }else{
                alarmModel.setStatus(0);
                holder.binding.tvAlarmLabel.setTextColor(Color.parseColor("#757579"));
                holder.binding.tvAlarmTime.setTextColor(Color.parseColor("#757579"));
            }
            alarmDAO.updateAlarm(alarmModel);
        });
        if(alarmModel.getStatus()==1){
            holder.binding.tvAlarmLabel.setTextColor(Color.parseColor("#fdf2f3"));
            holder.binding.tvAlarmTime.setTextColor(Color.parseColor("#fdf2f3"));
        }else{
            holder.binding.tvAlarmLabel.setTextColor(Color.parseColor("#757579"));
            holder.binding.tvAlarmTime.setTextColor(Color.parseColor("#757579"));
        }

        holder.binding.tvAlarmDelete.setOnClickListener(view -> {
            handleDelete(alarmModel,holder.binding.getRoot());
        });

        if(holder.binding.switchAlarm.isChecked()){
            if(alarmModel.getRepeat() == 1){
                configAlarmManager.startAlarmRepeat(context,alarmModel,"alarmFragment");
            }else{
                configAlarmManager.startAlarm(context,alarmModel,"alarmFragment");
            }
        }
    }

    @Override
    public int getItemCount() {
        return alarmModels.size();
    }

    class alarmViewHolder extends RecyclerView.ViewHolder {
        private ItemAlarmBinding binding;

        public alarmViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAlarmBinding.bind(itemView);
        }
    }

    public interface IOnItemClick {
        void onItemClick(AlarmModel alarmModel);
    }

    public void setWidth(LinearLayout linearLayout) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels + 170;
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.width = screenWidth;
        linearLayout.setLayoutParams(layoutParams);
    }
    public void animator(View view, int x, int y) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", x, y);
        animator.setDuration(300);
        animator.start();
    }
    public void handleDelete(AlarmModel model,  View view) {
        alarmDAO.delete(model.getId());
        List<AlarmModel> list = alarmDAO.getAllAlarm();
        Collections.sort(list, Comparator.comparingInt(alarm -> {
            String timeStr = alarm.getTime().replaceAll("\\D", "");
            return Integer.parseInt(timeStr);
        }));
        setData(list, false);
        animator(view, 0, 0);
    }
}
