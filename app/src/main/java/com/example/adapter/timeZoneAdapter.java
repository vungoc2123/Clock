package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.databinding.ItemTimezoneBinding;
import com.example.database.TimeZoneDAO;
import com.example.fragment.internationalFragment;
import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class timeZoneAdapter extends RecyclerView.Adapter<timeZoneAdapter.timeZoneViewHolder> implements Filterable {
    private Context context;
    private List<TimeZoneModel> timeZoneModels = new ArrayList<>();
    private List<TimeZoneModel> timeZoneModels2 = new ArrayList<>();
    private TimeZoneDAO timeZoneDAO;

    private IOnItemClick iOnItemClick;

    public void onItemClick(IOnItemClick iOnItemClick) {
        this.iOnItemClick = iOnItemClick;
    }

    public timeZoneAdapter(Context context) {
        this.context = context;
        timeZoneDAO = new TimeZoneDAO(context);
    }

    public void setData(List<TimeZoneModel> list) {
        this.timeZoneModels = list;
        this.timeZoneModels2 = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public timeZoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new timeZoneAdapter.timeZoneViewHolder(LayoutInflater.from(context).inflate(R.layout.item_timezone, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull timeZoneViewHolder holder, int position) {
        TimeZoneModel timeZoneModel = timeZoneModels.get(position);
        holder.binding.tvTimezoneName.setText(timeZoneModel.getName());
        if(position == timeZoneModels.size()-1){
            holder.binding.viewTimezone.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(view -> {
            iOnItemClick.onItemClick();
            if(timeZoneModel.getStatus() == 0){
                Date date = new Date();
                int unixTimestamp = (int) date.getTime();
                timeZoneModel.setStatus(unixTimestamp);
                update(timeZoneModel);
                List<TimeZoneModel> times = timeZoneDAO.getAllTimeZone();
                times = internationalFragment.filterList(times);
                internationalFragment.adapter.setData(times,false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeZoneModels.size();
    }


    class timeZoneViewHolder extends RecyclerView.ViewHolder {
        private ItemTimezoneBinding binding;

        public timeZoneViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTimezoneBinding.bind(itemView);
        }
    }

    public void update(TimeZoneModel timeZoneModel) {
        int kq = timeZoneDAO.updateTimeZone(timeZoneModel);
        if (kq == -1) {
            Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
        }
        if (kq == 1) {
            Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
        }
    }

    public interface IOnItemClick {
        void onItemClick();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                List<TimeZoneModel> list = new ArrayList<>();
                if(search.isEmpty()){
                    timeZoneModels = timeZoneModels2;
                }else{
                    for(TimeZoneModel timeZoneModel : timeZoneModels2){
                        if(timeZoneModel.getName().toLowerCase().contains(search.toLowerCase())){
                            list.add(timeZoneModel);
                        }
                    }
                    timeZoneModels = list;
                }
                FilterResults results = new FilterResults();
                results.values = timeZoneModels;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                timeZoneModels = (List<TimeZoneModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
