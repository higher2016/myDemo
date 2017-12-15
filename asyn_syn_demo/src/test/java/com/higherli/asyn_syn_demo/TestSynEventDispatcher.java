package com.higherli.asyn_syn_demo;

import com.higherli.asyn_syn_demo.event.syn_event.ListenSynEvent;
import com.higherli.asyn_syn_demo.event.syn_event.SynEvent;
import com.higherli.asyn_syn_demo.event.syn_event.SynEventDispatcher;
import com.higherli.asyn_syn_demo.event.syn_event.SynEventType;

public class TestSynEventDispatcher {
	public static void main(String[] args) {
		Test1 obj = new Test1();
		SynEventDispatcher.INSTANCE.addEventListeners(obj);
		SynEventDispatcher.INSTANCE.addEventListeners(obj);
		SynEventDispatcher.INSTANCE.addEventListeners(new Test0());
		SynEventDispatcher.INSTANCE.addEventListeners(Test1.class);
		SynEventDispatcher.INSTANCE.addEventListeners(Test1.class);
		SynEventDispatcher.INSTANCE.dispatchEvent(new UnknownEvent("aaa"));
		SynEventDispatcher.INSTANCE.dispatchEvent(new UnknownEvent("bb"));
	}

	private static class UnknownEvent implements SynEvent {
		public final String name;

		private UnknownEvent(String name) {
			this.name = name;
		}

		public SynEventType getEventType() {
			return SynEventType.UNKNOWN;
		}
	}

	private static class Test0 {
		@ListenSynEvent(eventType = SynEventType.UNKNOWN)
		public void onUnknownEventHappen1(final SynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			AppLogger.info("method1:" + ue.name);
		}

		@ListenSynEvent(eventType = SynEventType.UNKNOWN)
		public void onUnknownEventHappen0(final SynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			AppLogger.info("method0:" + ue.name);
		}
	}

	private static class Test1 {
		@ListenSynEvent(eventType = SynEventType.UNKNOWN)
		static public void onUnknownEventHappen4(final SynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			AppLogger.info("method4:" + ue.name);
		}

		@ListenSynEvent(eventType = SynEventType.UNKNOWN)
		public void onUnknownEventHappen2(final SynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			AppLogger.info("method2:" + ue.name);
		}

		@ListenSynEvent(eventType = SynEventType.UNKNOWN)
		public void onUnknownEventHappen3(final SynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			AppLogger.info("method3:" + ue.name);
		}

		@ListenSynEvent(eventType = SynEventType.UNKNOWN)
		static public void onUnknownEventHappen5(final SynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			AppLogger.info("method5:" + ue.name);
		}
	}
}
