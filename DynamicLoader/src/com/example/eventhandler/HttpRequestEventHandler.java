package com.example.eventhandler;

import java.util.List;

import com.example.events.HttpReqInfo;
import com.example.events.HttpRequestEvent;
import com.example.events.HttpResponseEvent;
import com.example.events.RequestEvent;
//import com.lenovo.vcs.weaverth.httphelper.HttpHelper;
//import com.lenovo.vcs.weaverth.main.YouyueApplication;
//import com.lenovo.vctl.weaverth.model.Response;
//import com.lenovo.vctl.weaverth.net.IHttpCallback;
//import com.lenovo.vctl.weaverth.net.NetConnect;

public class HttpRequestEventHandler<T> extends BaseEventHandler<HttpRequestEvent<T>> {

	@Override
	public void handleEvent(final HttpRequestEvent<T> event) {
//		HttpReqInfo<T> info = event.info;
//		HttpHelper.request(YouyueApplication.getYouyueAppContext(), info.choice, info.params, 
//				info.handler,  NetConnect.POST, new IHttpCallback<T>() {
//
//					@Override
//					public void onResult(Response<List<T>> response) {
//						HttpResponseEvent<T> responseEvent = new HttpResponseEvent<T>(event.action, response);
//						postEvent(responseEvent);
//					}
//		});
		
	}

	
	@Override
	public void onEvent(HttpRequestEvent<T> event) {
		super.onEvent(event);
	}
}
