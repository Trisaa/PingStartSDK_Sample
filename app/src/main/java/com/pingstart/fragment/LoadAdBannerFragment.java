package com.pingstart.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pingstart.R;
import com.pingstart.adsdk.AdManager;
import com.pingstart.adsdk.BannerListener;
import com.pingstart.adsdk.model.Ad;
import com.pingstart.utils.DataUtils;

public class LoadAdBannerFragment extends Fragment implements BannerListener, OnClickListener {
    private RelativeLayout mAdViewBannerContainer;
    private View mLoadAds;
    private Button mRefreshButton;
    private RelativeLayout mLoadingLayout;
    private AdManager mAdsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdsManager = new AdManager(getActivity(), DataUtils.ADS_APPID, DataUtils.ADS_SLOTID, DataUtils.ADS_PLACEMENT_ID_CPM, DataUtils.ADS_PLACEMENT_ID_FILL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        mRefreshButton = (Button) view.findViewById(R.id.refreshButton);
        mAdViewBannerContainer = (RelativeLayout) view.findViewById(R.id.adViewContainer);
        mLoadingLayout = (RelativeLayout) view.findViewById(R.id.fresh_ad_show);
        mRefreshButton.setOnClickListener(this);
        setViewVisible(View.INVISIBLE, View.VISIBLE);
        return view;
    }

    private void setViewVisible(int mProgressvisible, int mButtonvisible) {
        mLoadingLayout.setVisibility(mProgressvisible);
        mRefreshButton.setVisibility(mButtonvisible);
    }

    @Override
    public void onClick(View v) {
        setViewVisible(View.VISIBLE, View.INVISIBLE);
        if (mAdsManager != null) {
            mAdsManager.destroy();
            mAdsManager = new AdManager(getActivity(), DataUtils.ADS_APPID, DataUtils.ADS_SLOTID);
            mAdsManager.setListener(this);
            mAdsManager.loadAd();
        }

    }

    @Override
    public void onDestroyView() {
        mAdViewBannerContainer.removeView(mLoadAds);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mAdsManager.destroy();
        super.onDestroy();
    }

    @Override
    public void onAdError() {
        setViewVisible(View.INVISIBLE, View.VISIBLE);
        Toast.makeText(getActivity(), "Banner Erro", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        mLoadAds = mAdsManager.getBannerView();
        mAdViewBannerContainer.addView(mLoadAds);
        setViewVisible(View.INVISIBLE, View.INVISIBLE);
    }

    @Override
    public void onAdOpened() {
        Toast.makeText(getActivity(), "Banner Opened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClicked() {
        Toast.makeText(getActivity(), "Banner Clicked", Toast.LENGTH_SHORT).show();
    }
}