package com.pingstart.utils;

import java.io.File;

import android.os.Environment;
import android.widget.TextView;

public class CommonUtils {

	public static final String MAIN_SDCARD_LOCATION = File.separator + "pingstartdemo" + File.separator;

	public static long mLastClickTime;

	public static void setLabel(TextView textView, String str) {
		if (textView != null) {
			textView.setText(str);
		}
	}

	public static boolean isFastDoubleClick() {
		long currenTime = System.currentTimeMillis();
		long timeD = currenTime - mLastClickTime;
		if (timeD > 0 && timeD < 1000) {
			return true;
		}
		mLastClickTime = currenTime;
		return false;

	}

	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getExternalStorageDirectory() {

		if (isSdcardExist()) {
			File file = Environment.getExternalStorageDirectory();

			String apkCacheDir = file.getAbsolutePath() + MAIN_SDCARD_LOCATION;

			try {
				new File(apkCacheDir + "/").mkdirs();
			} catch (Exception ex) {
				return "";
			}
			return apkCacheDir;
		}

		return "";
	}
}
