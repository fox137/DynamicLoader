package com.example.dynamicloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.example.dynamicloader.data.PluginResource;
import com.example.dynamicloader.lifecircle.IActivityLifeCircle;

import dalvik.system.DexClassLoader;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * Load plugin by classloader
 * 
 * @author xufeng
 * 
 */
public class ProxyActivity extends Activity {

	public static final String TAG = "ProxyActivity";
	public static final String ACTION = "com.example.dynamicloader.ProxyActivity";
	public static final String EXTRA_DEXPATH = "dexpath";
	public static final String EXTRA_CLASS = "classname";
	public static final String EXTRA_ACTION = "action";
	public static final String EXTRA_LIBPATH = "libpath";

	private IActivityLifeCircle mPluginInstance;
	private Class<?> mPluginClass;
	private PluginResource mResource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		judgeIntent();
	}
	
	private void judgeIntent(){
		String dexPath = getIntent().getStringExtra(EXTRA_DEXPATH);
		String clsName = getIntent().getStringExtra(EXTRA_CLASS);
		String action = getIntent().getStringExtra(EXTRA_ACTION);
		if (TextUtils.isEmpty(clsName) && TextUtils.isEmpty(action) ) {
			Log.e(TAG, "intent error");
			return;
		}
		if (!TextUtils.isEmpty(dexPath) ) {
			Log.i(TAG, "start plugin activity dexPath=" + dexPath + ", clsName=" + clsName + ", action=" + action);
			if (TextUtils.isEmpty(clsName) && !TextUtils.isEmpty(action) ) {
				clsName = PluginManager.getManager().getPlugin(this, dexPath).getComponent().getActivityByAction(action);
			}
			launchPluginActivity(clsName, dexPath);
		}else {
			Log.i(TAG, "start host activity clsName=" + clsName + ", action=" + action);
			if (!TextUtils.isEmpty(action)) {
				startActivity(new Intent(action));
			}else {
				try {
					Intent intent = new Intent(this, Class.forName(clsName));
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					Log.e(TAG, ""+ e);
				}
			}
			finish();
		}
		Log.i(TAG, "launch plugin " + dexPath + ", clsName=" + clsName );
	}

	private void launchPluginActivity(String className, String path) {
		mResource = PluginManager.getManager().getPlugin(this, path).getResource();
		DexClassLoader classLoader = PluginManager.getManager().getPlugin(this, path).getClassLoader();
		try {
			mPluginClass = classLoader.loadClass(className);
			Constructor<?> cons = mPluginClass.getConstructor();
			mPluginInstance = (IActivityLifeCircle) cons.newInstance(new Object[] {});
			Log.d(TAG, "instance = " + mPluginInstance);
			mPluginInstance.setContext(this, path);
			mPluginInstance.callOnCreate(new Bundle());
		} catch (Exception e) {
			Log.e(TAG, "load class error: " + e);
		}
	}
	
	
	
	@Override
	public AssetManager getAssets() {
		return mResource == null ? super.getAssets() : mResource.assetManager;
	}

	@Override
	public Resources getResources() {
		return mResource == null ? super.getResources() : mResource.resources;
	}

	@Override
	public Theme getTheme() {
		return mResource == null ? super.getTheme() : mResource.theme;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mPluginInstance != null) mPluginInstance.callOnStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (mPluginInstance != null) mPluginInstance.callOnRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPluginInstance != null) mPluginInstance.callOnResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mPluginInstance != null) mPluginInstance.callOnPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mPluginInstance != null) mPluginInstance.callOnStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPluginInstance != null) mPluginInstance.callOnDestory();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mPluginInstance != null) mPluginInstance.callOnActivityResult(requestCode, resultCode, data);
	}
}
