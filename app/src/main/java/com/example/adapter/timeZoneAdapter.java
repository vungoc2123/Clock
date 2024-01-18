package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.databinding.ItemTimezoneBinding;
import com.example.database.TimeWorldDAO;
import com.example.database.TimeZoneDAO;
import com.example.fragment.internationalFragment;
import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class timeZoneAdapter extends RecyclerView.Adapter<timeZoneAdapter.timeZoneViewHolder> implements Filterable {
    private Context context;
    private List<TimeZoneModel> timeZoneModels = new ArrayList<>();
    private List<TimeZoneModel> timeZoneModels2 = new ArrayList<>();

    private IOnItemClick iOnItemClick;

    private TimeWorldDAO timeWorldDAO;

    public void onItemClick(IOnItemClick iOnItemClick) {
        this.iOnItemClick = iOnItemClick;
    }

    public timeZoneAdapter(Context context) {
        this.context = context;
        timeWorldDAO = new TimeWorldDAO(context);
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
            List<TimeZoneModel> list = timeWorldDAO.getAllTimeWorld();
            List<String> listName = new ArrayList<>();
            for(TimeZoneModel model : list){
                listName.add(model.getName());
            }
            if(!listName.contains(timeZoneModel.getName())){
                TimeZoneModel model = new TimeZoneModel(timeZoneModel.getName(), timeZoneModel.getTimezone());
                timeWorldDAO.insert(model);
                List<TimeZoneModel> times =  timeWorldDAO.getAllTimeWorld();
                internationalFragment.adapter.setData(times,false);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                internationalFragment.binding.recycleViewInternational.setLayoutManager(linearLayoutManager);
                internationalFragment.binding.recycleViewInternational.setAdapter(internationalFragment.adapter);
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
