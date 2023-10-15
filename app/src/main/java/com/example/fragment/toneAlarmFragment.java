package com.example.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.timeZoneAdapter;
import com.example.adapter.toneAlarmAdapter;
import com.example.clock.R;
import com.example.clock.databinding.FragmentDaysBinding;
import com.example.clock.databinding.FragmentToneAlarmBinding;
import com.example.model.ToneAlarmModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link toneAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class toneAlarmFragment extends Fragment {

    private FragmentToneAlarmBinding binding;
    private toneAlarmAdapter adapter;


    public toneAlarmFragment() {
        // Required empty public constructor
    }


    public static toneAlarmFragment newInstance(String param1, String param2) {
        toneAlarmFragment fragment = new toneAlarmFragment();
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
        binding = FragmentToneAlarmBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public void showBottomSheet(Activity activity) {

        RingtoneManager ringtoneManager = new RingtoneManager(activity);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);

        Cursor cursor = ringtoneManager.getCursor();
        List<ToneAlarmModel> toneAlarms = new ArrayList<>();

        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            long id = cursor.getLong(RingtoneManager.ID_COLUMN_INDEX);
            for(int i =0;i<toneAlarms.size();i++){
                if(toneAlarms.get(i).getName().equals(title)){
                    toneAlarms.remove(i);
                }
            }
            ToneAlarmModel toneAlarmModel = new ToneAlarmModel((int) id,title);
            toneAlarms.add(toneAlarmModel);
        }

        BottomSheetDialog sheetDialog = new BottomSheetDialog(activity);
        BottomSheetBehavior<View> bottomSheetBehavior;
        binding = FragmentToneAlarmBinding.inflate(LayoutInflater.from(activity));
        View view = binding.getRoot();
        sheetDialog.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        CoordinatorLayout layout = sheetDialog.findViewById(R.id.bottomSheet_days);
        assert layout != null;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int targetHeight = (int) (screenHeight * 0.92); // 92% của chiều cao màn hình

        adapter = new toneAlarmAdapter(activity);
        adapter.setData(toneAlarms);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        binding.recycleViewToneAlarm.setLayoutManager(linearLayoutManager);
        binding.recycleViewToneAlarm.setAdapter(adapter);
        binding.tvToneAlarmCancel.setOnClickListener(view1 -> {
            sheetDialog.cancel();
        });

        layout.setMinimumHeight(targetHeight);
        sheetDialog.show();
//        sheetDialog.cancel();
    }
}