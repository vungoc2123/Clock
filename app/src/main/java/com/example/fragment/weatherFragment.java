package com.example.fragment;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.adapter.weatherAdapter;
import com.example.adapter.weatherHorAdapter;
import com.example.clock.R;
import com.example.clock.databinding.FragmentWeatherBinding;
import com.example.config.SvgSoftwareLayerSetter;
import com.example.viewModel.WeatherFragmentViewModel;

public class weatherFragment extends Fragment {

    private WeatherFragmentViewModel viewModel;
    private FragmentWeatherBinding binding;

    public weatherFragment() {
        // Required empty public constructor
    }

    public static weatherFragment newInstance(String param1, String param2) {
        weatherFragment fragment = new weatherFragment();
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
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel =  new ViewModelProvider(requireActivity()).get(WeatherFragmentViewModel.class);
        fetchData("hanoi");
        binding.imgWeatherSearch.setOnClickListener(view1 -> {
            if(binding.edtWeatherSearch.getText().toString().isEmpty()){
                Toast.makeText(requireActivity(), "Không để trống", Toast.LENGTH_SHORT).show();
            }else{
                fetchData(binding.edtWeatherSearch.getText().toString());
            }
        });
    }
    private void fetchData(String name){
        viewModel.getCity(name).observe(getViewLifecycleOwner(),CodeCity ->{
            String[] part = CodeCity.split("-");
            viewModel.getCurrentConditions(part[0],part[1]).observe(getViewLifecycleOwner(),CurrentConditions ->{
                binding.tvWeatherLocation.setText(CurrentConditions.getLocation());
                binding.tvWeatherTemperature.setText(String.format("%.1f",(float)(CurrentConditions.getTemperature()-32)/1.8));
                binding.tvWeatherDetail.setText(CurrentConditions.getWeatherCondition());
                RequestBuilder<PictureDrawable> requestBuilder =
                        Glide.with(this)
                                .as(PictureDrawable.class)
                                .error(R.drawable.background)
                                .transition(withCrossFade())
                                .override(200, 200)
                                .listener(new SvgSoftwareLayerSetter());
                requestBuilder.load("https://www.accuweather.com/images/weathericons/"+CurrentConditions.getIconWeather()+".svg").into(binding.imgWeather);
            });
            viewModel.getForecast12Hours(part[0]).observe(getViewLifecycleOwner(), Forecast12Hours ->{
                weatherHorAdapter adapter = new weatherHorAdapter(requireActivity());
                adapter.setData(Forecast12Hours);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
                binding.recycleViewForecast12hour.setLayoutManager(linearLayoutManager);
                binding.recycleViewForecast12hour.setAdapter(adapter);
            });

            viewModel.getForecast5Days(part[0]).observe(getViewLifecycleOwner(), Forecast5Days ->{
                weatherAdapter adapter = new weatherAdapter(requireActivity());
                adapter.setData(Forecast5Days);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
                binding.recycleViewForecast5days.setLayoutManager(linearLayoutManager);
                binding.recycleViewForecast5days.setAdapter(adapter);
            });

        });
    }

}