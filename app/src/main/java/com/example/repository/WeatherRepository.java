package com.example.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.API.ApiService;
import com.example.API.WeatherService;
import com.example.model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    WeatherService weatherService = ApiService.getService();
    public LiveData<String> getCity(String nameCity) {
        MutableLiveData<String> data = new MutableLiveData<>();
        Call<ResponseBody> call = weatherService.getCity(nameCity, "rK3WpFTcVXveEoPOG8xZOiCpjasZiAg8");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        String json = response.body().string();
                        JSONArray jsonArray = new JSONArray(json);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        data.setValue(jsonObject.getString("Key")+"-"+jsonObject.getString("EnglishName"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        return data;
    }
    public LiveData<WeatherModel> getCurrentConditions(String locationKey,String name) {
        MutableLiveData<WeatherModel> data = new MutableLiveData<>();
        Call<ResponseBody> call = weatherService.getCurrentConditions(locationKey, "rK3WpFTcVXveEoPOG8xZOiCpjasZiAg8", "en-us", true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        String json = response.body().string();
                        JSONArray jsonArray = new JSONArray(json);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        WeatherModel weatherModel = new WeatherModel(name,jsonObject.getJSONObject("Temperature").getJSONObject("Imperial").getInt("Value"),jsonObject.getString("WeatherText"),jsonObject.getInt("WeatherIcon"));
                        data.setValue(weatherModel);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        return data;
    }

    public LiveData<List<WeatherModel>> getForecast12Hour(String locationKey) {
        MutableLiveData<List<WeatherModel>> data = new MutableLiveData<>();
        Call<ResponseBody> call = weatherService.getForecast12Hour(locationKey, "rK3WpFTcVXveEoPOG8xZOiCpjasZiAg8");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        String json = response.body().string();
                        JSONArray jsonArray = new JSONArray(json);
                        List<WeatherModel> weatherModels = new ArrayList<>();
                        for(int i =0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            WeatherModel weatherModel = new WeatherModel(jsonObject.getJSONObject("Temperature").getInt("Value"),jsonObject.getInt("WeatherIcon"),jsonObject.getString("DateTime"));
                            weatherModels.add(weatherModel);
                        }
                        data.setValue(weatherModels);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        return data;
    }

    public LiveData<List<WeatherModel>> getForecast5Days(String locationKey) {
        MutableLiveData<List<WeatherModel>> data = new MutableLiveData<>();
        Call<ResponseBody> call = weatherService.getForecast5Days(locationKey, "rK3WpFTcVXveEoPOG8xZOiCpjasZiAg8");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        String json = response.body().string();

                        List<WeatherModel> weatherModels = new ArrayList<>();

                        JSONObject jsonObject =new JSONObject(json);
                        JSONArray dailyForecastsArray = jsonObject.getJSONArray("DailyForecasts");
                        for(int i =0;i<dailyForecastsArray.length();i++){
                            JSONObject temperatureObject = dailyForecastsArray.getJSONObject(i).getJSONObject("Temperature");
                            JSONObject minimumObject = temperatureObject.getJSONObject("Minimum");
                            int minTemperature = minimumObject.getInt("Value");
                            JSONObject maximumObject = temperatureObject.getJSONObject("Maximum");
                            int maxTemperature = maximumObject.getInt("Value");
                            int averageTemperature = (minTemperature + maxTemperature) / 2;
                            WeatherModel weatherModel = new WeatherModel(averageTemperature,dailyForecastsArray.getJSONObject(i).getJSONObject("Day").getInt("Icon"),dailyForecastsArray.getJSONObject(i).getString("Date"));
                            weatherModels.add(weatherModel);
                        }
                        data.setValue(weatherModels);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        return data;
    }
}