package com.example.dynamicloader;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Load plugin by ProxyActivity with pluginPath
 * @author xufeng
 *
 */
public class MainActivity extends Activity implements OnClickListener{

	private static final String TAG = "MainActivity";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	


	private void loadActivity() {
//		String pluginPath = Environment.getExternalStorageDirectory() +
//				File.separator + "dl" + File.separator + "Plugin.apk";
//		String pluginPath = Environment.getExternalStorageDirectory() +
//		File.separator + "dl" + File.separator + "DynamicPlugin.apk";
		
		PackageInfo pi = this.getPackageManager().getPackageArchiveInfo(PluginManager.PLUGIN_PATH1, PackageManager.GET_ACTIVITIES);
		String atName = pi.activities[0].name;
		String libDir = pi.applicationInfo.nativeLibraryDir;
		Log.d(TAG,  " activityname=" + atName + ", libdir=" + libDir);
		
		Intent intent = new Intent(ProxyActivity.ACTION);
		intent.putExtra(ProxyActivity.EXTRA_DEXPATH, PluginManager.PLUGIN_PATH1);
		intent.putExtra(ProxyActivity.EXTRA_CLASS, atName);
		intent.putExtra(ProxyActivity.EXTRA_LIBPATH, libDir);
		startActivity(intent);
	}
	
	
	private void initView() {
		findViewById(R.id.btn_load1).setOnClickListener(this);
		findViewById(R.id.btn_load_service1).setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_load1:
			loadActivity();
			break;
		case R.id.btn_load_service1:
			loadService1();
			break;

		default:
			break;
		}
		
	}



	private void loadService1() {
		String pluginPath = Environment.getExternalStorageDirectory() +
				File.separator + "dl" + File.separator + "DynamicPlugin.apk";
		PackageInfo pi = this.getPackageManager().getPackageArchiveInfo(pluginPath, PackageManager.GET_SERVICES);
		String sName = pi.services[0].name;
		Intent intent = new Intent(ProxyService.ACTION);
		intent.putExtra(ProxyService.EXTRA_CLASSNAME, sName);
		startService(intent);
		
	}
}
