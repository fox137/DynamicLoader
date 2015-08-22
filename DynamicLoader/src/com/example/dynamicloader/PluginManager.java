package com.example.dynamicloader;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class PluginManager {

	private static final String TAG = "ClassControl";
	private static volatile PluginManager sManager;
	private String mLibPath = null;
	public static final String PLUGIN_PATH1 = Environment.getExternalStorageDirectory() + File.separator + "dl"
			+ File.separator + "DynamicPlugin.apk";
	private Map<Integer, String> mPlugins;

	private Map<String, DexClassLoader> mClassLoaderMap;

	private PluginManager() {
		mClassLoaderMap = new HashMap<String, DexClassLoader>();
		mPlugins = new HashMap<Integer, String>();
		mPlugins.put(1, PLUGIN_PATH1);
	}

	public static PluginManager getManager() {
		if (sManager == null) {
			synchronized (PluginManager.class) {
				if (sManager == null) {
					sManager = new PluginManager();
				}
			}
		}
		return sManager;
	}

	public DexClassLoader getClassLoader(Context context, String path) {
		DexClassLoader cl = mClassLoaderMap.get(path);
		if (cl != null) {
			return cl;
		}
		String dexOutputDir = context.getApplicationInfo().dataDir;
		cl = new DexClassLoader(path, dexOutputDir, mLibPath, context.getClassLoader());
		mClassLoaderMap.put(path, cl);
		return cl;
	}

}
