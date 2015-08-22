package com.example.dynamicplugin;


import com.example.dynamicloader.lifecircle.IActivityLifeCircle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class BasePluginActivity extends Activity implements IActivityLifeCircle{

	protected Activity mContext;
	private static final String PROXY_ACTIVITY_ACTION = "com.example.dynamicloader.ProxyActivity";
	private static final String PROXY_SERVICE_ACTION = "com.example.dynamicloader.ProxyService";
	protected static final String TAG = "BasePluginActivity";
	private final String INTENT_CLASS = "classname";
	private final String EXTRA_DEXPATH = "dexpath";
	private final String EXTRA_LIBPATH = "libpath";
	private String mDexPath;

	/**
	 * Set Host context
	 * @param at
	 */
	@Override
	public void setContext(Activity at, String dexPath) {
		mContext = at;
		mDexPath = dexPath;
//		mInstrumentation = new Instrumentation();
	}

	public Activity getContext() {
		return mContext;
	}

	/////////////////////////// Activity Life Circle //////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// When context is null, the plugin is started by self
		if (mContext == null) {
			super.onCreate(savedInstanceState);
			mContext = this;
		}
	}
	
	@Override
	protected void onStart() {
		if (mContext == this) {
			super.onStart();
		} 
	}

	@Override
	protected void onRestart() {
		if (mContext == this) {
			super.onRestart();
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
	
	
	/////////////////////// Resource //////////////////////

	@Override
	public void setContentView(int layoutResID) {
		if (mContext == this) {
			super.setContentView(layoutResID);
		} else {
			mContext.setContentView(layoutResID);
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

	
	///////////////////////////start activity service broadcast//////////////////////////////
	@Override
	public void startActivity(Intent intent) {
		Log.d(TAG, "intent action = " + intent.getAction());
		if (mContext == this) {
			super.startActivity(intent);
		} else {
			// start activity by proxy activity
			Intent proxyIntent = new Intent(PROXY_ACTIVITY_ACTION);
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
			Intent proxyIntent = new Intent(PROXY_ACTIVITY_ACTION);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mContext == this) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public ComponentName startService(Intent intent) {
		if (mContext == this) {
			return super.startService(intent);
		}else {
			Intent proxyIntent = new Intent(PROXY_SERVICE_ACTION);
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
			return mContext.startService(proxyIntent);
		}
	}


	@Override
	public void sendBroadcast(Intent intent) {
		mContext.sendBroadcast(intent);
	}
	
	
	@Override
	public void sendBroadcast(Intent intent, String receiverPermission) {
		mContext.sendBroadcast(intent, receiverPermission);
	}

	
	
	
	/////////////////////////// Called by Proxy Activity /////////////////////////////
	@Override
	public void callOnCreate(Bundle bundle) {
		Log.i(TAG, "callOnCreate");
		onCreate(bundle);
	}


	@Override
	public void callOnStart() {
		Log.i(TAG, "callOnStart");
		onStart();
	}
	
	@Override
	public void callOnRestart() {
		Log.i(TAG, "callOnRestart");
		onRestart();
	}
	
	@Override
	public void callOnResume() {
		Log.i(TAG, "callOnResume");
		onResume();
	}
	
	@Override
	public void callOnPause() {
		Log.i(TAG, "callOnPause");
		onPause();
	}
	
	@Override
	public void callOnStop() {
		Log.i(TAG, "callOnStop");
		onStop();
	}
	
	@Override
	public void callOnDestory() {
		Log.i(TAG, "callOnDestory");
		onDestroy();
	}

	@Override
	public void callOnActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResult(requestCode, resultCode, data);
	}

	
}
