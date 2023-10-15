package com.example.clock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;

import com.example.broadcast.MyBroadcast;
import com.example.clock.databinding.ActivityMainBinding;
import com.example.fragment.alarmFragment;
import com.example.fragment.internationalFragment;
import com.example.fragment.stopwatchFragment;
import com.example.fragment.timerFragment;
import com.example.fragment.weatherFragment;
import com.example.service.MyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    private Fragment activeFragment = new internationalFragment();
    private internationalFragment international = new internationalFragment();
    private alarmFragment alarm= new alarmFragment();
    private stopwatchFragment stopwatch = new stopwatchFragment();
    private timerFragment timer = new timerFragment();
    private weatherFragment weather = new weatherFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        String nameFragment = getIntent().getStringExtra("nameFragment");
//        System.out.println(nameFragment);
//        if("timerFragment".equals(nameFragment)){
//            replaceFragment(new timerFragment());
//            binding.bottomNavigation.setSelectedItemId(R.id.timer);
//        }else if("alarmFragment".equals(nameFragment)){
//            replaceFragment(new alarmFragment());
//            binding.bottomNavigation.setSelectedItemId(R.id.alarm);
//        }else{
//            replaceFragment(new internationalFragment());
//        }



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(binding.frameLayout.getId(), international, "international");

        transaction.add(binding.frameLayout.getId(), alarm, "alarm");
        transaction.hide(alarm);

        transaction.add(binding.frameLayout.getId(), stopwatch, "stopwatch");
        transaction.hide(stopwatch);

        transaction.add(binding.frameLayout.getId(), timer, "timer");
        transaction.hide(timer);

        transaction.add(binding.frameLayout.getId(), weather, "weather");
        transaction.hide(weather);

        transaction.commit();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.internationalTime:
                    fragmentManager.beginTransaction().hide(activeFragment).show(international).commit();
                    activeFragment = international;
                    return true;
                case R.id.alarm:
                    fragmentManager.beginTransaction().hide(activeFragment).show(alarm).commit();
                    activeFragment = alarm;
                    return true;
                case R.id.stopWatch:
                    fragmentManager.beginTransaction().hide(activeFragment).show(stopwatch).commit();
                    activeFragment = stopwatch;
                    return true;
                case R.id.timer:
                    fragmentManager.beginTransaction().hide(activeFragment).show(timer).commit();
                    activeFragment = timer;
                    return true;
                case R.id.weather:
                    fragmentManager.beginTransaction().hide(activeFragment).show(weather).commit();
                    activeFragment = weather;
                    return true;
            }
            return false;
        });
    }

//    public void replaceFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(binding.frameLayout.getId(), fragment);
//        transaction.commit();
//    }
}