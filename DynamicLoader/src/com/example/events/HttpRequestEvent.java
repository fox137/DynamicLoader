package com.example.events;

public class HttpRequestEvent<T> extends BaseEvent {

	public HttpReqInfo<T> info;
	
	public HttpRequestEvent(int action) {
		super(action);
	}
	
	public HttpRequestEvent(int action, HttpReqInfo<T> reqData) {
		super(action);
		info = reqData;
	}

}
