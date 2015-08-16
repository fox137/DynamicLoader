package com.example.dynamicplugin1;

import java.lang.reflect.Method;

import com.example.pluginlib.IActivityLifeCircle;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class BasePluginActivity extends Activity{

	protected Activity mContext;
	private static final String PROXY_ACTION = "com.example.dynamicloader.ProxyActivity";
	protected static final String TAG = "BasePluginActivity";
	private final String INTENT_CLASS = "class";
	private final String EXTRA_DEXPATH = "dexpath";
	private final String EXTRA_LIBPATH = "libpath";
	// private final String DEX_PATH = "/storage/sdcard0/dl/DynamicPlugin1.apk";
	private String mDexPath;
	private Instrumentation mInstrumentation;

	/**
	 * Set Host context
	 * 
	 * @param at
	 */
	public void setContext(Activity at, String dexPath) {
		mContext = at;
		mDexPath = dexPath;
		mInstrumentation = new Instrumentation();
	}

	public Activity getContext() {
		return mContext;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// When context is null, the plugin is started by self
		if (mContext == null) {
			super.onCreate(savedInstanceState);
			mContext = this;
		}

	}

	@Override
	public void setContentView(int layoutResID) {
		if (mContext == this) {
			super.setContentView(layoutResID);
		} else {
			mContext.setContentView(layoutResID);
		}
	}

	@Override
	public void setContentView(View view) {
		if (mContext == this) {
			super.setContentView(view);
		} else {
			mContext.setContentView(view);
		}
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		if (mContext == this) {
			super.setContentView(view, params);
		} else {
			mContext.setContentView(view, params);
		}
	}

	@Override
	public void addContentView(View view, LayoutParams params) {
		if (mContext == this) {
			super.addContentView(view, params);
		} else {
			mContext.addContentView(view, params);
		}
	}

	@Override
	public void startActivity(Intent intent) {
		Log.d(TAG, "intent action = " + intent.getAction());
		if (mContext == this) {
			super.startActivity(intent);
		} else {
			// start activity by proxy activity
			Intent proxyIntent = new Intent(PROXY_ACTION);
			if (intent.getAction() == null) {
				// from new Intent(this, XXXActivity.class)
				ComponentName cn = intent.getComponent();
				proxyIntent.putExtra(INTENT_CLASS, cn.getClassName());
				Log.d(TAG, "intent action = " + cn.getClassName());
			} else {
				// from new Intent(action)
				proxyIntent.putExtra(INTENT_CLASS, intent.getAction());
			}

			proxyIntent.putExtra(EXTRA_DEXPATH, mDexPath);
			mContext.startActivity(proxyIntent);
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		Log.d(TAG, "intent action = " + intent.getAction());
		if (mContext == this) {
			super.startActivityForResult(intent, requestCode);
		} else {
			// start activity by proxy activity
			Intent proxyIntent = new Intent(PROXY_ACTION);
			if (intent.getAction() == null) {
				// from new Intent(this, XXXActivity.class)
				ComponentName cn = intent.getComponent();
				proxyIntent.putExtra(INTENT_CLASS, cn.getClassName());
				Log.d(TAG, "intent action = " + cn.getClassName());
			} else {
				// from new Intent(action)
				proxyIntent.putExtra(INTENT_CLASS, intent.getAction());
			}

			proxyIntent.putExtra(EXTRA_DEXPATH, mDexPath);
			mContext.startActivityForResult(proxyIntent, requestCode);
		}
	}

	@Override
	public View findViewById(int id) {
		if (mContext == this) {
			return super.findViewById(id);
		}
		return mContext.findViewById(id);
	}

	@Override
	protected void onStart() {
		if (mInstrumentation == null) {
			super.onStart();
		} else {
			mInstrumentation.callActivityOnStart(mContext);
		}
	}

	@Override
	protected void onRestart() {
		if (mInstrumentation == null) {
			super.onRestart();
		} else {
			mInstrumentation.callActivityOnRestart(mContext);
		}
	}

	@Override
	protected void onResume() {
		if (mContext == this) {
			super.onResume();
		} 
	}

	@Override
	protected void onPause() {
		if (mContext == this) {
			super.onPause();
		} 
	}

	@Override
	protected void onStop() {
		if (mContext == this) {
			super.onStop();
		}
	}

	@Override
	protected void onDestroy() {
		if (mContext == this) {
			super.onDestroy();
		} 
	}

//	@Override
//	public void callOnDestory() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void callOnPause() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void callOnRestart() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void callOnResume() {
//		// TODO Auto-generated method stub
//		onResume();
//	}
//
//	@Override
//	public void callOnStart() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void callOnStop() {
//		// TODO Auto-generated method stub
//		
//	}

}
