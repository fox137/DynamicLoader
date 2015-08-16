package com.example.dynamicloader;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class PluginManager {
	
	private static final String TAG = "ClassControl";
	private static volatile PluginManager sManager;
	private DexClassLoader mClassLoader;
	private String mDexPath = Environment.getExternalStorageDirectory() +
			File.separator + "dl" + File.separator + "DynamicPlugin1.apk";
	
	private PluginManager(){
		
	}
	
	public static PluginManager getManager(){
		if(sManager == null){
			synchronized (PluginManager.class) {
				if (sManager == null) {
					sManager = new PluginManager();
				}
			}
		}
		return sManager;
	}
	
	public DexClassLoader getClassLoader(Context context){
		if (mClassLoader != null) {
			return mClassLoader;
		}
		String dexOutputDir = context.getApplicationInfo().dataDir;
		mClassLoader = new DexClassLoader(mDexPath, dexOutputDir, null,
				ClassLoader.getSystemClassLoader());
		return mClassLoader;
	}

}
