package com.example.eventhandler;

import android.util.Log;

import com.example.events.BaseEvent;

import de.greenrobot.event.EventBus;

public abstract class BaseEventHandler <T extends BaseEvent>{
	protected String TAG = this.getClass().getSimpleName();
	
	protected IEventListener<T> mListener;
//	protected int mRegisterTime = 0;
	
	public BaseEventHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public void addListener(IEventListener<T> listener) {
		mListener = listener;
	}

	public abstract void handleEvent(final T event);
	
	/**
	 * The subclass must override this method and change protected to public because of EventBus 
	 * @param event
	 */
	protected void onEvent(T event){
		Log.i(TAG, "onEvent: " + event);
		handleEvent(event);
		callBack(event);
	}
	
	public void register(){
		if (!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
//		mRegisterTime++;
	}
	
	public void unregister(){
//		boolean unreg = false;
//		if (mRegisterTime <= 0) {
			EventBus.getDefault().unregister(this);
//			unreg = true;
//		}
//		mRegisterTime--;
//		return unreg;
	}
	
	protected void postEvent(BaseEvent e){
		EventBus.getDefault().post(e);
	}
	
	protected void callBack(T event){
		if (mListener != null) {
			mListener.handleEvent(event);
		}
	}
}
