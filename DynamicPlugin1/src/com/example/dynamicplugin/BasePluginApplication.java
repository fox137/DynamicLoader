package com.example.dynamicplugin;

import com.example.dynamicloader.lifecircle.IApplicationLifeCircle;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class BasePluginApplication extends Application implements IApplicationLifeCircle {
	
	private static final String TAG = "BasePluginApplication";
	private Application mApplication;
	public static Context sContext;
	
	@Override
	public void onCreate() {
		if (mApplication == null) {
			super.onCreate();
			mApplication = this;
			sContext = getApplicationContext();
		}
	}

	@Override
	public void callOnCreate() {
		Log.i(TAG, "callOnCreate");
		onCreate();
	}

	@Override
	public void setContext(Application app, String path) {
		mApplication = app;
		sContext = app.getApplicationContext();
	}

}
