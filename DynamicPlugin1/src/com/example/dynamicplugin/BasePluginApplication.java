package com.example.dynamicplugin;

import com.example.dynamicloader.lifecircle.IApplicationLifeCircle;
import com.example.dynamicloader.lifecircle.PluginContext;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class BasePluginApplication extends Application implements IApplicationLifeCircle {
	
	private static final String TAG = "BasePluginApplication";
	private Application mApplication;
	public static Context context;
	private String mDexPath;
	
	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		if (mApplication == null) {
			super.onCreate();
			mApplication = this;
			context = getApplicationContext();
		}
	}

	@Override
	public void callOnCreate() {
		Log.i(TAG, "callOnCreate");
		onCreate();
	}

	@Override
	public void attach(PluginContext context) {
		mApplication = (Application) context.context;
		mDexPath = context.dexPath;
		this.context = mApplication.getApplicationContext();
	}

}
