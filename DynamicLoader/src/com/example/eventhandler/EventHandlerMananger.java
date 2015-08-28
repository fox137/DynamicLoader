package com.example.eventhandler;

import java.util.ArrayList;
import java.util.List;


public class EventHandlerMananger {

	private static EventHandlerMananger MANAGER = new EventHandlerMananger();
	
	private List<BaseEventHandler> mEventHandlers;
	
	private EventHandlerMananger(){
		mEventHandlers = new ArrayList<BaseEventHandler>();
	}
	
	public static EventHandlerMananger getMananger(){
		return MANAGER;
	}
	
	public void registerEventHandler(BaseEventHandler handler){
		if (handler == null) {
			return;
		}
//		for (BaseEventHandler h : mEventHandlers) {
//			if (h.getClass().getName().equals(handler.getClass().getName())) {
//				h.register();
//				return;
//			}
//		}
		mEventHandlers.add(handler);
		handler.register();
	}
	
	public void unregisterEventHandler(BaseEventHandler handler){
		if (handler == null) {
			return;
		}
		mEventHandlers.remove(handler);
		
		for (BaseEventHandler h : mEventHandlers) {
			if (h.getClass().getName().equals(handler.getClass().getName())) {
				return;
			}
		}
		handler.unregister();
	}
	
}
