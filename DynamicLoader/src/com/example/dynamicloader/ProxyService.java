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
	private Class<?> mPluginClass;
	private IServiceLifeCircle mPluginInstance;

	// public ProxyService(String className) {
	// launchPluginService(className);
	// }

	private void initClass(Intent intent) {
		if (intent == null || mPluginInstance != null) {
			return;
		}
		String className = intent.getStringExtra(EXTRA_CLASSNAME);
		if (TextUtils.isEmpty(className)) {
			return;
		}
		launchPluginService(className);
	}

	private void launchPluginService(String className) {
		try {
			DexClassLoader classLoader = PluginManager.getManager().getClassLoader(this, PluginManager.PLUGIN_PATH1);
			mPluginClass = classLoader.loadClass(className);
			Constructor<?> cons = mPluginClass.getConstructor();
			mPluginInstance = (IServiceLifeCircle) cons.newInstance(new Object[] {});
			Log.d(TAG, "instance = " + mPluginInstance);
			mPluginInstance.setContext(this);
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
