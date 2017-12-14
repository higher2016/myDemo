package com.higherli.asyn_syn_demo;

import com.higherli.asyn_syn_demo.event.asyn_event.AsynEvent;
import com.higherli.asyn_syn_demo.event.asyn_event.AsynEventDispatcher;
import com.higherli.asyn_syn_demo.event.asyn_event.AsynEventType;
import com.higherli.asyn_syn_demo.event.asyn_event.ListenAsynEvent;

public class TestAsynEventDispatcher {
	public static void main(String[] args) {
		Test1 obj = new Test1();
		AsynEventDispatcher.INSTANCE.addEventListeners(obj);
		AsynEventDispatcher.INSTANCE.addEventListeners(obj);
		AsynEventDispatcher.INSTANCE.addEventListeners(new Test0());
		AsynEventDispatcher.INSTANCE.addEventListeners(Test1.class);
		AsynEventDispatcher.INSTANCE.addEventListeners(Test1.class);
		AsynEventDispatcher.INSTANCE.start();
		AsynEventDispatcher.INSTANCE.dispatchEvent(new UnknownEvent("aaa"));
		AsynEventDispatcher.INSTANCE.dispatchEvent(new UnknownEvent("bb"));
		try {
			Thread.sleep(1L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AsynEventDispatcher.INSTANCE.shutdown();
	}

	private static class UnknownEvent implements AsynEvent {
		public final String name;

		private UnknownEvent(String name) {
			this.name = name;
		}

		public AsynEventType getEventType() {
			return AsynEventType.UNKNOWN;
		}
	}

	private static class Test0 {
		@ListenAsynEvent(eventType = AsynEventType.UNKNOWN)
		public void onUnknownEventHappen1(final AsynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			System.out.println("method1:" + ue.name);
		}

		@ListenAsynEvent(eventType = AsynEventType.UNKNOWN)
		public void onUnknownEventHappen0(final AsynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			System.out.println("method0:" + ue.name);
		}
	}

	private static class Test1 {
		@ListenAsynEvent(eventType = AsynEventType.UNKNOWN)
		static public void onUnknownEventHappen4(final AsynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			System.out.println("method4:" + ue.name);
		}

		@ListenAsynEvent(eventType = AsynEventType.UNKNOWN)
		public void onUnknownEventHappen2(final AsynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			System.out.println("method2:" + ue.name);
		}

		@ListenAsynEvent(eventType = AsynEventType.UNKNOWN)
		public void onUnknownEventHappen3(final AsynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			System.out.println("method3:" + ue.name);
		}

		@ListenAsynEvent(eventType = AsynEventType.UNKNOWN)
		static public void onUnknownEventHappen5(final AsynEvent event) {
			UnknownEvent ue = (UnknownEvent) event;
			System.out.println("method5:" + ue.name);
		}
	}
}
