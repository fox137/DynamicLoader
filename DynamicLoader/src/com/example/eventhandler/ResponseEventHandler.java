package com.example.eventhandler;

import android.util.Log;

import com.example.events.ResponseEvent;

public class ResponseEventHandler<T> extends BaseEventHandler<ResponseEvent<T>> {

	@Override
	public void handleEvent(ResponseEvent<T> event) {
		Log.d(TAG, "receive ResponseEvent: " + event);
		
	}

	
	@Override
	public void onEvent(ResponseEvent<T> event) {
		super.onEvent(event);
	}


}
