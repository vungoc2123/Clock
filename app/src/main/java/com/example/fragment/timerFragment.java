package com.example.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clock.R;
import com.example.clock.databinding.FragmentTimerBinding;
import com.example.broadcast.MyBroadcast;
import com.example.service.TimerService;
import com.example.viewModel.TimeFragmentViewModel;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

public class timerFragment extends Fragment {
    private long timeSelected = 0;
    private boolean isStart = false;
    private int total;
    private FragmentTimerBinding binding;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimeFragmentViewModel viewModel;

    public timerFragment() {
        // Required empty public constructor
    }

    public static timerFragment newInstance(String param1, String param2) {
        timerFragment fragment = new timerFragment();
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
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TimeFragmentViewModel.class);

//        SharedPreferences preferences = requireActivity().getSharedPreferences("My_file",Context.MODE_PRIVATE);
//        isStart = preferences.getBoolean("isStart", false);
//        timeSelected = preferences.getLong("timeInitial", 0);
//        total =(int) timeSelected;

//        if(isStart == true){
//            setPause();
//        }else{
        binding.cvTimerReset.setEnabled(false);
        binding.cvTimerStart.setEnabled(false);
        binding.tvTimerCancel.setAlpha(0.3f);
        binding.tvTimerStart.setAlpha(0.3f);
        //}
        viewModel.getTimer().observe(getViewLifecycleOwner(), timer -> {
            binding.tvTimerTime.setText(timer);
        });
        viewModel.getProgress().observe(getViewLifecycleOwner(), progress -> {
            binding.pbTimer.setMax(total);
            binding.pbTimer.setProgress(progress);
            timeSelected = progress;
           if(progress == 0){
               setStart();
               resetTimer();
           }
        });
        binding.tpTimer.setIs24HourView(true);
        binding.tpTimer.setHour(0);
        binding.tpTimer.setMinute(0);
        binding.tpTimer.setOnTimeChangedListener((timePicker, hourTime, minuteTime) -> {
            int minute = binding.tpTimer.getMinute();
            int hour = binding.tpTimer.getHour();
            timeSelected = (hour * 3600000) + (minute * 60000);
            total = (int) timeSelected;
            if (binding.tpTimer.getMinute() == 0 && binding.tpTimer.getHour() == 0) {
                binding.tvTimerStart.setAlpha(0.3f);
                binding.cvTimerStart.setEnabled(false);
            } else {
                binding.tvTimerStart.setAlpha(1f);
                binding.cvTimerStart.setEnabled(true);
            }
        });
        binding.cvTimerStart.setOnClickListener(view1 -> {
            if (!isStart) {
                setPause();
                isStart = !isStart;
                binding.pbTimer.setMax(total);
                Intent intent = new Intent(getContext(), TimerService.class);
                intent.putExtra("timeSelected", timeSelected);
                requireActivity().startService(intent);

            } else {
                setResume();
                isStart = !isStart;
//                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("My_file",Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("timeRemaining", timeRemaining);
//                editor.apply();
                TimerService.countDownTimer.cancel();
            }
        });
        binding.cvTimerReset.setOnClickListener(view1 -> {
            setStart();
            resetTimer();
            TimerService.countDownTimer.cancel();
        });
    }

//    private void startTimer(long time) {
//        countDownTimer = new CountDownTimer(time, 1) {
//            @Override
//            public void onTick(long l) {
//                long totalSeconds = l / 1000;
//                int hours = (int) (totalSeconds / 3600);
//                int minutes = (int) ((totalSeconds % 3600) / 60);
//                int seconds = (int) (totalSeconds % 60);
//                int hour = binding.tpTimer.getHour();
//                if(hour>0){
//                    binding.tvTimerTime.setText(String.format("%02d:%02d:%02d",hours, minutes, seconds));
//                }else{
//                    binding.tvTimerTime.setText(String.format("%02d:%02d", minutes, seconds));
//                }
//                binding.pbTimer.setMax(total);
//                binding.pbTimer.setProgress(total - timeProgress);
//                timeProgress++;
//                timeSelected = l;
//            }
//            @Override
//            public void onFinish() {
//                binding.tvTimerTime.setText(String.format("%02d:%02d", 0, 0));
//                binding.pbTimer.setProgress(0);
//                setStart();
//                resetTimer();
//                alarm();
//            }
//        };
//        countDownTimer.start();
//    }

    private void stopTimer() {
        //countDownTimer.cancel();
    }

    private void resetTimer() {
        int minute = binding.tpTimer.getMinute();
        int hour = binding.tpTimer.getHour();
        timeSelected = (hour * 3600000) + (minute * 60000);
        binding.pbTimer.setProgress((int) timeSelected);
        isStart = false;
        //countDownTimer.cancel();
    }

    private void setPause() {
        binding.frameTimer.setVisibility(View.VISIBLE);
        binding.linearTimer.setVisibility(View.GONE);
        binding.cvTimerStart.setEnabled(true);
        binding.tvTimerStart.setAlpha(1f);
        binding.cvTimerReset.setEnabled(true);
        binding.tvTimerCancel.setAlpha(1f);
        binding.tvTimerStart.setText("Pause");
        binding.tvTimerStart.setTextColor(Color.parseColor("#FAA009"));
        binding.cvTimerStart.getBackground().setTint(Color.parseColor("#312107"));
        binding.cvTimerStart.setRadius(110);
        binding.viewTimerStart.setBackgroundResource(R.drawable.circle_border_yellow);
    }

    private void setStart() {
        binding.frameTimer.setVisibility(View.GONE);
        binding.linearTimer.setVisibility(View.VISIBLE);
        binding.tvTimerTime.setText("00:00, 00");
        binding.tvTimerCancel.setAlpha(0.5f);
        binding.cvTimerReset.setEnabled(false);
        binding.tvTimerStart.setText("Start");
        binding.tvTimerStart.setTextColor(Color.parseColor("#03FD2A"));
        binding.cvTimerStart.setRadius(110);
        binding.cvTimerStart.getBackground().setTint(Color.parseColor("#0b2c11"));
        binding.viewTimerStart.setBackgroundResource(R.drawable.circle_border_green);
    }

    private void setResume() {
        binding.tvTimerStart.setText("Resume");
        binding.tvTimerStart.setTextColor(Color.parseColor("#03FD2A"));
        binding.cvTimerStart.setRadius(110);
        binding.cvTimerStart.getBackground().setTint(Color.parseColor("#0b2c11"));
        binding.viewTimerStart.setBackgroundResource(R.drawable.circle_border_green);
    }

}