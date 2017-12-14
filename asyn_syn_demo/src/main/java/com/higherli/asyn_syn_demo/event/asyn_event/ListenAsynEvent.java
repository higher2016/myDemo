package com.higherli.asyn_syn_demo.event.asyn_event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于监听异步事件
 * 
 * @author huangjunjie
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenAsynEvent {
	public AsynEventType eventType();

	public AsynEventProcessPriority processPriority() default AsynEventProcessPriority.DEFAULT;
}

