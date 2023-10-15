package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.databinding.ItemAlarmtoneBinding;
import com.example.fragment.addAlarmFragment;
import com.example.model.ToneAlarmModel;

import java.util.ArrayList;
import java.util.List;


public class toneAlarmAdapter extends RecyclerView.Adapter<toneAlarmAdapter.toneAlarmHolder> {
    private Context context;
    private List<ToneAlarmModel> list = new ArrayList<>();

    public toneAlarmAdapter(Context context) {
        this.context = context;

    }
    public void setData(List<ToneAlarmModel> list){
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
        if(addAlarmFragment.binding.tvAlarmTone.getText().toString().equals(toneAlarmModel.getName())){
            holder.binding.imgToneAlarmSelected.setVisibility(View.VISIBLE);
            System.out.println(position);
        }else{
            holder.binding.imgToneAlarmSelected.setVisibility(View.INVISIBLE);
        }
        holder.binding.linearToneAlarm.setOnClickListener(view -> {
            addAlarmFragment.binding.tvAlarmTone.setText(toneAlarmModel.getName());
            notifyDataSetChanged();
        });
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
}
