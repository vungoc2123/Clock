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
import android.widget.Toast;

import com.example.clock.R;
import com.example.clock.databinding.FragmentAddAlarmBinding;
import com.example.clock.databinding.FragmentDaysBinding;
import com.example.database.AlarmDAO;
import com.example.model.AlarmModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;


public class daysFragment extends Fragment {


    private FragmentDaysBinding binding;
    public daysFragment() {
        // Required empty public constructor
    }


    public static daysFragment newInstance(String param1, String param2) {
        daysFragment fragment = new daysFragment();

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
        binding = FragmentDaysBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void showBottomSheet(Activity activity) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(activity);
        BottomSheetBehavior<View> bottomSheetBehavior;
        binding = FragmentDaysBinding.inflate(LayoutInflater.from(activity));
        View view = binding.getRoot();
        sheetDialog.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        CoordinatorLayout layout = sheetDialog.findViewById(R.id.bottomSheet_days);
        assert layout != null;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int targetHeight = (int) (screenHeight * 0.92); // 92% của chiều cao màn hình

        binding.tvDaysCancel.setOnClickListener(view1 -> {
            sheetDialog.cancel();
        });
        binding.tvDaysMonday.setOnClickListener(view1 -> {
            addAlarmFragment.binding.tvAlarmRepeatDays.setText("Monday");
            sheetDialog.cancel();
        });
        binding.tvDaysTuesday.setOnClickListener(view1 -> {
            addAlarmFragment.binding.tvAlarmRepeatDays.setText("Tuesday");
            sheetDialog.cancel();
        });
        binding.tvDaysWednesday.setOnClickListener(view1 -> {
            addAlarmFragment.binding.tvAlarmRepeatDays.setText("Wednesday");
            sheetDialog.cancel();
        });
        binding.tvDaysThursday.setOnClickListener(view1 -> {
            addAlarmFragment.binding.tvAlarmRepeatDays.setText("Thursday");
            sheetDialog.cancel();
        });
        binding.tvDaysFriday.setOnClickListener(view1 -> {
            addAlarmFragment.binding.tvAlarmRepeatDays.setText("Friday");
            sheetDialog.cancel();
        });
        binding.tvDaysSaturday.setOnClickListener(view1 -> {
            addAlarmFragment.binding.tvAlarmRepeatDays.setText("Saturday");
            sheetDialog.cancel();
        });
        binding.tvDaysSunday.setOnClickListener(view1 -> {
            addAlarmFragment.binding.tvAlarmRepeatDays.setText("Sunday");
            sheetDialog.cancel();
        });




        layout.setMinimumHeight(targetHeight);
        sheetDialog.show();
//        sheetDialog.cancel();
    }

}