package com.example.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.alarmAdapter;
import com.example.adapter.internationalTimeAdapter;
import com.example.clock.R;
import com.example.clock.databinding.FragmentAddAlarmBinding;
import com.example.clock.databinding.FragmentAlarmBinding;
import com.example.clock.databinding.FragmentInternationalBinding;
import com.example.database.AlarmDAO;
import com.example.database.TimeZoneDAO;
import com.example.model.AlarmModel;
import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class alarmFragment extends Fragment {
    private FragmentAlarmBinding binding;
    private AlarmDAO alarmDAO;
    public static alarmAdapter adapter;
    private List<AlarmModel> list;
    private boolean check =false;

    public alarmFragment() {
        // Required empty public constructor
    }

    public static alarmFragment newInstance(String param1, String param2) {
        alarmFragment fragment = new alarmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        adapter = new alarmAdapter(getActivity());
        alarmDAO = new AlarmDAO(getActivity());
        binding.imgAlarmAdd.setOnClickListener(view1 -> {
            check = false;
            binding.tvAlarmEdit.setText(getString(R.string.edit));
            Handler handler = new Handler();
            handler.postDelayed(delayedRunnable, 500);
            addAlarmFragment fragment = new addAlarmFragment();
            fragment.showBottomSheet(requireActivity());
        });

        binding.tvAlarmEdit.setOnClickListener(view1 -> {
            check = !check;
            if(check){
                binding.tvAlarmEdit.setText(getString(R.string.done));
            }else{
                binding.tvAlarmEdit.setText(getString(R.string.edit));
            }
            fetchData();
        });
        fetchData();
        adapter.onItemClick(alarmModel -> {
            check = false;
            Handler handler = new Handler();
            handler.postDelayed(delayedRunnable, 500);
            addAlarmFragment fragment = new addAlarmFragment();
            fragment.showDetailAlarm(requireActivity(),alarmModel);
            binding.tvAlarmEdit.setText("Edit");
        });
    }
    Runnable delayedRunnable = new Runnable() {
        @Override
        public void run() {
            fetchData();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    public void fetchData() {
        list.clear();
        list = alarmDAO.getAllAlarm();
        list = sortList(list);
        adapter.setData(list,check);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recycleViewAlarm.setLayoutManager(linearLayoutManager);
        binding.recycleViewAlarm.setAdapter(adapter);
    }

    public static List<AlarmModel> sortList(List<AlarmModel> list) {
        Collections.sort(list, Comparator.comparingInt(alarm -> {
            String timeStr = alarm.getTime().replaceAll("\\D", "");
            return Integer.parseInt(timeStr);
        }));
        return list;
    }
}