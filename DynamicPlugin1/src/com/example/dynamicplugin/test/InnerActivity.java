package com.example.dynamicplugin.test;

import android.os.Bundle;
import android.widget.TextView;

import com.example.dynamicplugin.BasePluginActivity;
import com.example.dynamicplugin.R;
import com.example.dynamicplugin.R.layout;

public class InnerActivity extends BasePluginActivity {
	
	public static final String TAG = "InnerActivity";
	public static final String ACTION = "com.example.dynamicplugin1.InnerActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_plugin_inner);
	}

}
