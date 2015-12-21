package com.pingstart.utils;

import android.content.Context;

import com.pingstart.adsdk.AdManager;
import com.pingstart.data.DataUtils;

public class SingleAdsManager {

    private static SingleAdsManager instance = new SingleAdsManager();

    public static SingleAdsManager getInstance() {
        return instance;

    }

    public static void setInstance(SingleAdsManager instance) {
        SingleAdsManager.instance = instance;
    }

    private AdManager adsManager = null;

    public AdManager getAdsManager(Context context) {
        if (adsManager == null) {
            adsManager = new AdManager(context, DataUtils.ADS_APPID,
                    DataUtils.ADS_SLOTID);
        }
        return adsManager;
    }
}
