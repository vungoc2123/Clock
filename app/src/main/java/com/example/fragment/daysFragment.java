package com.example.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adapter.daysAdapter;
import com.example.adapter.toneAlarmAdapter;
import com.example.clock.R;
import com.example.clock.databinding.FragmentAddAlarmBinding;
import com.example.clock.databinding.FragmentDaysBinding;
import com.example.clock.databinding.FragmentToneAlarmBinding;
import com.example.database.AlarmDAO;
import com.example.model.AlarmModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class daysFragment extends Fragment {

    private daysAdapter adapter;
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
        int targetHeight = (int) (screenHeight * 1); // 92% của chiều cao màn hình

        List<String> days = new ArrayList<>();
        days.add(activity.getString(R.string.every_monday));
        days.add(activity.getString(R.string.every_tuesday));
        days.add(activity.getString(R.string.every_wednesday));
        days.add(activity.getString(R.string.every_thursday));
        days.add(activity.getString(R.string.every_friday));
        days.add(activity.getString(R.string.every_saturday));
        days.add(activity.getString(R.string.every_sunday));

        adapter = new daysAdapter(activity);
        adapter.setData(days);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        binding.recycleViewDay.setLayoutManager(linearLayoutManager);
        binding.recycleViewDay.setAdapter(adapter);

        binding.tvDaysCancel.setOnClickListener(view1 -> {
            sheetDialog.cancel();
        });
        layout.setMinimumHeight(targetHeight);
        sheetDialog.show();
    }

}