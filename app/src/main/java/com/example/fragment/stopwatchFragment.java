package com.example.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
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
    private CountDownTimer countDownTimer;
    private long time;
    private long time2;
    private stopWatchAdapter adapter;
    private boolean isStart = false;
    private List<String> times;
    private int index =1;
    private long timeinitial = Long.MAX_VALUE;
    private long timeRemainning = 0;
    private long timeRemainning2 = 0;
    private long timeRemainning3 = 0;

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
            if(!isStart){
                startTime();
                isStart = !isStart;
                setPause();
            }else{
                binding.tvStopWatchRing.setText("Ring");
                stopTime();
                isStart = false;
                timeRemainning = timeRemainning2;
                setResume();
            }
        });
        binding.cvStopWatchRing.setOnClickListener(view1 -> {
            if(!isStart){
                setStart();
            }else{
                time2 = 0;
                timeRemainning3 = timeRemainning2;
                times.add(binding.tvItemStopWatchTime.getText().toString());
                Pair<Long, Long> minMaxPair = findMinMax(times);
                adapter.setData(times,minMaxPair.first,minMaxPair.second);
                binding.recycleViewStopWatch.scrollToPosition(times.size() - 1);
                index++;
            }
        });
    }
    private void fetchData(){
        times = new ArrayList<>();
        adapter = new stopWatchAdapter(getActivity());
        adapter.setData(times,0L,0L);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        binding.recycleViewStopWatch.setLayoutManager(linearLayoutManager);
        binding.recycleViewStopWatch.setAdapter(adapter);
        binding.recycleViewStopWatch.scrollToPosition(times.size() - 1);
        binding.recycleViewStopWatch.setNestedScrollingEnabled(false);
    }
    private void startTime(){
        countDownTimer = new CountDownTimer(timeinitial,1) {
            @Override
            public void onTick(long l) {
                time = timeinitial + timeRemainning - l;
                timeRemainning2 = timeinitial + timeRemainning - l ;
                long minutes = (time / 1000) / 60;
                long seconds = (time / 1000) % 60;
                long milliseconds = (time % 1000)/10;

                binding.tvStopwatchTime.setText(String.format("%02d:%02d,%02d", minutes, seconds, milliseconds));
                time2 = timeinitial + timeRemainning - l - timeRemainning3;

                long minutes2 = (time2 / 1000) / 60;
                long seconds2 = (time2 / 1000) % 60;
                long milliseconds2 = (time2 % 1000)/10;
                binding.tvItemStopWatchRing.setText("Ring "+index);
                binding.tvItemStopWatchTime.setText(String.format("%02d:%02d,%02d", minutes2, seconds2, milliseconds2));
            }
            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }
    private void stopTime(){
        countDownTimer.cancel();
    }
    private void setPause() {
        binding.linearStopWatch.setVisibility(View.VISIBLE);
        binding.tvStopWatchRing.setText("Ring");
        binding.cvStopWatchRing.setClickable(true);
        binding.tvStopWatchRing.setAlpha(1f);
        binding.tvStopWatchStart.setText("Pause");
        binding.tvStopWatchStart.setTextColor(Color.parseColor("#ad2f2f"));
        binding.cvStopWatchStart.getBackground().setTint(Color.parseColor("#340d0c"));
        binding.cvStopWatchStart.setRadius(110);
        binding.viewStopWatchStart.setBackgroundResource(R.drawable.circle_border_red);
    }
    private void setStart(){
        times.clear();
        adapter.setData(times,0L,0L);
        binding.linearStopWatch.setVisibility(View.GONE);
        index = 1;
        time = 0;
        time2 = 0;
        timeRemainning =0;
        timeRemainning3 =0;
        binding.tvStopwatchTime.setText("00:00,00");
        binding.tvStopWatchRing.setAlpha(0.5f);
        binding.cvStopWatchRing.setClickable(false);
        binding.tvStopWatchStart.setText("Start");
        binding.tvStopWatchStart.setTextColor(Color.parseColor("#24B13B"));
        binding.cvStopWatchStart.setRadius(110);
        binding.cvStopWatchStart.getBackground().setTint(Color.parseColor("#0b2c11"));
        binding.cvStopWatchStart.setBackgroundResource(R.drawable.circle_border_green);
    }

    private void setResume(){
        binding.tvStopWatchRing.setText("Reset");
        binding.tvStopWatchStart.setText("Resume");
        binding.tvStopWatchStart.setTextColor(Color.parseColor("#24B13B"));
        binding.cvStopWatchStart.setRadius(110);
        binding.cvStopWatchStart.getBackground().setTint(Color.parseColor("#0b2c11"));
        binding.viewStopWatchStart.setBackgroundResource(R.drawable.circle_border_green);
    }
    public Pair<Long, Long> findMinMax(List<String> list) {
        if (list == null || list.isEmpty()) {
            // Trả về giá trị mặc định hoặc xử lý theo ý bạn nếu danh sách trống hoặc null
            return new Pair<>(0L, 0L);
        }
        String timeStr = times.get(0).replaceAll("\\D", "");
        long min = Long.parseLong(timeStr); // Giả sử phần tử đầu tiên là min
        long max = Long.parseLong(timeStr); // Giả sử phần tử đầu tiên là max

        for (int i = 1; i < list.size(); i++) {
            String timeStr2 = times.get(i).replaceAll("\\D", "");
            long current = Long.parseLong(timeStr2);
            if (current < min) {
                min = current;
            }
            if (current > max) {
                max = current;
            }
        }

        return new Pair<>(min, max);
    }
}