package com.example.events;

public class RequestEvent<T> extends BaseEvent{
	
	public static final int GET_ID = 1;
	
	public T data;

	public RequestEvent(int action) {
		super(action);
	}

	public RequestEvent(int action, T data) {
		super(action);
		this.data = data;
	}
	
	
}
