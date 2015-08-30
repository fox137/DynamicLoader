package com.example.dynamicplugin.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MainReceiver extends BroadcastReceiver {

	private static final String TAG = "MainReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "receiver intent");
	}

}
