package com.example.dynamicplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.example.dynamicplugin.R;
import com.example.events.RequestEvent;
import com.lenovo.vcs.weaverth.trade.utils.ProtectJni;

import de.greenrobot.event.EventBus;

public class MainActivity extends BasePluginActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plugin_main);
		findViewById(R.id.pl_btn_jumpinner).setOnClickListener(this);
		findViewById(R.id.pl_btn_startservice).setOnClickListener(this);
		findViewById(R.id.pl_btn_event).setOnClickListener(this);
		findViewById(R.id.pl_btn_loadso).setOnClickListener(this);
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
		case R.id.pl_btn_loadso:
			Log.i(TAG, "load so: " + ProtectJni.alpbk());
			break;
		default:
			break;
		}
	}
	
	private void postEvent() {
		// TODO Auto-generated method stub
		EventBus.getDefault().post(new RequestEvent<String>(1));
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "plug main onResume");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
}
