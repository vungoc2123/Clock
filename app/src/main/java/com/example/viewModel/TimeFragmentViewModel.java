package com.example.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.repository.TimerRepository;

public class TimeFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<String> timerMutableLiveData;
    private MutableLiveData<Integer> progressMutableLiveData;

    public TimeFragmentViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<String> getTimer(){
        if(timerMutableLiveData == null){
            timerMutableLiveData = new MutableLiveData<>();
        }
        TimerRepository.getInstance().getData().observeForever(timer -> {
            timerMutableLiveData.setValue(timer);
        });
        return timerMutableLiveData;
    }
    public MutableLiveData<Integer> getProgress(){
        if(progressMutableLiveData == null){
            progressMutableLiveData = new MutableLiveData<>();
        }
        TimerRepository.getInstance().getProgress().observeForever(progress -> {
            progressMutableLiveData.setValue(progress);
        });
        return progressMutableLiveData;
    }
}
