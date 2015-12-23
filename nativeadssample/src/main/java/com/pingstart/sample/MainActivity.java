package com.pingstart.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pingstart.adsdk.NativeAdsManager;
import com.pingstart.adsdk.model.Ad;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NativeAdsManager mNativeAdsManager;
    private LinearLayout mAdsContainer;
    private RelativeLayout mAdsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdsContainer = (LinearLayout) findViewById(R.id.ads_container);
        mNativeAdsManager = new NativeAdsManager(this, 1004, 1002, 5);
        mNativeAdsManager.setListener(new NativeAdsManager.AdsListener() {
            @Override
            public void onAdError() {
                Toast.makeText(MainActivity.this, "onAdError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(ArrayList<Ad> arrayList) {
                //an ad list has loaded, then do your own work
                for (int i = 0; i < arrayList.size(); i++) {
                    Ad ad = arrayList.get(i);
                    if (ad != null) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        mAdsLayout = (RelativeLayout) inflater.inflate(R.layout.native_ad_layout, null);
                        ImageView imageView = (ImageView) mAdsLayout.findViewById(R.id.native_coverImage);
                        TextView titleView = (TextView) mAdsLayout.findViewById(R.id.native_title);
                        TextView contentView = (TextView) mAdsLayout.findViewById(R.id.native_description);
                        TextView actionView = (TextView) mAdsLayout.findViewById(R.id.native_titleForAdButton);
                        ad.displayCoverImage(imageView);
                        titleView.setText(ad.getTitle());
                        contentView.setText(ad.getDescription());
                        actionView.setText(ad.getAdCallToAction());
                        mNativeAdsManager.registerNativeView(ad, actionView);
                        mAdsContainer.addView(mAdsLayout);
                    }
                }
            }

            @Override
            public void onAdClicked() {
                Toast.makeText(MainActivity.this, "onAdClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(MainActivity.this, "onAdOpened", Toast.LENGTH_SHORT).show();
            }
        });
        mNativeAdsManager.loadAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNativeAdsManager != null) {
            mNativeAdsManager.destroy();
        }
    }
}
