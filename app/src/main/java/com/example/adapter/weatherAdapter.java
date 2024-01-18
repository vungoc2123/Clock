package com.example.adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.clock.R;
import com.example.clock.databinding.ItemWeatherBinding;
import com.example.config.SvgSoftwareLayerSetter;
import com.example.model.WeatherModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class weatherAdapter extends RecyclerView.Adapter<weatherAdapter.weatherHolder> {
    private Context context;
    private List<WeatherModel> weatherModels = new ArrayList<>();

    public weatherAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<WeatherModel> list){
        this.weatherModels = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public weatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new weatherAdapter.weatherHolder(LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull weatherHolder holder, int position) {
        WeatherModel weatherModel = weatherModels.get(position);
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date date = inputDateFormat.parse(weatherModel.getDateTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String outputTimeString = getDayOfWeekString(calendar.get(Calendar.DAY_OF_WEEK));
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
        private ItemWeatherBinding binding;
        public weatherHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemWeatherBinding.bind(itemView);
        }
    }
    public String getDayOfWeekString(int dayOfWeek) {
        String[] daysOfWeek = {
                context.getString(R.string.sunday),
                context.getString(R.string.monday),
                context.getString(R.string.tuesday),
                context.getString(R.string.wednesday),
                context.getString(R.string.thursday),
                context.getString(R.string.friday),
                context.getString(R.string.saturday)
        };
        return daysOfWeek[dayOfWeek - 1];
    }
}
