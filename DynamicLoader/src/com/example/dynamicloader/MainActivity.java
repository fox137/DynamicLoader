package com.example.dynamicloader;

import java.io.File;

import com.example.dynamicloader.data.PluginComponent;
import com.example.eventhandler.EventHandlerMananger;
import com.example.eventhandler.IEventListener;
import com.example.eventhandler.RequestEventHandler;
import com.example.eventhandler.ResponseEventHandler;
import com.example.events.RequestEvent;
import com.example.events.ResponseEvent;

import de.greenrobot.event.EventBus;

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
 * 
 * @author xufeng
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
//		PluginManager.getManager().initPlugin(this, PluginManager.PATH_PLUGIN_A);
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		PluginManager.getManager().releasePlugin(this, PluginManager.PATH_PLUGIN_A);
	}

	private void loadActivity() {
		Intent intent = new Intent(ProxyActivity.ACTION);
		intent.putExtra(ProxyActivity.EXTRA_CLASS,
				PluginManager.getManager().getPlugin(this, PluginManager.PATH_PLUGIN_A)
						.getComponent().getActivityByAction(PluginComponent.ACTION_MAIN));
		intent.putExtra(ProxyActivity.EXTRA_DEXPATH, PluginManager.PATH_PLUGIN_A);
		startActivity(intent);
	}

	private void initView() {
		findViewById(R.id.btn_load1).setOnClickListener(this);
		findViewById(R.id.btn_load_service1).setOnClickListener(this);
		findViewById(R.id.btn_gotoevent).setOnClickListener(this);
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
		case R.id.btn_gotoevent:
			startActivity(new Intent(this, TestBusActivity.class));
			break;

		default:
			break;
		}

	}


	private void loadService1() {
		String pluginPath = Environment.getExternalStorageDirectory() + File.separator + "dl"
				+ File.separator + "DynamicPlugin.apk";
		PackageInfo pi = this.getPackageManager().getPackageArchiveInfo(pluginPath,
				PackageManager.GET_SERVICES);
		String sName = pi.services[0].name;
		Intent intent = new Intent(ProxyService.ACTION);
		intent.putExtra(ProxyActivity.EXTRA_DEXPATH, PluginManager.PATH_PLUGIN_A);

	}
}
