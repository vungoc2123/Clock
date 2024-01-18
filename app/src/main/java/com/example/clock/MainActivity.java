package com.example.clock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.ads.AdsAdmob;
import com.example.broadcast.MyBroadcast;
import com.example.clock.databinding.ActivityMainBinding;
import com.example.config.ConfigString;
import com.example.fragment.alarmFragment;
import com.example.fragment.internationalFragment;
import com.example.fragment.stopwatchFragment;
import com.example.fragment.timerFragment;
import com.example.fragment.weatherFragment;
import com.example.service.MyService;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    private Fragment activeFragment = new internationalFragment();
    private internationalFragment international = new internationalFragment();
    private alarmFragment alarm= new alarmFragment();
    private stopwatchFragment stopwatch = new stopwatchFragment();
    private timerFragment timer = new timerFragment();
    private weatherFragment weather = new weatherFragment();
    FragmentManager fragmentManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        AdsAdmob admob = new AdsAdmob(this);
        admob.loadBannerAd(this);
        admob.loadInterAd(this);

        checkPermission();
        fragmentManager = getSupportFragmentManager();
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
                    admob.loadInterAd(this);
                    admob.showAdsFullBeforeDoAction(this);
                    fragmentManager.beginTransaction().hide(activeFragment).show(international).commit();
                    activeFragment = international;
                    return true;
                case R.id.alarm:
                    admob.loadInterAd(this);
                    admob.showAdsFullBeforeDoAction(this);
                    fragmentManager.beginTransaction().hide(activeFragment).show(alarm).commit();
                    activeFragment = alarm;
                    return true;
                case R.id.stopWatch:
                    admob.loadInterAd(this);
                    admob.showAdsFullBeforeDoAction(this);
                    fragmentManager.beginTransaction().hide(activeFragment).show(stopwatch).commit();
                    activeFragment = stopwatch;
                    return true;
                case R.id.timer:
                    admob.loadInterAd(this);
                    admob.showAdsFullBeforeDoAction(this);
                    fragmentManager.beginTransaction().hide(activeFragment).show(timer).commit();
                    activeFragment = timer;
                    return true;
                case R.id.weather:
                    admob.loadInterAd(this);
                    admob.showAdsFullBeforeDoAction(this);
                    fragmentManager.beginTransaction().hide(activeFragment).show(weather).commit();
                    activeFragment = weather;
                    return true;
            }
            return false;
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(), fragment);
        transaction.commit();
    }
    public void hanldeStopService(){
        Intent intent = getIntent();
        String nameFragment = intent.getStringExtra(ConfigString.nameFragment);
        if(ConfigString.alarmFragment.equals(nameFragment)){
            fragmentManager.beginTransaction().hide(activeFragment).show(alarm).commit();
            activeFragment = alarm;
            binding.bottomNavigation.setSelectedItemId(R.id.alarm);
        } else if (ConfigString.timerFragment.equals(nameFragment)) {
            fragmentManager.beginTransaction().hide(activeFragment).show(timer).commit();
            activeFragment = timer;
            binding.bottomNavigation.setSelectedItemId(R.id.timer);
        }
        startMyService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hanldeStopService();
    }

    public void startMyService(){
        Intent intent1 = new Intent(this, MyService.class);
        intent1.setAction(ConfigString.stop);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent1);
        }else{
            startService(intent1);
        }
    }
    public void checkPermission(){
        int hasPostNotification = ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS);
        if (hasPostNotification != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 199);
        }
    }


}