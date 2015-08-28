package com.example.events;

public class BaseEvent {
	
	public int action;
	
	public BaseEvent(int action) {
		this.action = action;
	}
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" action=").append(action);
		return sb.toString();
	}

}
