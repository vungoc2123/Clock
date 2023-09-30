package com.example.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.model.WeatherModel;
import com.example.repository.WeatherRepository;

import java.util.List;

public class WeatherFragmentViewModel extends AndroidViewModel {

    private WeatherRepository weatherRepository;
    private MutableLiveData<String> cityMutableLiveData;
    private MutableLiveData<WeatherModel> currentConditionsMutableLiveData;
    private MutableLiveData<List<WeatherModel>> forecast12HoursMutableLiveData;
    private MutableLiveData<List<WeatherModel>> forecast5DaysMutableLiveData;

    public WeatherFragmentViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository();
    }
    public MutableLiveData<String> getCity(String nameCity){
        if(cityMutableLiveData == null){
            cityMutableLiveData = new MutableLiveData<>();
        }
        weatherRepository.getCity(nameCity).observeForever(city -> {
            cityMutableLiveData.setValue(city);
        });
        return cityMutableLiveData;
    }
    public MutableLiveData<WeatherModel> getCurrentConditions(String locationKey, String name){
        if(currentConditionsMutableLiveData == null){
            currentConditionsMutableLiveData = new MutableLiveData<>();
        }
        weatherRepository.getCurrentConditions(locationKey,name).observeForever(CurrentConditions -> {
            currentConditionsMutableLiveData.setValue(CurrentConditions);
        });
        return currentConditionsMutableLiveData;
    }

    public MutableLiveData<List<WeatherModel>> getForecast12Hours(String locationKey){
        if(forecast12HoursMutableLiveData == null){
            forecast12HoursMutableLiveData = new MutableLiveData<>();
        }
        weatherRepository.getForecast12Hour(locationKey).observeForever(forecast12hours -> {
            forecast12HoursMutableLiveData.setValue(forecast12hours);
        });
        return forecast12HoursMutableLiveData;
    }

    public MutableLiveData<List<WeatherModel>> getForecast5Days(String locationKey){
        if(forecast5DaysMutableLiveData == null){
            forecast5DaysMutableLiveData = new MutableLiveData<>();
        }
        weatherRepository.getForecast5Days(locationKey).observeForever(forecast5days -> {
            forecast5DaysMutableLiveData.setValue(forecast5days);
        });
        return forecast5DaysMutableLiveData;
    }

}
