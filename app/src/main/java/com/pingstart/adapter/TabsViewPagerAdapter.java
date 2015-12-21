package com.pingstart.adapter;

import com.pingstart.fragment.LoadAdInterFragment;
import com.pingstart.fragment.LoadAdNativeFragment;
import com.pingstart.fragment.LoadAdBannerFragment;
import com.pingstart.fragment.LoadAdShuffFragment;
import com.pingstart.utils.DataUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsViewPagerAdapter extends FragmentPagerAdapter {

	public TabsViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case DataUtils.AD_BANNER_FIRST:
			return new LoadAdBannerFragment();
		case DataUtils.AD_INTERSTITIAL_SECOND:
			return new LoadAdInterFragment();
		case DataUtils.AD_NATIVE_THIRD:
			return new LoadAdNativeFragment();
		case DataUtils.AD_SHUFFLE_FOUR:
			return new LoadAdShuffFragment();
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return DataUtils.AD_FRAGMENT_COUNT;
	}

}
