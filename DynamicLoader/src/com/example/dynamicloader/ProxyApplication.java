package com.example.dynamicloader;


import android.app.Application;
import android.content.Context;

public class ProxyApplication extends Application {
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();
		PluginManager.getManager().initPlugin(this, PluginManager.PATH_PLUGIN_A);
	}
	

	public static Context getContext() {
		return sContext;
	}
}
