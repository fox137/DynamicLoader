package com.example.dynamicplugin;

import com.example.dynamicloader.lifecircle.IServiceLifeCircle;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BasePluginService extends Service implements IServiceLifeCircle {

	private final String TAG = "BasePluginService";
	private static final String PROXY_ACTIVITY_ACTION = "com.example.dynamicloader.ProxyActivity";
	private static final String PROXY_SERVICE_ACTION = "com.example.dynamicloader.ProxyService";
	private final String INTENT_CLASS = "classname";
	private final String INTENT_ACTION = "action";
	private final String EXTRA_DEXPATH = "dexpath";
	private final String EXTRA_LIBPATH = "libpath";
	private Service mContext;
	private String mDexPath;

	public BasePluginService() {
	}

	@Override
	public void setContext(Service service, String path) {
		mContext = service;
		mDexPath = path;
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

	//////////////////////start conponents////////////////////
	@Override
	public void startActivity(Intent intent) {
		Log.d(TAG, "intent action = " + intent.getAction());
		if (mContext == this) {
			super.startActivity(intent);
		} else {
			mContext.startActivity(genPluginIntent(intent));
		}
	}

	private Intent genPluginIntent(Intent intent) {
		// start activity by proxy activity
		Intent proxyIntent = new Intent(PROXY_ACTIVITY_ACTION);
		if (intent.getAction() == null) {
			// from new Intent(this, XXXActivity.class)
			ComponentName cn = intent.getComponent();
			proxyIntent.putExtra(INTENT_CLASS, cn.getClassName());
			Log.d(TAG, "intent action = " + cn.getClassName());
		} else {
			// from new Intent(action)
			proxyIntent.putExtra(INTENT_ACTION, intent.getAction());
		}
		proxyIntent.putExtra(EXTRA_DEXPATH, mDexPath);
		return proxyIntent;
	}

	@Override
	public ComponentName startService(Intent intent) {
		if (mContext == this) {
			return super.startService(intent);
		} else {
			Intent proxyIntent = new Intent(PROXY_SERVICE_ACTION);
			if (intent.getAction() == null) {
				// from new Intent(this, XXXActivity.class)
				ComponentName cn = intent.getComponent();
				proxyIntent.putExtra(INTENT_CLASS, cn.getClassName());
				Log.d(TAG, "intent action = " + cn.getClassName());
			} else {
				// from new Intent(action)
				proxyIntent.putExtra(INTENT_CLASS, intent.getAction());
			}
			proxyIntent.putExtra(EXTRA_DEXPATH, mDexPath);
			return mContext.startService(proxyIntent);
		}
	}

	@Override
	public void sendBroadcast(Intent intent) {
		mContext.sendBroadcast(intent);
	}

	@Override
	public void sendBroadcast(Intent intent, String receiverPermission) {
		mContext.sendBroadcast(intent, receiverPermission);
	}

	// ////////////////////// called by proxy service /////////////////////////

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
		Log.i(TAG, "callOnStartCommand intent=" + intent + ", flags=" + flags + ", startId="
				+ startId);
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
