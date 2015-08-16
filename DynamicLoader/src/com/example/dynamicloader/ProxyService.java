package com.example.dynamicloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.example.pluginlib.IActivityLifeCircle;

import dalvik.system.DexClassLoader;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class ProxyService extends Service {

	public static final String ACTION = "com.example.dynamicloader.ProxyService";
	private static final String TAG = "ProxyService";
	public static final String EXTRA_CLASSNAME = "classname";
	private Class<?> mPluginClass;
	private Object mPluginInstance;

//	public ProxyService(String className) {
//		launchPluginService(className);
//	}

	private void initClass(Intent intent){
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
		DexClassLoader classLoader = PluginManager.getManager().getClassLoader(this);
		try {
			mPluginClass = classLoader.loadClass(className);
			Constructor<?> cons = mPluginClass.getConstructor();
			mPluginInstance = cons.newInstance(new Object[] {});
			Log.d(TAG, "instance = " + mPluginInstance);
			Log.d(TAG, "instance = " + (mPluginInstance instanceof IActivityLifeCircle));

			// make plugin using the context of host
			Method setContextMethod = mPluginClass.getMethod("setContext", Service.class);
			setContextMethod.setAccessible(true);
			setContextMethod.invoke(mPluginInstance, this);
			Log.d(TAG, "setContextMethod = " + setContextMethod);

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
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
//		try {
//			Method method = mPluginClass.getDeclaredMethod("onCreate", Bundle.class);
//			method.setAccessible(true);
//			method.invoke(mPluginInstance, new Bundle());
//			Log.d(TAG, "onCreateMethod = " + method);
//		} catch (Exception e) {
//			Log.e(TAG, "" + e);
//		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initClass(intent);
		int result = super.onStartCommand(intent, flags, startId);
		try {
			Method method = mPluginClass.getDeclaredMethod("onStartCommand", Intent.class, int.class, int.class);
			method.setAccessible(true);
			result = (Integer) method.invoke(mPluginInstance, intent, flags, startId);
			Log.d(TAG, "onStartCommand = " + method);
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		return result;
	}

	@Override
	public void onDestroy() {
		try {
			Method method = mPluginClass.getDeclaredMethod("onDestroy");
			method.setAccessible(true);
			method.invoke(mPluginInstance);
			Log.d(TAG, "onDestroy = " + method);
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
	}

}
