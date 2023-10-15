package com.example.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.clock.R;
import com.example.clock.databinding.FragmentAddAlarmBinding;
import com.example.clock.databinding.FragmentAlarmBinding;
import com.example.database.AlarmDAO;
import com.example.model.AlarmModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;


public class addAlarmFragment extends Fragment {
    public static FragmentAddAlarmBinding binding;
    private AlarmDAO alarmDAO;

    public addAlarmFragment() {
        // Required empty public constructor
    }

    public static addAlarmFragment newInstance(String param1, String param2) {
        addAlarmFragment fragment = new addAlarmFragment();
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
        binding = FragmentAddAlarmBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tpAlarm.setIs24HourView(true);
    }

    public void showBottomSheet(Activity activity) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(activity);
        BottomSheetBehavior<View> bottomSheetBehavior;
        binding = FragmentAddAlarmBinding.inflate(LayoutInflater.from(activity));
        View view = binding.getRoot();
        sheetDialog.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        CoordinatorLayout layout = sheetDialog.findViewById(R.id.bottomSheet_addAlarm);
        assert layout != null;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int targetHeight = (int) (screenHeight * 0.92); // 95% của chiều cao màn hình

        layout.setMinimumHeight(targetHeight);
        sheetDialog.show();

        Calendar calendar = Calendar.getInstance();
        alarmDAO = new AlarmDAO(activity);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        binding.tpAlarm.setIs24HourView(true);
        binding.tpAlarm.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, binding.tpAlarm.getHour());
            calendar.set(Calendar.MINUTE, binding.tpAlarm.getMinute());
        });
        binding.linearAlarmRepeat.setOnClickListener(view1 -> {
            daysFragment bottomSheetFragment = new daysFragment();
            bottomSheetFragment.showBottomSheet(activity);
        });
        binding.linearAlarmTone.setOnClickListener(view1 -> {
            toneAlarmFragment  bottomSheetFragment = new toneAlarmFragment();
            bottomSheetFragment.showBottomSheet(activity);
        });
        binding.tvAlarmSave.setOnClickListener(view1 -> {
            int repeat = binding.switchAlarmAgain.isChecked() ? 1 : 0;
            AlarmModel alarmModel = new AlarmModel();
            alarmModel.setLabel(binding.edtAlarmLabel.getText().toString().isEmpty() ? "Alarm" : binding.edtAlarmLabel.getText().toString());
            alarmModel.setTime(dateFormat.format(calendar.getTime()));
            alarmModel.setRepeatDays(binding.tvAlarmRepeatDays.getText().toString());
            alarmModel.setSound(binding.tvAlarmTone.getText().toString());
            alarmModel.setStatus(1);
            alarmModel.setRepeat(repeat);
            int kq = alarmDAO.insertAlarm(alarmModel);
            if (kq > 0) {
                Toast.makeText(activity, "Thành công", Toast.LENGTH_SHORT).show();
            }
            List<AlarmModel> list = alarmDAO.getAllAlarm();
            alarmFragment.sortList(list);
            alarmFragment.adapter.setData(list, false);
            sheetDialog.cancel();
        });
        binding.tvAlarmCancel.setOnClickListener(view1 -> {
            sheetDialog.cancel();
        });
    }

    public void showDetailAlarm(Activity activity, AlarmModel alarm) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(activity);
        BottomSheetBehavior<View> bottomSheetBehavior;
        binding = FragmentAddAlarmBinding.inflate(LayoutInflater.from(activity));
        View view = binding.getRoot();
        sheetDialog.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        CoordinatorLayout layout = sheetDialog.findViewById(R.id.bottomSheet_addAlarm);
        assert layout != null;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int targetHeight = (int) (screenHeight * 0.92); // 95% của chiều cao màn hình
        layout.setMinimumHeight(targetHeight);
        binding.tpAlarm.setIs24HourView(true);
        sheetDialog.show();

        Calendar calendar = Calendar.getInstance();
        alarmDAO = new AlarmDAO(activity);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String[] parts = alarm.getTime().split(":");

        binding.tpAlarm.setHour(Integer.parseInt(parts[0]));
        binding.tpAlarm.setMinute(Integer.parseInt(parts[1]));

        binding.edtAlarmLabel.setText(alarm.getLabel());
        binding.switchAlarmAgain.setChecked(alarm.getRepeat() == 1 ? true : false);
        binding.tvAlarmRepeatDays.setText(alarm.getRepeatDays());
        binding.tvAlarmTone.setText(alarm.getSound());
        binding.tvAlarmDelete.setVisibility(View.VISIBLE);

        binding.tpAlarm.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, binding.tpAlarm.getHour());
            calendar.set(Calendar.MINUTE, binding.tpAlarm.getMinute());
        });
        binding.tvAlarmRepeatDays.setOnClickListener(view1 -> {


        });

        binding.tvAlarmSave.setOnClickListener(view1 -> {
            int repeat = binding.switchAlarmAgain.isChecked() ? 1 : 0;
            AlarmModel alarmModel = new AlarmModel();
            alarmModel.setId(alarm.getId());
            alarmModel.setLabel(binding.edtAlarmLabel.getText().toString().isEmpty() ? "Alarm" : binding.edtAlarmLabel.getText().toString());
            alarmModel.setTime(dateFormat.format(calendar.getTime()));
            alarmModel.setRepeatDays(binding.tvAlarmRepeatDays.getText().toString());
            alarmModel.setSound(binding.tvAlarmTone.getText().toString());
            alarmModel.setStatus(1);
            alarmModel.setRepeat(repeat);
            try {
                int kq = alarmDAO.updateAlarm(alarmModel);
                if (kq > 0) {
                    Toast.makeText(activity, "Thành công", Toast.LENGTH_SHORT).show();
                    List<AlarmModel> list = alarmDAO.getAllAlarm();
                    alarmFragment.sortList(list);
                    alarmFragment.adapter.setData(list, false);
                    sheetDialog.cancel();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        binding.tvAlarmDelete.setOnClickListener(view1 -> {
            int kq = alarmDAO.delete(alarm.getId());
            if (kq > 0) {
                Toast.makeText(activity, "Thành công", Toast.LENGTH_SHORT).show();
                List<AlarmModel> list = alarmDAO.getAllAlarm();
                alarmFragment.sortList(list);
                alarmFragment.adapter.setData(list, false);
                sheetDialog.cancel();
            }
        });
        binding.tvAlarmCancel.setOnClickListener(view1 -> {
            sheetDialog.cancel();
        });
    }


}