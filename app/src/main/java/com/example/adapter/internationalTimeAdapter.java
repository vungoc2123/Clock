package com.example.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clock.R;
import com.example.clock.databinding.ItemInternationalBinding;
import com.example.database.TimeZoneDAO;
import com.example.fragment.internationalFragment;
import com.example.model.TimeZoneModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class internationalTimeAdapter extends RecyclerView.Adapter<internationalTimeAdapter.internationalViewHolder> {
    private Context context;
    private List<TimeZoneModel> timeZoneModels = new ArrayList<>();
    private TimeZoneDAO timeZoneDAO;
    private Boolean check = false;
    private Handler handler;

    public internationalTimeAdapter(Context context) {
        this.context = context;
        timeZoneDAO = new TimeZoneDAO(context);
    }

    public void setData(List<TimeZoneModel> list, boolean check) {
        this.timeZoneModels = list;
        this.check = check;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public internationalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new internationalTimeAdapter.internationalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_international, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull internationalViewHolder holder, int position) {
        setWidth(holder.binding.linearInternational);
        if (check) {
            holder.binding.imgDelete.setVisibility(View.VISIBLE);
            holder.binding.imgDelete.setOnClickListener(view -> {
                animator(holder.itemView, 0, -160);
            });
        }
        if(position == timeZoneModels.size()-1){
            holder.binding.viewInternationaltime.setVisibility(View.VISIBLE);
        }
        holder.binding.getRoot().setOnClickListener(view -> {
            animator(holder.itemView, -160, 0);
        });
        TimeZoneModel timeZoneModel = timeZoneModels.get(position);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        TimeZone timeZone = TimeZone.getDefault();
        int rawOffsetMillis = timeZone.getRawOffset();
        int rawOffsetHours = rawOffsetMillis / 3600000;
        int rawOffsetMinutes = (rawOffsetMillis % 3600000) / 60000;
        int hour;
        int minute;
        String[] parts = timeZoneModel.getTimezone().split(":");
        if (Integer.parseInt(parts[0]) >= 0) {
            hour = Integer.parseInt(parts[0]) - rawOffsetHours;
            minute = Integer.parseInt(parts[1]) + rawOffsetMinutes;
        } else {
            hour = Integer.parseInt(parts[0]) - rawOffsetHours;
            minute = -Integer.parseInt(parts[1]) + rawOffsetMinutes;
        }
        String formattedTime = dateFormat.format(date);
        String timeStr = formattedTime.replaceAll("\\D", "");
        int currentTime = Integer.parseInt(timeStr);
        String[] name = timeZoneModel.getName().split(",");
        holder.binding.tvItnTimeZone.setText(handleDate(currentTime, hour, Integer.parseInt(parts[1])));
        holder.binding.tvItnName.setText(name[0]);
        timeZoneModel.setStatus(0);

        calendar.add(Calendar.HOUR_OF_DAY, hour);
        calendar.add(Calendar.MINUTE, minute);


        handler = new Handler();
        Runnable updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, hour);
                calendar.add(Calendar.MINUTE, minute);
                Date date1 = calendar.getTime();
                holder.binding.tvItnTime.setText(dateFormat.format(date1.getTime()));
                handler.postDelayed(this, 100);
            }
        };
        handler.post(updateTimeRunnable);
        holder.binding.tvItnDelete.setOnClickListener(view -> {
            handleUpdate(timeZoneModel, position, holder.itemView);
        });
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

    @Override
    public int getItemCount() {
        return timeZoneModels.size();
    }

    class internationalViewHolder extends RecyclerView.ViewHolder {
        private ItemInternationalBinding binding;

        public internationalViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemInternationalBinding.bind(itemView);
        }
    }

    public String handleDate(int x, int y, int c) {
        int z = Integer.parseInt(y + "00");
        if (x + z < 0) {
            return "Yesterday, " + (y >= 0 ? "+" + y : y) + (c != 0 ? ":" + c : "G");
        } else if (x + z < 2400) {
            return "Today, " + (y >= 0 ? "+" + y : y) + (c != 0 ? ":" + c : "G");
        } else {
            return "Tomorrow, " + (y >= 0 ? "+" + y : y) + (c != 0 ? ":" + c : "G");
        }
    }

    public void handleUpdate(TimeZoneModel model, int position, View view) {
        handler.removeCallbacksAndMessages(null);
        timeZoneDAO.updateTimeZone(model);
        List<TimeZoneModel> list = timeZoneDAO.getAllTimeZone();
        list = internationalFragment.filterList(list);
        setData(list, false);
        animator(view, 0, 0);
    }

    public void text(TextView textView2) {
            textView2.setText("");
    }

    public void animator(View view, int x, int y) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", x, y);
        animator.setDuration(300);
        animator.start();
    }


}
