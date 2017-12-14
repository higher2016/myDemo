package com.higherli.asyn_syn_demo.event;

/**
 * 抛出事件的抽象
 * 
 * @param <EventType>
 *            : 分为同步事件(SynEvent)和异步事件(ASynEvent)
 */
public interface Event<EventType> {
	EventType getEventType();
}
