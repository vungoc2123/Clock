package com.example.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.example.clock.R;
import com.example.clock.databinding.FragmentTimerBinding;
import com.example.broadcast.MyBroadcast;
import com.example.config.ConfigString;
import com.example.service.TimerService;
import com.example.viewModel.TimeFragmentViewModel;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

public class timerFragment extends Fragment {
    private long timeSelected = 0;
    private boolean isStart = false;
    private int total;
    public static FragmentTimerBinding binding;
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
        getFirstRingTone();
        binding.cvTimerReset.setEnabled(false);
        binding.cvTimerStart.setEnabled(false);
        binding.tvTimerCancel.setAlpha(0.3f);
        binding.tvTimerStart.setAlpha(0.3f);


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

        binding.cvTimerStart.setOnClickListener(view1 -> {
            if (!isStart) {
                setPause();
                isStart = !isStart;
                binding.pbTimer.setMax(total);
                Intent intent = new Intent(getContext(), TimerService.class);
                intent.putExtra(ConfigString.timeSelected, timeSelected);
                intent.putExtra(ConfigString.sound, binding.tvTimerToneAlarm.getText().toString());
                requireActivity().startService(intent);
            } else {
                setResume();
                isStart = !isStart;
                TimerService.countDownTimer.cancel();
            }
        });

        binding.cvTimerReset.setOnClickListener(view1 -> {
            setStart();
            resetTimer();
            TimerService.countDownTimer.cancel();
        });

        binding.linearAlarmTone.setOnClickListener(view1 -> {
            toneAlarmFragment  bottomSheetFragment = new toneAlarmFragment(ConfigString.timerFragment);
            bottomSheetFragment.showBottomSheet(getActivity());
        });

        setNumberPicker(binding.hourPicker,23);
        setNumberPicker(binding.minutePicker,59);
        setNumberPicker(binding.secondPicker,59);
    }

    private void getFirstRingTone(){
        RingtoneManager ringtoneManager = new RingtoneManager(getActivity());
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            binding.tvTimerToneAlarm.setText(title);
            break;
        }
    }


    private void resetTimer() {
        int hour = binding.hourPicker.getValue();
        int minute = binding.minutePicker.getValue();
        int second = binding.secondPicker.getValue();
        timeSelected = (hour * 3600000) + (minute * 60000) + (second * 1000) ;
        binding.pbTimer.setProgress((int) timeSelected);
        isStart = false;
    }

    private void setPause() {
        binding.frameTimer.setVisibility(View.VISIBLE);
        binding.linearTimer.setVisibility(View.GONE);
        binding.cvTimerStart.setEnabled(true);
        binding.tvTimerStart.setAlpha(1f);
        binding.cvTimerReset.setEnabled(true);
        binding.tvTimerCancel.setAlpha(1f);
        binding.tvTimerStart.setText(getString(R.string.pause));
        binding.tvTimerStart.setTextSize(16);
        binding.tvTimerStart.setTextColor(ContextCompat.getColor(getActivity(), R.color.yellow_text));
        binding.cvTimerStart.getBackground().setTint(ContextCompat.getColor(getActivity(), R.color.yellow_bg));
        binding.viewTimerStart.setBackgroundResource(R.drawable.circle_border_yellow);
    }

    private void setStart() {
        binding.frameTimer.setVisibility(View.GONE);
        binding.linearTimer.setVisibility(View.VISIBLE);
        binding.tvTimerTime.setText("00:00, 00");
        binding.tvTimerCancel.setAlpha(0.5f);
        binding.cvTimerReset.setEnabled(false);
        binding.tvTimerStart.setTextSize(18);
        binding.tvTimerStart.setText(getString(R.string.start));
        binding.tvTimerStart.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
        binding.cvTimerStart.getBackground().setTint(ContextCompat.getColor(getActivity(), R.color.green_bg));
        binding.viewTimerStart.setBackgroundResource(R.drawable.circle_border_green);
    }

    private void setResume() {
        binding.tvTimerStart.setText(getString(R.string.resume));
        binding.tvTimerStart.setTextSize(18);
        binding.tvTimerStart.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
        binding.cvTimerStart.getBackground().setTint(ContextCompat.getColor(getActivity(), R.color.green_bg));
        binding.viewTimerStart.setBackgroundResource(R.drawable.circle_border_green);
    }

    private void setNumberPicker(NumberPicker numberPicker, int max){
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(max);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%01d", i);
            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int hour = binding.hourPicker.getValue();
                int minute = binding.minutePicker.getValue();
                int second = binding.secondPicker.getValue();
                timeSelected = (hour * 3600000) + (minute * 60000) + (second * 1000) ;
                total = (int) timeSelected;
                if (binding.hourPicker.getValue() == 0 && binding.minutePicker.getValue() == 0  && binding.secondPicker.getValue() == 0) {
                    binding.tvTimerStart.setAlpha(0.3f);
                    binding.cvTimerStart.setEnabled(false);
                } else {
                    binding.tvTimerStart.setAlpha(1f);
                    binding.cvTimerStart.setEnabled(true);
                }
            }
        });
    }

}