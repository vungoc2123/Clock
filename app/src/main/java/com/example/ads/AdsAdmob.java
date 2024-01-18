package com.example.ads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.clock.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdsAdmob {
    private Context context;
    private static InterstitialAd mInterstitialAd = null;
    private static long lastAdDisplayTime = 0;

    private static final long MIN_AD_DISPLAY_INTERVAL =  3 * 60 * 1000;
    public AdsAdmob(Context context) {
        this.context = context;
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public static void showAdsFullBeforeDoAction(Activity act) {
        long currentTime = System.currentTimeMillis();
        if (mInterstitialAd != null && (currentTime - lastAdDisplayTime) >= MIN_AD_DISPLAY_INTERVAL) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    mInterstitialAd = null;
                    lastAdDisplayTime = System.currentTimeMillis();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    mInterstitialAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                }
            });

            mInterstitialAd.show(act);

        }
    }

    public void loadBannerAd(Activity act) {
        AdView mAdView = act.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if(mAdView != null){
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                    System.out.println("Ad Clicked");
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    System.out.println("Ad Failed to Load: " + adError.getMessage());
                }

                @Override
                public void onAdImpression() {

                }

                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    System.out.println("Ad Loaded");
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                    System.out.println("Ad Opened");
                }
            });
        }
    }

    public void loadInterAd(Activity act) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(act, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }
}
