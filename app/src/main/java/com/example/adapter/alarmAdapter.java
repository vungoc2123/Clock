package com.example.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.databinding.ItemAlarmBinding;
import com.example.config.ConfigAlarmManager;
import com.example.config.ConfigString;
import com.example.database.AlarmDAO;
import com.example.model.AlarmModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class alarmAdapter extends RecyclerView.Adapter<alarmAdapter.alarmViewHolder> {

    private Context context;
    private List<AlarmModel> alarmModels = new ArrayList<>();
    private AlarmDAO alarmDAO;
    private Boolean check = false;


    private IOnItemClick iOnItemClick;

    private ConfigAlarmManager configAlarmManager = new ConfigAlarmManager();
    private float scale;

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
        AtomicReference<Boolean> checkAnimator = new AtomicReference<>(false);
        scale = context.getResources().getDisplayMetrics().density;
        AlarmModel alarmModel = alarmModels.get(position);
        holder.binding.tvAlarmTime.setText(alarmModel.getTime());
        if(alarmModel.getRepeatDays().length()>0){
            holder.binding.tvAlarmLabel.setText(alarmModel.getLabel() + ", "+ alarmModel.getRepeatDays());
        }else{
            holder.binding.tvAlarmLabel.setText(alarmModel.getLabel());
        }
        holder.binding.switchAlarm.setChecked(alarmModel.getStatus() == 1 ? true : false);
        if(position == alarmModels.size()-1){
            holder.binding.viewAlarm.setVisibility(View.VISIBLE);
        }
        if(check){
            holder.binding.imgDelete.setVisibility(View.VISIBLE);
            holder.binding.imgAlarmNext.setVisibility(View.VISIBLE);
            holder.binding.switchAlarm.setVisibility(View.GONE);
            holder.binding.imgDelete.setOnClickListener(view -> {
                animator(holder.itemView, 0, -(int)(65f * scale + 0.5f));
                checkAnimator.set(true);
            });
        }
        holder.binding.getRoot().setOnClickListener(view -> {

                if(!checkAnimator.get()){
                    iOnItemClick.onItemClick(alarmModel);
                }
                if(checkAnimator.get()){
                    animator(holder.itemView, -(int)(65f * scale + 0.5f), 0);
                    checkAnimator.set(false);
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
                configAlarmManager.startAlarm(context,alarmModel, ConfigString.alarmFragment, true);
            }else{
                configAlarmManager.startAlarm(context,alarmModel,ConfigString.alarmFragment, false);
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
        scale = context.getResources().getDisplayMetrics().density;
        int intValue = (int) (45f * scale + 0.5f);
        int screenWidth = displayMetrics.widthPixels + intValue;
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
        setData(list, true);
        animator(view, 0, 0);
    }
}
