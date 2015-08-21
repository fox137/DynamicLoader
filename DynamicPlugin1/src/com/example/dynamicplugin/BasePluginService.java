package com.example.dynamicplugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BasePluginService extends Service {

	private final String TAG = "BasePluginService";
	private Service mContext;
	
	public BasePluginService() {
	}

	public void setContext(Service service) {
		mContext = service;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		if (mContext == this) {
			mContext = this;
			super.onCreate();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mContext == this) {
			return super.onStartCommand(intent, flags, startId);
		}
		return 0;
	}

	@Override
	public void onRebind(Intent intent) {
		if (mContext == this) {
			super.onRebind(intent);
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (mContext == this) {
			return super.onUnbind(intent);
		} else {
			return false;
		}
	}
	
	@Override
	public void onDestroy() {
		if (mContext == this) {
			super.onDestroy();
		}
	}

}
