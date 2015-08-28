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
import com.example.events.EventTest;
import com.example.events.EventTestBack;

import de.greenrobot.event.EventBus;

public class MainActivity extends BasePluginActivity implements OnClickListener{
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plugin_main);
		findViewById(R.id.pl_btn_jumpinner).setOnClickListener(this);
		findViewById(R.id.pl_btn_startservice).setOnClickListener(this);
		findViewById(R.id.pl_btn_event).setOnClickListener(this);
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

		case R.id.pl_btn_event:
			postEvent();
			break;
		default:
			break;
		}
	}
	
	private void postEvent() {
		// TODO Auto-generated method stub
		EventBus.getDefault().post(new EventTest("plugin"));
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "plug main onResume");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}
	
	@Override
	protected void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}
	
	public void onEvent(EventTestBack e){
		Log.d(TAG, "plugin receive event: " + e);
	}
}
