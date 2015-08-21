package com.example.dynamicloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.example.dynamicloader.lifecircle.IActivityLifecircle;

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
	public static final String EXTRA_INNERCLASS = "class";
	public static final String EXTRA_LIBPATH = "libpath";

	private AssetManager mAssetManager;
	private Resources mResources;
	private Theme mTheme;
	private Object mPluginInstance;
	private Class<?> mPluginClass;

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
			// for plugin to start inner activity
			launchPluginActivity(clsName, dexPath, libPath);
		}
	}

	private void launchPluginActivity(String apkPath) {
		PackageInfo pi = this.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		String atName = pi.activities[0].name;
		String libDir = pi.applicationInfo.nativeLibraryDir;
		Log.d(TAG, "launchPluginActivity apkPath=" + apkPath + ", activityname=" + atName + ", libdir=" + libDir);
		launchPluginActivity(atName, apkPath, libDir);
	}

	private void launchPluginActivity(String className, String dexPath, String libPath) {
		loadResources(dexPath);
//		DexClassLoader classLoader = PluginManager.getManager().getClassLoader(this);
		ClassLoader cl= getClassLoader() ;
	    DexClassLoader classLoader = new DexClassLoader(dexPath, getApplicationInfo().dataDir, null ,cl) ;  
		File file = new File(dexPath);
		Log.d("xufeng", "file exist = " + file.exists());
		try {
			mPluginClass = classLoader.loadClass(className);
			Constructor<?> cons = mPluginClass.getConstructor();
			mPluginInstance = cons.newInstance(new Object[]{});
			Log.d(TAG, "instance = " + mPluginInstance);
			Log.d(TAG, "instance " + (mPluginInstance instanceof IActivityLifecircle));

			// make plugin using the context of host
			Method setContextMethod = mPluginClass.getMethod("setContext", Activity.class, String.class);
			setContextMethod.setAccessible(true);
			setContextMethod.invoke(mPluginInstance, this, dexPath);
			Log.d(TAG, "setContextMethod = " + setContextMethod);

			// start activity
			Method onCreateMethod = mPluginClass.getDeclaredMethod("onCreate", Bundle.class);
			onCreateMethod.setAccessible(true);
			onCreateMethod.invoke(mPluginInstance, new Bundle());
			Log.d(TAG, "onCreateMethod = " + onCreateMethod);
		} 
		catch (ClassNotFoundException e) {
			Log.e(TAG, "load class error: " + e);
		}
		catch (Exception e) {
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
		try {
			Method method = mPluginClass.getDeclaredMethod("onStart");
			method.setAccessible(true);
			method.invoke(mPluginInstance);
		} catch (Exception e) {
			Log.w(TAG, ""+e);
		} 
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		try {
			Method method = mPluginClass.getDeclaredMethod("onRestart");
			method.setAccessible(true);
			method.invoke(mPluginInstance);
		} catch (Exception e) {
			Log.w(TAG, ""+e);
		} 
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			Method method = mPluginClass.getDeclaredMethod("onResume");
			method.setAccessible(true);
			method.invoke(mPluginInstance);
		} catch (Exception e) {
			Log.w(TAG, ""+e);
		} 
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		try {
			Method method = mPluginClass.getDeclaredMethod("onPause");
			method.setAccessible(true);
			method.invoke(mPluginInstance);
		} catch (Exception e) {
			Log.w(TAG, ""+e);
		} 
	}
	@Override
	protected void onStop() {
		super.onStop();
		try {
			Method method = mPluginClass.getDeclaredMethod("onStop");
			method.setAccessible(true);
			method.invoke(mPluginInstance);
		} catch (Exception e) {
			Log.w(TAG, ""+e);
		} 
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			Method method = mPluginClass.getDeclaredMethod("onDestroy");
			method.setAccessible(true);
			method.invoke(mPluginInstance);
		} catch (Exception e) {
			Log.w(TAG, ""+e);
		} 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try{
			Method method = mPluginClass.getDeclaredMethod("onActivityResult", int.class, int.class, Intent.class);
			method.setAccessible(true);
			method.invoke(mPluginInstance, requestCode, resultCode, data);
		}catch(Exception e){
			Log.w(TAG, ""+e);
		}
	}
}
