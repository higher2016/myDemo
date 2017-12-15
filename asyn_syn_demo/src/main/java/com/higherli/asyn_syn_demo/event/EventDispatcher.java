package com.higherli.asyn_syn_demo.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.higherli.asyn_syn_demo.AppLogger;

/**
 * 事件分发器(事件产生之后调用dispatchEvent(E event))
 * 
 * @param <T>
 *            事件类型
 * @param <E>
 *            事件
 * @param <P>
 *            事件处理优先级
 */
public abstract class EventDispatcher<T extends Enum<T>, E extends Event<T>, P extends Comparable<P>> {
	private final HashMap<T, List<EventListener<P>>> eventType_EventListenerList;
	protected final EnumMap<T, EventListener<P>[]> eventType_EventListeners; // 监听同步事件的方法
	private final HashSet<Object> objectSet = new HashSet<Object>(); // 所有对象状态的监听者(即监听者是在对象中的方法)
	private final HashSet<Class<?>> staticClassSet = new HashSet<Class<?>>(); // 所有静态方法监听者
	private final String eventDispatcherName;
	private final Class<E> eventClass;
	private final Class<? extends Annotation> listenAnnotationClass;

	public EventDispatcher(Class<T> eventTypeClass, Class<E> eventClass, Class<? extends Annotation> listenAnnotationClass) {
		this.eventClass = eventClass;
		this.listenAnnotationClass = listenAnnotationClass;
		eventType_EventListenerList = new HashMap<T, List<EventListener<P>>>();
		eventType_EventListeners = new EnumMap<T, EventListener<P>[]>(eventTypeClass);
		eventDispatcherName = this.getClass().getSimpleName();
	}

	/**
	 * 抛出事件(事件产生后调用)
	 */
	public abstract void dispatchEvent(E event);

	/**
	 * 适用于监听者是具体对象的情况
	 * 
	 * @param obj
	 */
	public synchronized void addEventListeners(Object obj) {
		if (objectSet.add(obj)) {
			addEventListeners0(obj, obj.getClass(), false);
		}
	}

	public synchronized void addEventListeners(Class<?> staticClass) {
		if (staticClassSet.add(staticClass)) {
			addEventListeners0(null, staticClass, true);
		}
	}

	/**
	 * 将监听者的方法和对象注册到分发器中,当事件产生监听器就会监听到,并调相应的方法处理
	 * 
	 * @param obj
	 * @param clazz
	 * @param mustBeStaticOrNot
	 */
	@SuppressWarnings("unchecked")
	private void addEventListeners0(Object obj, Class<? extends Object> clazz, boolean mustBeStaticOrNot) {
		if (clazz == null || clazz == Object.class) {
			return;
		}
		// if (!mustBeStaticOrNot) { // 上溯父类
		// addEventListeners0(obj, clazz.getSuperclass(), mustBeStaticOrNot);
		// }
		int listenerNum = 0;
		String className = clazz.getSimpleName();
		for (Method method : clazz.getDeclaredMethods()) {
			// Modifier:用于判断类、方法、成员变量的修饰符
			if (Modifier.isStatic(method.getModifiers()) != mustBeStaticOrNot) {
				continue;
			}
			String methodDescription = String.format("%s#%s()", className, method.getName());
			try {
				Annotation listenAnnotation = method.getAnnotation(listenAnnotationClass);
				if (listenAnnotation == null) {
					continue;
				}
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes == null || parameterTypes.length != 1 || parameterTypes[0] != eventClass) {
					AppLogger.error(String.format("<%s> The parameter types of %s are wrong.", eventDispatcherName, methodDescription));
					continue;
				}
				T eventType = (T) listenAnnotationClass.getMethod("eventType").invoke(listenAnnotation); // 获取元注解中的eventType
				P processPriority = (P) listenAnnotationClass.getMethod("processPriority").invoke(listenAnnotation);
				List<EventListener<P>> eventListenerList = eventType_EventListenerList.get(eventType);
				if (eventListenerList == null) {
					eventListenerList = new ArrayList<EventListener<P>>();
					eventType_EventListenerList.put(eventType, eventListenerList);
				}
				method.setAccessible(true);
				eventListenerList.add(new EventListener<P>(processPriority, obj, method, methodDescription));
				Collections.sort(eventListenerList);
				eventType_EventListeners.put(eventType, eventListenerList.toArray(new EventListener[eventListenerList.size()]));
				listenerNum++;
			} catch (Throwable t) {
				AppLogger.error(String.format("<%s> Make %s be a listener unsuccessfully.", eventDispatcherName, methodDescription));
			}
			if (listenerNum <= 0) {
				AppLogger.error(String.format("<%s> Listener method not found in class:%s.", eventDispatcherName, className));
			}
		}
	}

	/**
	 * 按顺序执行注册的监听该事件的方法
	 * 
	 * @param eventType
	 *            : 用于声明事件类型
	 * @param event
	 *            : 事件
	 */
	protected void doEvent(T eventType, E event) {
		EventListener<P>[] eventListeners = eventType_EventListeners.get(eventType);
		if (eventListeners == null) {
			return;
		}
		for (EventListener<P> eventListener : eventListeners) {
			try {
				eventListener.method.invoke(eventListener.instance, event);
			} catch (Throwable t) {
				System.err.println(String.format("<%s> invoke %s error.", eventDispatcherName, eventListener.methodDescription));
			}
		}
	}

	/**
	 * 监听者对象(包括监听者的要执行的监听方法、优先级、方法描述)
	 *
	 * @param <P>
	 */
	protected static class EventListener<P extends Comparable<P>> implements Comparable<EventListener<P>> {
		public final P processPriority;
		public final Object instance;
		public final Method method;
		public final String methodDescription;

		private EventListener(P processPriority, Object instance, Method method, String methodDescription) {
			this.processPriority = processPriority;
			this.instance = instance;
			this.method = method;
			this.methodDescription = methodDescription;
		}

		public int compareTo(EventListener<P> o) {
			return processPriority.compareTo(o.processPriority);
		}
	}
}
