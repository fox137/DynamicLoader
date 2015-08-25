package com.example.dynamicloader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.dynamicloader.data.Plugin;
import com.example.dynamicloader.data.PluginResource;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class PluginManager {

	private static final String TAG = "ClassControl";
	private static volatile PluginManager sManager;
	public static final String PATH_PLUGIN_A = Environment.getExternalStorageDirectory() + File.separator + "dl"
			+ File.separator + "DynamicPlugin.apk";
	private Map<String, Plugin> mPluginMap;

	private PluginManager() {
		mPluginMap = new HashMap<String, Plugin>();
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
	
	public void initPlugin(Context context, String path){
		Plugin plugin = new Plugin(context, path);
		mPluginMap.put(PATH_PLUGIN_A, plugin);
	}
	
	public void releasePlugin(Context context, String path){
		Plugin plugin = mPluginMap.get(path);
		if (plugin != null) {
			plugin.release(context);
		}
	}

	public Plugin getPlugin(Context context, String path){
		Plugin plugin = mPluginMap.get(path);
		if (plugin == null) {
			initPlugin(context, path);
		}
		return mPluginMap.get(path);
	}

}
