package com.example.repository;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TimerRepository {
    private MutableLiveData<String> data = new MutableLiveData<>();
    private MutableLiveData<Integer> progress = new MutableLiveData<>();

    private static TimerRepository instance = new TimerRepository();
    public static TimerRepository getInstance() {
        return instance;
    }
    public LiveData<String> getData() {
        return data;
    }
    public void setData(String newData) {
        data.setValue(newData);
    }


    public LiveData<Integer> getProgress() {
        return progress;
    }
    public void setProgress(int newProgress) {
        progress.setValue(newProgress);
    }

}
