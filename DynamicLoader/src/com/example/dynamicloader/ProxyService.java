package com.example.dynamicloader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.example.dynamicloader.lifecircle.IServiceLifeCircle;

import dalvik.system.DexClassLoader;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class ProxyService extends Service {

	public static final String ACTION = "com.example.dynamicloader.ProxyService";
	private static final String TAG = "ProxyService";
	public static final String EXTRA_CLASSNAME = "classname";
	public static final String EXTRA_ACTION = "action";
	public static final String EXTRA_DEXPATH = "dexpath";
	private Class<?> mPluginClass;
	private IServiceLifeCircle mPluginInstance;

	// public ProxyService(String className) {
	// launchPluginService(className);
	// }

	private void initClass(Intent intent) {
		if (intent == null || mPluginInstance != null) {
			return;
		}
		String dexPath = intent.getStringExtra(EXTRA_DEXPATH);
		String className = intent.getStringExtra(EXTRA_CLASSNAME);
		String action = intent.getStringExtra(EXTRA_ACTION);
		if (TextUtils.isEmpty(className) && TextUtils.isEmpty(action) ) {
			Log.e(TAG, "intent error");
			return;
		}
		if (TextUtils.isEmpty(className) && !TextUtils.isEmpty(action) ) {
			className = PluginManager.getManager().getPlugin(this, dexPath).getComponent().getActivityByAction(action);
		}
		launchPluginService(className, dexPath);
	}

	private void launchPluginService(String className, String path) {
		try {
			DexClassLoader classLoader = PluginManager.getManager().getPlugin(this, path).getClassLoader();
			mPluginClass = classLoader.loadClass(className);
			Constructor<?> cons = mPluginClass.getConstructor();
			mPluginInstance = (IServiceLifeCircle) cons.newInstance(new Object[] {});
			Log.d(TAG, "instance = " + mPluginInstance);
			mPluginInstance.setContext(this, path);
			mPluginInstance.callOnCreate();
		} catch (Exception e) {
			Log.e(TAG, "load class error: " + e);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		initClass(intent);
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initClass(intent);
		int result = super.onStartCommand(intent, flags, startId);
		return mPluginInstance.callOnStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		mPluginInstance.callOnDestroy();
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		super.onUnbind(intent);
		return mPluginInstance.callOnUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		mPluginInstance.callOnRebind(intent);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mPluginInstance.callOnLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		mPluginInstance.callOnTrimMemory(level);
	}

}
