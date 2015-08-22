package com.example.dynamicplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.dynamicplugin.R;

public class MainActivity extends BasePluginActivity implements OnClickListener{
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plugin_main);
		findViewById(R.id.pl_btn_jumpinner).setOnClickListener(this);
		findViewById(R.id.pl_btn_startservice).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pl_btn_jumpinner:
			startActivity(new Intent(mContext, InnerActivity.class));
			break;
			
		case R.id.pl_btn_startservice:
			startService(new Intent("com.example.dynamicplugin.MainService"));
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "plug main onResume");
	}
}
