package com.example.dynamicloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.example.pluginlib.IActivityLifeCircle;

import dalvik.system.DexClassLoader;
import android.app.Activity;
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
	public static final String EXTRA_INNERCLASS = "class";
	public static final String EXTRA_LIBPATH = "libpath";

	private AssetManager mAssetManager;
	private Resources mResources;
	private Theme mTheme;
	private IActivityLifeCircle mPluginLife;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String dexPath = getIntent().getStringExtra(EXTRA_DEXPATH);
		String clsName = getIntent().getStringExtra(EXTRA_INNERCLASS);
		String libPath = getIntent().getStringExtra(EXTRA_LIBPATH);
		if (TextUtils.isEmpty(clsName)) {
			// for host to start activity
			launchPluginActivity(dexPath);
		} else {
			// for plugin to start activity
			launchPluginActivity(clsName, dexPath, libPath);
		}
	}

	private void launchPluginActivity(String apkPath) {
		PackageInfo pi = this.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		String cn = pi.activities[0].name;
		String lp = pi.applicationInfo.nativeLibraryDir;
		Log.d(TAG, "launchPluginActivity apkPath=" + apkPath + ", activityname=" + cn + ", libdir=" + lp);
		launchPluginActivity(cn, apkPath, lp);
	}

	private void launchPluginActivity(String className, String dexPath, String libPath) {
		loadResources(dexPath);
		String dexOutputDir = this.getApplicationInfo().dataDir;

		DexClassLoader classLoader = new DexClassLoader(dexPath, dexOutputDir, libPath,
				ClassLoader.getSystemClassLoader());

		try {
			Class<?> clazz = classLoader.loadClass(className);
			Constructor<?> cons = clazz.getConstructor();
			Object instance = cons.newInstance(null);
			mPluginLife = (IActivityLifeCircle) instance;
			Log.d(TAG, "instance = " + instance);

			// make plugin using the context of host
			Method setContextMethod = clazz.getMethod("setContext", Activity.class, String.class);
			setContextMethod.setAccessible(true);
			setContextMethod.invoke(instance, this, dexPath);
			Log.d(TAG, "setContextMethod = " + setContextMethod);

			// start activity
			Method onCreateMethod = clazz.getDeclaredMethod("onCreate", Bundle.class);
			onCreateMethod.setAccessible(true);
			onCreateMethod.invoke(instance, new Bundle());
			Log.d(TAG, "onCreateMethod = " + onCreateMethod);
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
		if (mPluginLife == null) {
			super.onStart();
		}else{
			mPluginLife.callOnStart();
		}
	}
	
	@Override
	protected void onRestart() {
		if (mPluginLife == null) {
			super.onRestart();
		}else{
			mPluginLife.callOnRestart();
		}
	}
	
	@Override
	protected void onResume() {
		if (mPluginLife == null) {
			super.onResume();
		}else{
			mPluginLife.callOnResume();
		}
	}
	
	@Override
	protected void onPause() {
		if (mPluginLife == null) {
			super.onPause();
		}else{
			mPluginLife.callOnPause();
		}
	}
	
	@Override
	protected void onStop() {
		if (mPluginLife == null) {
			super.onStop();
		}else{
			mPluginLife.callOnStop();
		}
	}
	
	@Override
	protected void onDestroy() {
		if (mPluginLife == null) {
			super.onDestroy();
		}else{
			mPluginLife.callOnDestory();
		}
	}
	
}
