package com.example.autoclickdemo;

import java.io.OutputStream;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * @description 自动点击adb shell版
 * @author gaok
 * @data 2018年1月23日 下午2:53:21
 */
public class AutoService extends Service {
	
	private boolean start = true;
	private OutputStream os;
	/**
	 * 模拟点击的ADB命令
	 */
	private static String ADB_SHELL = "input tap 125 340 \n";
	/**
	 * 目标包名
	 */
	private static String PACKAGE_NAME = "com.meizu.media.music";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		autoClick();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 服务销毁时的回调
	 */
	@Override
	public void onDestroy() {
		start = false;
		super.onDestroy();
	}

	private void autoClick() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (start) {
					if (isCurrentAppIsTarget()) {
						exec(ADB_SHELL);
					}
//					 1.利用ProcessBuilder执行shell命令，不能后台
//					int x = 0, y = 0;
//					String[] order = { "input", "tap", " ", x + "", y + "" };
//					try {
//						new ProcessBuilder(order).start();
//					} catch (IOException e) {
//						Log.i("GK", e.getMessage());
//						e.printStackTrace();
//					}

					// 2.可以不用在 Activity 中增加任何处理，各 Activity 都可以响应，不能后台
//					try {
//						Instrumentation inst = new Instrumentation();
//						inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
//						inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
//						Log.i("GK", "模拟点击" + x + ", " + y);
//					} catch (Exception e) {
//						Log.e("Exception when sendPointerSync", e.toString());
//					}
					try {
						Thread.sleep(1 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 如果前台APP是目标apk
	 */
	private boolean isCurrentAppIsTarget() {
		String name = getForegroundAppPackageName();
		if (!TextUtils.isEmpty(name) && PACKAGE_NAME.equalsIgnoreCase(name)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取前台程序包名，该方法在android L之前有效
	 */
	public String getForegroundAppPackageName() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> lr = am.getRunningAppProcesses();
		if (lr == null) {
			return null;
		}

		for (RunningAppProcessInfo ra : lr) {
			if (ra.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE || ra.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				Log.i("GK", ra.processName);
				return ra.processName;
			}else{
				Log.i("GK", "找不到");
			}
		}
		return "";
	}

	/**
	 * 执行ADB命令： input tap 125 340
	 */
	public final void exec(String cmd) {
		try {
			if (os == null) {
				os = Runtime.getRuntime().exec("su").getOutputStream();
			}
			os.write(cmd.getBytes());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("GK", e.getMessage());
		}
	}
}
