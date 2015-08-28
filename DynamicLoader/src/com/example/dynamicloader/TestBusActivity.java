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

public class TestBusActivity extends Activity implements OnClickListener {

	private static final String TAG = "TestBusActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus);
		initView();
		registerEventHandler();
	}
	
	private void registerEventHandler() {
		
		ResponseEventHandler<String> handler2 = new ResponseEventHandler<String>();
		handler2.addListener(new IEventListener<ResponseEvent<String>>() {
			@Override
			public void handleEvent(ResponseEvent<String> event) {
				Log.d(TAG, "mainactivity handleEvent2 action=" + event.action + ", data=" + event.data);
			}
		});
		EventHandlerMananger.getMananger().registerEventHandler(handler2);
	}



	private void initView() {
		findViewById(R.id.btn_postevent).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_postevent:
			break;

		default:
			break;
		}

	}


}
