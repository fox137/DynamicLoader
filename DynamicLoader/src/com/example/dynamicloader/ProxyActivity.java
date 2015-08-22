package com.example.dynamicloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
	public static final String EXTRA_LIBPATH = "libpath";

	private AssetManager mAssetManager;
	private Resources mResources;
	private Theme mTheme;
	private IActivityLifeCircle mPluginInstance;
	private Class<?> mPluginClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String dexPath = getIntent().getStringExtra(EXTRA_DEXPATH);
		String clsName = getIntent().getStringExtra(EXTRA_CLASS);
		String libPath = getIntent().getStringExtra(EXTRA_LIBPATH);
		Log.i(TAG, "launch dexPath=" + dexPath + ", clsName=" + clsName + ", libPath=" + libPath);
		launchPluginActivity(clsName, dexPath, libPath);
	}

//	private void launchPluginActivity(String apkPath) {
//		PackageInfo pi = this.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
//		String atName = pi.activities[0].name;
//		String libDir = pi.applicationInfo.nativeLibraryDir;
//		Log.d(TAG, "launchPluginActivity apkPath=" + apkPath + ", activityname=" + atName + ", libdir=" + libDir);
//		launchPluginActivity(atName, apkPath, libDir);
//	}

	private void launchPluginActivity(String className, String dexPath, String libPath) {
		loadResources(dexPath);
		DexClassLoader classLoader = PluginManager.getManager().getClassLoader(this, dexPath);
		try {
			mPluginClass = classLoader.loadClass(className);
			Constructor<?> cons = mPluginClass.getConstructor();
			mPluginInstance = (IActivityLifeCircle) cons.newInstance(new Object[] {});
			Log.d(TAG, "instance = " + mPluginInstance);
			mPluginInstance.setContext(this, dexPath);
			mPluginInstance.callOnCreate(new Bundle());
		} catch (Exception e) {
			Log.e(TAG, "load class error: " + e);
		}
	}

	protected void loadResources(String dexPath) {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
			addAssetPath.invoke(assetManager, dexPath);
			mAssetManager = assetManager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resources superRes = super.getResources();
		mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
		mTheme = mResources.newTheme();
		mTheme.setTo(super.getTheme());
	}

	@Override
	public AssetManager getAssets() {
		return mAssetManager == null ? super.getAssets() : mAssetManager;
	}

	@Override
	public Resources getResources() {
		return mResources == null ? super.getResources() : mResources;
	}

	@Override
	public Theme getTheme() {
		return mTheme == null ? super.getTheme() : mTheme;
	}

	@Override
	protected void onStart() {
		super.onStart();
		mPluginInstance.callOnStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mPluginInstance.callOnRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPluginInstance.callOnResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPluginInstance.callOnPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPluginInstance.callOnStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPluginInstance.callOnDestory();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mPluginInstance.callOnActivityResult(requestCode, resultCode, data);
	}
}
