package com.example.dynamicplugin1;

import android.os.Bundle;
import android.widget.TextView;

public class InnerActivity extends BaseActivity {
	
	public static final String TAG = "InnerActivity";
	public static final String ACTION = "com.example.dynamicplugin1.InnerActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TextView tv = new TextView(mContext);
		tv.setText("inner");
		setContentView(tv);
	}

}
