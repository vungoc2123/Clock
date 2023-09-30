package com.example.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.internationalTimeAdapter;
import com.example.callback.SwipeToDeleteCallBack;
import com.example.clock.databinding.FragmentInternationalBinding;
import com.example.database.TimeZoneDAO;
import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class internationalFragment extends Fragment {

    private TimeZoneDAO timeZoneDAO;

    public static internationalTimeAdapter adapter;

    private List<TimeZoneModel> list;
    private FragmentInternationalBinding binding;
    private Boolean check= false;


    public internationalFragment() {
        // Required empty public constructor
    }


    public static internationalFragment newInstance(String param1, String param2) {
        internationalFragment fragment = new internationalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInternationalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
        binding.imgItnAdd.setOnClickListener(view1 -> {
            listTimeZoneFragment bottomSheetFragment = new listTimeZoneFragment();
            bottomSheetFragment.showBottomSheet(requireActivity());
        });

        binding.tvItnEdit.setOnClickListener(view1 -> {
            check = !check;
            fetchData();
        });
    }

    public void fetchData() {
        list = new ArrayList<>();
        adapter = new internationalTimeAdapter(getActivity());
        SwipeToDeleteCallBack callback = new SwipeToDeleteCallBack(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recycleViewInternational);
        timeZoneDAO = new TimeZoneDAO(getActivity());
        list = timeZoneDAO.getAllTimeZone();
        list = filterList(list);
        adapter.setData(list,check);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recycleViewInternational.setLayoutManager(linearLayoutManager);
        binding.recycleViewInternational.setAdapter(adapter);
    }

    public static List<TimeZoneModel> filterList(List<TimeZoneModel> list) {
        List<TimeZoneModel> times = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() !=0) {
                times.add(list.get(i));
            }
        }
        Collections.sort(times, Comparator.comparingInt(TimeZoneModel::getStatus));
        return times;
    }

}