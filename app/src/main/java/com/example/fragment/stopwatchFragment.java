package com.example.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.stopWatchAdapter;
import com.example.clock.R;
import com.example.clock.databinding.FragmentStopwatchBinding;

import java.util.ArrayList;
import java.util.List;


public class stopwatchFragment extends Fragment {

    private FragmentStopwatchBinding binding;
    private stopWatchAdapter adapter;
    private boolean isStart = false;
    private List<String> times;
    private int index = 1;
    private Handler handler = new Handler();
    private long time = 0;
    private long timeItem = 0;
    private final int delayMillis = 10;
    private final int interval = 10;
    public stopwatchFragment() {
        // Required empty public constructor
    }

    public static stopwatchFragment newInstance(String param1, String param2) {
        stopwatchFragment fragment = new stopwatchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStopwatchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
        binding.cvStopWatchRing.setClickable(false);
        binding.tvStopWatchRing.setAlpha(0.3f);
        binding.linearStopWatch.setVisibility(View.GONE);
        binding.cvStopWatchStart.setOnClickListener(view1 -> {
            if (!isStart) {
                startTime();
                isStart = !isStart;
                setPause();
            } else {
                binding.tvStopWatchRing.setText(getString(R.string.lap));
                stopTime();
                isStart = false;
                setResume();
            }
        });
        binding.cvStopWatchRing.setOnClickListener(view1 -> {
            if (!isStart) {
                setStart();
            } else {
                timeItem = 0;
                times.add(binding.tvItemStopWatchTime.getText().toString());
                Pair<Long, Long> minMaxPair = findMinMax(times);
                adapter.setData(times, minMaxPair.first, minMaxPair.second);
                binding.recycleViewStopWatch.scrollToPosition(times.size() - 1);
                index++;
            }
        });
    }

    private void startTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time += interval;
                long minutes = time / 60000;
                long seconds = (time % 60000) / 1000;
                long milliseconds = (time % 1000) / 10;
                if(minutes >= 100){
                    binding.tvStopwatchTime.setTextSize(70);
                }
                binding.tvStopwatchTime.setText(String.format("%02d:%02d,%02d", minutes, seconds, milliseconds));
                timeItem += interval;
                long minutesItem = timeItem / 60000;
                long secondsItem = (timeItem % 60000) / 1000;
                long millisecondsItem = (timeItem % 1000) / 10;
                binding.tvItemStopWatchRing.setText(getString(R.string.lap)+ " "+ index);
                binding.tvItemStopWatchTime.setText(String.format("%02d:%02d,%02d", minutesItem, secondsItem, millisecondsItem));
                startTime();
            }
        }, delayMillis);
    }

    private void fetchData() {
        times = new ArrayList<>();
        adapter = new stopWatchAdapter(getActivity());
        adapter.setData(times, 0L, 0L);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        binding.recycleViewStopWatch.setLayoutManager(linearLayoutManager);
        binding.recycleViewStopWatch.setAdapter(adapter);
        binding.recycleViewStopWatch.scrollToPosition(times.size() - 1);
        binding.recycleViewStopWatch.setNestedScrollingEnabled(false);
    }

    private void stopTime() {
        handler.removeCallbacksAndMessages(null);
    }

    private void setPause() {
        binding.linearStopWatch.setVisibility(View.VISIBLE);
        binding.tvStopWatchRing.setText(getString(R.string.lap));
        binding.cvStopWatchRing.setClickable(true);
        binding.tvStopWatchRing.setAlpha(1f);
        binding.tvStopWatchStart.setText(getString(R.string.stop));
        binding.tvStopWatchStart.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
        binding.cvStopWatchStart.getBackground().setTint(ContextCompat.getColor(getActivity(), R.color.red_bg));
        binding.viewStopWatchStart.setBackgroundResource(R.drawable.circle_border_red);
    }

    private void setStart() {
        times.clear();
        adapter.setData(times, 0L, 0L);
        binding.linearStopWatch.setVisibility(View.GONE);
        index = 1;
        time = 0;
        timeItem =0;
        binding.tvStopwatchTime.setText("00:00,00");
        binding.tvStopWatchRing.setAlpha(0.5f);
        binding.cvStopWatchRing.setClickable(false);
        binding.tvStopWatchStart.setText(getString(R.string.start));
        binding.tvStopWatchStart.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
        binding.cvStopWatchStart.getBackground().setTint(ContextCompat.getColor(getActivity(), R.color.green_bg));
        binding.cvStopWatchStart.setBackgroundResource(R.drawable.circle_border_green);
    }

    private void setResume() {
        binding.tvStopWatchRing.setText(getString(R.string.reset));
        binding.tvStopWatchStart.setText(getString(R.string.start));
        binding.tvStopWatchStart.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
        binding.cvStopWatchStart.getBackground().setTint(ContextCompat.getColor(getActivity(), R.color.green_bg));
        binding.viewStopWatchStart.setBackgroundResource(R.drawable.circle_border_green);
    }

    public Pair<Long, Long> findMinMax(List<String> list) {
        if (list == null || list.isEmpty()) {
            return new Pair<>(0L, 0L);
        }
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (String timeStr : list) {
            String cleanTimeStr = timeStr.replaceAll("\\D", "");
            long current = Long.parseLong(cleanTimeStr);
            min = Math.min(min, current);
            max = Math.max(max, current);
        }
        return new Pair<>(min, max);
    }
}