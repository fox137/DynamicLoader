package com.example.dynamicplugin1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class BaseActivity extends Activity {
	
	protected Activity mContext;
	private static final String PROXY_ACTION = "com.example.dynamicloader.ProxyActivity";
	private static final String TAG = "BaseActivity";
	private final String INTENT_CLASS = "class";
	private final String EXTRA_DEXPATH = "dexpath";
	private final String EXTRA_LIBPATH = "libpath";
	private final String DEX_PATH = "/storage/sdcard0/dl/DynamicPlugin1.apk";
	
	/**
	 * Set Host context
	 * @param at
	 */
	public void setContext(Activity at){
		mContext = at;
	}
	
	public Activity getContext(){
		return mContext;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//When context is null, the plugin is started by self
		if (mContext == null) {
			super.onCreate(savedInstanceState);
			mContext = this;
		}
		
		
	}
	
	@Override
	public void setContentView(int layoutResID) {
		if (mContext == this) {
			super.setContentView(layoutResID);
		}else {
			mContext.setContentView(layoutResID);
		}
	}
	
	@Override
	public void setContentView(View view) {
		if (mContext == this) {
			super.setContentView(view);
		}else {
			mContext.setContentView(view);
		}
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		if (mContext == this) {
			super.setContentView(view, params);
		}else {
			mContext.setContentView(view, params);
		}
	}
	
	@Override
	public void addContentView(View view, LayoutParams params) {
		if (mContext == this) {
			super.addContentView(view, params);
		}else {
			mContext.addContentView(view, params);
		}
	}
	
	@Override
	public void startActivity(Intent intent) {
		Log.d(TAG, "intent action = " + intent.getAction());
		if (mContext == this) {
			super.startActivity(intent);
		}else {
			//start activity by proxy activity
			Intent proxyIntent = new Intent(PROXY_ACTION);
			if (intent.getAction() == null) {
				//from new Intent(this, XXXActivity.class)
				ComponentName cn = intent.getComponent();
				proxyIntent.putExtra(INTENT_CLASS, cn.getClassName());
				Log.d(TAG, "intent action = " + cn.getClassName());
			}else {
				//from new Intent(action)
				proxyIntent.putExtra(INTENT_CLASS, intent.getAction());
			}
			
			proxyIntent.putExtra(EXTRA_DEXPATH, DEX_PATH);
			mContext.startActivity(proxyIntent);
		}
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		Log.d(TAG, "intent action = " + intent.getAction());
		if (mContext == this) {
			super.startActivityForResult(intent, requestCode);
		}else {
			//start activity by proxy activity
			Intent proxyIntent = new Intent(PROXY_ACTION);
			if (intent.getAction() == null) {
				//from new Intent(this, XXXActivity.class)
				ComponentName cn = intent.getComponent();
				proxyIntent.putExtra(INTENT_CLASS, cn.getClassName());
				Log.d(TAG, "intent action = " + cn.getClassName());
			}else {
				//from new Intent(action)
				proxyIntent.putExtra(INTENT_CLASS, intent.getAction());
			}
			
			proxyIntent.putExtra(EXTRA_DEXPATH, DEX_PATH);
			mContext.startActivityForResult(proxyIntent, requestCode);
		}
	}

}
