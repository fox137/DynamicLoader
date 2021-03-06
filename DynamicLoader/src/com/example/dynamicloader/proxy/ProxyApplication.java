package com.example.dynamicloader.proxy;


import java.lang.reflect.Constructor;

import com.example.dynamicloader.PluginManager;
import com.example.dynamicloader.data.Plugin;
import com.example.dynamicloader.lifecircle.IApplicationLifeCircle;
import com.example.dynamicloader.lifecircle.PluginContext;

import dalvik.system.DexClassLoader;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class ProxyApplication extends Application {
	private static final String TAG = "ProxyApplication";
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();
		PluginManager.getManager().initPlugin(this, PluginManager.PATH_PLUGIN_A);
		launchPluginApplication(PluginManager.getManager().getPlugin(this, PluginManager.PATH_PLUGIN_A));
	}
	
	
	private void launchPluginApplication(Plugin plugin) {
		DexClassLoader classLoader = plugin.getClassLoader();
		try {
			String appName = plugin.getComponent().mApplicationInfo.className;
			Class<?> mPluginClass = classLoader.loadClass(appName);
			Constructor<?> cons = mPluginClass.getConstructor();
			IApplicationLifeCircle mPluginInstance = (IApplicationLifeCircle) cons.newInstance(new Object[] {});
			Log.d(TAG, "instance = " + mPluginInstance);
			PluginContext pc = new PluginContext();
			pc.context = this;
			pc.dexPath = plugin.getPath();
			mPluginInstance.attach(pc);
			mPluginInstance.callOnCreate();
		} catch (Exception e) {
			Log.e(TAG, "load class error: " + e);
		}
	}

}
