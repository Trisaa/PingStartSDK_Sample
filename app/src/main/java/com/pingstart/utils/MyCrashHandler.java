package com.pingstart.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import android.content.Context;
import android.content.pm.ApplicationInfo;

public class MyCrashHandler implements UncaughtExceptionHandler {
	private static final int MAX_CRASH_LOG_COUNT = 3;

	private static UncaughtExceptionHandler mOldHandler;
	private static boolean mRegisted = false;
	private static MyCrashHandler sInstance;
	private static String mVersion;
	private static int mAppFlags = 0;

	private String logPath = null;
	private static final Object sLock = new Object();

	public synchronized static MyCrashHandler getInstance() {
		synchronized (sLock) {
			if (sInstance == null) {
				sInstance = new MyCrashHandler();
			}

			return sInstance;
		}
	}

	public void register(Context ctx) {
		if (!mRegisted) {

			mRegisted = true;			
			mVersion = "";
			mAppFlags = 0;

			try {
				mOldHandler = Thread.getDefaultUncaughtExceptionHandler();
				Thread.setDefaultUncaughtExceptionHandler(this);

				ApplicationInfo ai = ctx.getApplicationInfo();
				if (ai != null) {
					mAppFlags = ai.flags;
				}
				logPath = CommonUtils.getExternalStorageDirectory();
			} catch (Exception e) {				
				mVersion = "";
			}
		}
	}

	public void clearCrashLogs(boolean all) {
		File fs[] = getCrashLogs();
		if (fs != null) {
			if (all) {
				for (int i = 0; i < fs.length; i++) {
					fs[i].delete();
				}
			} else if (fs.length > MAX_CRASH_LOG_COUNT) {
				int t = fs.length - MAX_CRASH_LOG_COUNT;
				for (int i = 0; i < t; i++) {
					fs[i].delete();
				}
			}
		}
	}

	private void outputCrashLog(Throwable ex) {
		try {
			clearCrashLogs(false);

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");

			String s = df.format(new Date());
			File fPath = new File(logPath);

			if (!fPath.exists()) {
				fPath.mkdirs();
			}

			File f = new File(logPath + "crash_" + s + ".txt");

			if (!f.exists()) {
				f.createNewFile();
			}

			if (f != null) {
				FileWriter fw = new FileWriter(f);
				if (fw != null) {
					fw.write("-----infromation----\n");
					fw.write("me="
							+ mVersion
							+ "\ndebug="
							+ String.valueOf(0 != (mAppFlags & ApplicationInfo.FLAG_DEBUGGABLE)));					

					fw.write("\n\n----exception localized message----\n");
					s = ex.getLocalizedMessage();
					if (s != null) {
						fw.write(s);
					}

					fw.write("\n\n----exception stack trace----\n");

					PrintWriter pw = new PrintWriter(fw);
					if (pw != null) {
						Throwable c = ex;
						while (c != null) {
							c.printStackTrace(pw);
							c = c.getCause();
						}

						pw.close();
					}

					fw.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File[] getCrashLogs() {
		File dir = new File(logPath);
		if (dir != null) {
			if (dir.exists()) {
				String fnames[] = dir.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String filename) {
						return filename.startsWith("crash_");
					}
				});

				if (fnames != null && fnames.length != 0) {
					Arrays.sort(fnames, new Comparator<Object>() {
						@Override
						public int compare(Object object1, Object object2) {
							String str1 = (String) object1;
							String str2 = (String) object2;

							return str2.compareTo(str1);
						}
					});

					File[] fs = new File[fnames.length];
					for (int i = 0; i < fnames.length; i++) {
						fs[i] = new File(logPath + fnames[i]);
					}

					return fs;
				}
			}
		}

		return null;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		outputCrashLog(ex);

		if (mOldHandler != null) {
			mOldHandler.uncaughtException(thread, ex);
		}
	}
}