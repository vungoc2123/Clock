package com.example.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adapter.timeZoneAdapter;
import com.example.clock.R;
import com.example.clock.databinding.FragmentListTimezoneBinding;
import com.example.database.TimeZoneDAO;
import com.example.model.TimeZoneModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class listTimeZoneFragment extends BottomSheetDialogFragment{
    private FragmentListTimezoneBinding binding;
    private TimeZoneDAO timeZoneDAO;
    private timeZoneAdapter adapter;
    private List<TimeZoneModel> list;
    BottomSheetDialog sheetDialog ;
    public listTimeZoneFragment() {
        // Required empty public constructor
    }

    public static listTimeZoneFragment newInstance(String param1, String param2) {
        listTimeZoneFragment fragment = new listTimeZoneFragment();
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
        binding = FragmentListTimezoneBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void showBottomSheet(Activity activity){
        sheetDialog = new BottomSheetDialog(activity);
        BottomSheetBehavior<View> bottomSheetBehavior;
        binding = FragmentListTimezoneBinding.inflate(LayoutInflater.from(activity));
        View view = binding.getRoot();
        sheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        CoordinatorLayout layout = sheetDialog.findViewById(R.id.bottomSheet_listCity);
        assert layout != null;

        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int targetHeight = (int) (screenHeight * 0.92); // 95% của chiều cao màn hình
        layout.setMinimumHeight(targetHeight);


        timeZoneDAO = new TimeZoneDAO(activity);
        list = new ArrayList<>();
        list = timeZoneDAO.getAllTimeZone();
        adapter = new timeZoneAdapter(activity);
        adapter.setData(list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        binding.recycleViewTimeZone.setLayoutManager(linearLayoutManager);
        binding.recycleViewTimeZone.setAdapter(adapter);
        adapter.onItemClick(() -> {
            sheetDialog.cancel();
        });

        binding.tvTimeZoneCancel.setOnClickListener(view1 -> {
            sheetDialog.cancel();
        });

        binding.edtTimeZoneSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(binding.edtTimeZoneSearch.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        sheetDialog.show();
    }

}