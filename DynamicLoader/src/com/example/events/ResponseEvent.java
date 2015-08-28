package com.example.events;

public class ResponseEvent<T> extends BaseEvent {
	
	public T data;

	public ResponseEvent(int action) {
		super(action);
	}
	
	public ResponseEvent(int action, T data) {
		super(action);
		this.data = data;
	}

}
