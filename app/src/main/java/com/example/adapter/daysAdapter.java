package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.clock.R;
import com.example.clock.databinding.ItemDayBinding;
import com.example.config.ConfigDay;
import com.example.fragment.addAlarmFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class daysAdapter extends RecyclerView.Adapter<daysAdapter.dayHolder> {
    private Context context;
    public List<String> list = new ArrayList<>();
    public List<String> listDays = new ArrayList<>();

    public daysAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public dayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new daysAdapter.dayHolder(LayoutInflater.from(context).inflate(R.layout.item_day, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull dayHolder holder, int position) {
        String[] daysArray = addAlarmFragment.binding.tvAlarmRepeatDays.getText().toString().split(" ");
        if (daysArray[0].equals(holder.itemView.getContext().getString(R.string.every))) {
            if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("vi")) {
                if (daysArray[1].equals(context.getString(R.string.day))) {
                    daysArray[0] = daysArray[0] + " " + daysArray[1];
                    daysArray[1] = "";
                } else {
                    daysArray[0] = daysArray[0] + " " + daysArray[1] + daysArray[2];
                    daysArray[1] = "";
                    daysArray[2] = "";
                }
            } else {
                daysArray[0] = holder.itemView.getContext().getString(R.string.every) + " " + daysArray[1];
                daysArray[1] = "";
            }
        }
        String day = list.get(position);
        holder.binding.tvDay.setText(day);

        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("vi")) {
            String days = ConfigDay.handleAddComma(addAlarmFragment.binding.tvAlarmRepeatDays.getText().toString());
            daysArray = days.split(",");
        }
        for (String selectedDay : daysArray) {
            if (ConfigDay.handleConvertNumber(context, selectedDay.trim()).equals(day) || daysArray[0].equals(context.getString(R.string.every_day))) {
                holder.binding.imgDaySelected.setVisibility(View.VISIBLE);
                if (daysArray[0].equals(context.getString(R.string.every_day))) {
                    listDays.add(day);
                } else {
                    listDays.add(ConfigDay.handleConvertNumber(context, selectedDay.trim()));
                }
                break;
            } else {
                holder.binding.imgDaySelected.setVisibility(View.INVISIBLE);
            }
        }
        holder.binding.getRoot().setOnClickListener(view -> {
            if (listDays.contains(day)) {
                listDays.remove(day);
                holder.binding.imgDaySelected.setVisibility(View.INVISIBLE);
                String listDay = addAlarmFragment.binding.tvAlarmRepeatDays.getText().toString().replace(ConfigDay.handleConvertText(context, day), "").trim();
                if (listDays.size() == 0) {
                    listDay = "";
                }
                if (listDays.size() == 6) {
                    listDay = "";
                    for (String daySelected : listDays) {
                        listDay += ConfigDay.handleConvertText(context, daySelected) + " ";
                    }
                }
                addAlarmFragment.binding.tvAlarmRepeatDays.setText(listDay.trim());
            } else {
                listDays.add(day);
                holder.binding.imgDaySelected.setVisibility(View.VISIBLE);
                String repeatDay = handleSort(ConfigDay.handleConvertText(context, addAlarmFragment.binding.tvAlarmRepeatDays.getText().toString()) + " " + ConfigDay.handleConvertText(context, day));
                addAlarmFragment.binding.tvAlarmRepeatDays.setText(repeatDay);
                if (listDays.size() >= 5 && listDays.size() < 7) {
                    addAlarmFragment.binding.tvAlarmRepeatDays.setTextSize(16);
                } else {
                    addAlarmFragment.binding.tvAlarmRepeatDays.setTextSize(18);
                }
                if (listDays.size() == 7) {
                    addAlarmFragment.binding.tvAlarmRepeatDays.setText(context.getString(R.string.every_day));
                }
            }
            if (listDays.size() == 1) {
                addAlarmFragment.binding.tvAlarmRepeatDays.setText(handleSort(ConfigDay.handleConvertNumber(context, addAlarmFragment.binding.tvAlarmRepeatDays.getText().toString().trim())));
            }
        });

        if (position == list.size() - 1) {
            holder.binding.viewDay.setVisibility(View.INVISIBLE);
        } else {
            holder.binding.viewDay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class dayHolder extends RecyclerView.ViewHolder {
        private ItemDayBinding binding;

        public dayHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemDayBinding.bind(itemView);
        }
    }


    public static void sortDaysOfWeek(String[] days, Context context) {
        // Sử dụng Comparator để sắp xếp ngày theo thứ tự trong tuần
        Arrays.sort(days, new Comparator<String>() {
            @Override
            public int compare(String day1, String day2) {
                // Chuyển đổi ngày về dạng số để so sánh
                int index1 = getIndex(day1);
                int index2 = getIndex(day2);
                return Integer.compare(index1, index2);
            }

            // Phương thức này trả về vị trí của ngày trong tuần
            private int getIndex(String day) {
                String trimmedDay = day.trim();
                if (context.getString(R.string.mon).equals(trimmedDay)) {
                    return 1;
                } else if (context.getString(R.string.tue).equals(trimmedDay)) {
                    return 2;
                } else if (context.getString(R.string.wed).equals(trimmedDay)) {
                    return 3;
                } else if (context.getString(R.string.thu).equals(trimmedDay)) {
                    return 4;
                } else if (context.getString(R.string.fri).equals(trimmedDay)) {
                    return 5;
                } else if (context.getString(R.string.sat).equals(trimmedDay)) {
                    return 6;
                } else if (context.getString(R.string.sun).equals(trimmedDay)) {
                    return 7;
                } else {
                    return 8;
                }
            }
        });
    }

    public String handleSort(String listDay) {
        String days = ConfigDay.handleAddComma(listDay);
        String[] parts;
        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("vi")) {
            parts = days.split(",");
        } else {
            parts = days.split(" ");
        }
        sortDaysOfWeek(parts, context);
        String chuoi = "";
        for (String part : parts) {
            chuoi += part.trim() + " ";
        }
        chuoi.replaceAll("\\s+", " ");
        return chuoi.trim();
    }
}
