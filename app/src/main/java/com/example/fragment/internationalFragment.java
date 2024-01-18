package com.example.fragment;

import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.adapter.internationalTimeAdapter;
import com.example.clock.R;
import com.example.clock.databinding.FragmentInternationalBinding;
import com.example.database.TimeWorldDAO;
import com.example.database.TimeZoneDAO;
import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Delayed;


public class internationalFragment extends Fragment {

    private TimeWorldDAO timeWorldDAO;

    public static internationalTimeAdapter adapter;
    public static FragmentInternationalBinding binding;
    private List<TimeZoneModel> list;

    private Boolean check = false;


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
            check = false;
            Handler handler = new Handler();
            binding.tvItnEdit.setText(getString(R.string.edit));
            handler.postDelayed(delayedRunnable, 500);
            listTimeZoneFragment bottomSheetFragment = new listTimeZoneFragment();
            bottomSheetFragment.showBottomSheet(requireActivity());
        });
        binding.tvItnEdit.setOnClickListener(view1 -> {
            check = !check;
            if (check) {
                binding.tvItnEdit.setText(getString(R.string.done));
            } else {
                binding.tvItnEdit.setText(getString(R.string.edit));
            }
            fetchData();
        });
    }

    Runnable delayedRunnable = new Runnable() {
        @Override
        public void run() {
            fetchData();
        }
    };
    public void fetchData() {
        list = new ArrayList<>();
        adapter = new internationalTimeAdapter(getActivity());
        timeWorldDAO = new TimeWorldDAO(getActivity());
        list = timeWorldDAO.getAllTimeWorld();
        adapter.setData(list, check);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recycleViewInternational.setLayoutManager(linearLayoutManager);
        binding.recycleViewInternational.setAdapter(adapter);
    }
}