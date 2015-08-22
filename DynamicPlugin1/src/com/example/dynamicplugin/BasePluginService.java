package com.example.dynamicplugin;

import com.example.dynamicloader.lifecircle.IServiceLifeCircle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BasePluginService extends Service implements IServiceLifeCircle{

	private final String TAG = "BasePluginService";
	private Service mContext;
	
	public BasePluginService() {
	}

	@Override
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
	
	
	//////////////////////// called by proxy service /////////////////////////

	@Override
	public IBinder callOnBind(Intent intent) {
		Log.i(TAG, "callOnBind");
		return onBind(intent);
	}

	@Override
	public void callOnCreate() {
		Log.i(TAG, "callOnCreate");
		onCreate();
	}

	@Override
	public void callOnDestroy() {
		Log.i(TAG, "callOnDestroy");
		onDestroy();
	}

	@Override
	public void callOnLowMemory() {
		Log.i(TAG, "callOnLowMemory");
		onLowMemory();
	}

	@Override
	public void callOnRebind(Intent intent) {
		Log.i(TAG, "callOnRebind " + intent);
		onRebind(intent);
	}

	@Override
	public int callOnStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "callOnStartCommand intent=" + intent + ", flags=" + flags + ", startId=" + startId);
		return onStartCommand(intent, flags, startId);
	}

	@Override
	public void callOnTrimMemory(int level) {
		Log.i(TAG, "callOnTrimMemory " + level);
		onTrimMemory(level);
	}

	@Override
	public boolean callOnUnbind(Intent intent) {
		Log.i(TAG, "callOnUnbind " + intent);
		return onUnbind(intent);
	}

}
