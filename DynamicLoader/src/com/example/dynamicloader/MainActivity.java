package com.example.dynamicloader;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Load plugin by ProxyActivity with pluginPath
 * @author xufeng
 *
 */
public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	


	private void load1() {
		String pluginPath = Environment.getExternalStorageDirectory() +
				File.separator + "dl" + File.separator + "DynamicPlugin1.apk";
		Intent intent = new Intent(ProxyActivity.ACTION);
		intent.putExtra(ProxyActivity.EXTRA_DEXPATH, pluginPath);
		startActivity(intent);
	}
	
	
	private void initView() {
		findViewById(R.id.btn_load1).setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_load1:
			load1();
			break;

		default:
			break;
		}
		
	}
}
