package com.higherli.asyn_syn_demo.event.syn_event;

import com.higherli.asyn_syn_demo.event.EventDispatcher;

/**
 * 同步事件分发器的实现
 */
public class SynEventDispatcher extends EventDispatcher<SynEventType, SynEvent, SynEventProcessPriority> {

	public static final SynEventDispatcher INSTANCE = new SynEventDispatcher();

	private SynEventDispatcher() {
		super(SynEventType.class, SynEvent.class, ListenSynEvent.class);
	}

	@Override
	public void dispatchEvent(SynEvent event) {
		SynEventType eventType = event.getEventType();
		if (eventType == null) {
			throw new RuntimeException("<SynEventDispatcher> event type not exist!");
		}
		doEvent(eventType, event);
	}

}
