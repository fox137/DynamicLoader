package com.example.eventhandler;

import android.util.Log;

import com.example.events.RequestEvent;
import com.example.events.ResponseEvent;

public class RequestEventHandler<T> extends BaseEventHandler<RequestEvent<T>> {



	@Override
	public void handleEvent(RequestEvent<T> event) {
		Log.d(TAG, "receive RequestEvent: " + event);
//		if (event.action == RequestEvent.GET_ID) {
//			R r = (R) "sss";
//			ResponseEvent<R> re = new ResponseEvent<R>(event.action, r);
//			postEvent(re);
//		}
	}

	public void onEvent(RequestEvent<T> event){
		super.onEvent(event);
	}

}
