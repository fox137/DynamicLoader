package com.example.dynamicplugin.test;

import com.example.dynamicplugin.BasePluginService;

import android.content.Intent;
import android.util.Log;

public class MainService extends BasePluginService {

	private String TAG = "MainService";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "plugin service onCreate");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 3; i++) {
					Log.d(TAG, "thread run " + i);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						Log.e(TAG, "" + e);
					}
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
}
