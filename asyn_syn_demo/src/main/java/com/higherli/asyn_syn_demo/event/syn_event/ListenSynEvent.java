package com.higherli.asyn_syn_demo.event.syn_event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于监听同步事件
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenSynEvent {
	public SynEventType eventType();

	public SynEventProcessPriority processPriority() default SynEventProcessPriority.DEFAULT;
}
