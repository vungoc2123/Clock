package com.example.adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.clock.R;
import com.example.clock.databinding.ItemWeatherHorBinding;
import com.example.config.SvgSoftwareLayerSetter;
import com.example.model.WeatherModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class weatherHorAdapter extends RecyclerView.Adapter<weatherHorAdapter.weatherHolder> {
    private Context context;
    private List<WeatherModel> weatherModels = new ArrayList<>();

    public weatherHorAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<WeatherModel> list){
        this.weatherModels = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public weatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new weatherHorAdapter.weatherHolder(LayoutInflater.from(context).inflate(R.layout.item_weather_hor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull weatherHolder holder, int position) {
        WeatherModel weatherModel = weatherModels.get(position);
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date date = inputDateFormat.parse(weatherModel.getDateTime());
            holder.binding.tvWeatherTime.setWidth(50);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH:mm");
            String outputTimeString = outputDateFormat.format(date);
            holder.binding.tvWeatherTime.setText(outputTimeString);
            float temperature = (float) ((weatherModel.getTemperature()-32)/1.8);
            holder.binding.tvWeatherTemperature.setText(String.format("%.1f", temperature));
            RequestBuilder<PictureDrawable> requestBuilder =
                    Glide.with(context)
                            .as(PictureDrawable.class)
                            .error(R.drawable.background)
                            .transition(withCrossFade())
                            .override(200, 200)
                            .listener(new SvgSoftwareLayerSetter());
            requestBuilder.load("https://www.accuweather.com/images/weathericons/"+weatherModel.getIconWeather()+".svg").into(holder.binding.imgWeatherIcon);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int getItemCount() {
        return weatherModels.size();
    }


    class weatherHolder extends RecyclerView.ViewHolder {
        private ItemWeatherHorBinding binding;
        public weatherHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemWeatherHorBinding.bind(itemView);
        }
    }
}
