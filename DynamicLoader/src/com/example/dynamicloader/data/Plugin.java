package com.example.dynamicloader.data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import dalvik.system.DexClassLoader;

public class Plugin {

	private String mDexPath;
	private String mLibPath;
	private PluginResource mResource;
	private DexClassLoader mClassLoader;
	private PluginComponent mComponent;

	public Plugin(Context context, String path) {
		mDexPath = path;
		
		initClassLoader(context);
		initResources(context);
		initComponent(context, mClassLoader);
	}


	private void initComponent(Context context, DexClassLoader classLoader) {
		mComponent = new PluginComponent(context, mDexPath, classLoader);
	}


	private void initClassLoader(Context context) {
		PackageInfo pi = context.getPackageManager().getPackageArchiveInfo(mDexPath, 0);
		mLibPath = pi.applicationInfo.nativeLibraryDir;
		String dexOutputDir = context.getApplicationInfo().dataDir;
		mClassLoader = new DexClassLoader(mDexPath, dexOutputDir, mLibPath, context.getClassLoader());
	}

	private void initResources(Context context) {
		mResource = new PluginResource();
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
			addAssetPath.invoke(assetManager, mDexPath);
			mResource.assetManager = assetManager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resources superRes = context.getResources();
		mResource.resources = new Resources(mResource.assetManager, superRes.getDisplayMetrics(),
				superRes.getConfiguration());
		mResource.theme = mResource.resources.newTheme();
		mResource.theme.setTo(context.getTheme());
	}



	public DexClassLoader getClassLoader() {
		return mClassLoader;
	}

	public PluginResource getResource() {
		return mResource;
	}

	public PluginComponent getComponent(){
		return mComponent;
	}


	public void release(Context context) {
		mComponent.unregisterReceivers(context.getApplicationContext());
		
	}

}
