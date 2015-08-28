package com.example.eventhandler;

import com.example.events.BaseEvent;

public interface IEventListener<T extends BaseEvent> {

	void handleEvent(T event);
}
