package com.higherli.app.common.server;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取被 {@link Service @Service} 注解类(标注为远程对象)中的方法集合
 * 
 * @author Admin
 *
 */
public class CServerHelper {

	/**
	 * @param c
	 *            :被{@link Service @Service}修饰的类(标注为远程对象)
	 * @return c类(远程对象，包括其父类)的方法集合
	 */
	static Set<Method> findServiceMethods(Class<?> c) {
		Set<Class<?>> interfaceSet = findServiceInterface(c);
		Set<Method> methodSet = new HashSet<>();
		for (Class<?> i : interfaceSet) {
			methodSet.addAll(Arrays.asList(i.getMethods()));
		}
		return methodSet;
	}

	static Set<Class<?>> findServiceInterface(Class<?> c) {
		Set<Class<?>> interfaceSet = getInterfaces(c);
		HashSet<Class<?>> serviceIs = new HashSet<>();
		for (Class<?> i : interfaceSet) {
			if (i.isAnnotationPresent(Service.class)) {
				serviceIs.add(i);
			}
		}
		return serviceIs;
	}

	static Set<Class<?>> getInterfaces(Class<?> c) {
		if (c == null) {
			return new HashSet<Class<?>>(0);
		}
		HashSet<Class<?>> interfaceSet = new HashSet<>();
		interfaceSet.addAll(Arrays.asList(c.getInterfaces()));
		// 鐖剁被涔熶細琚姞杞�
		interfaceSet.addAll(Arrays.asList(c.getSuperclass()));
		return interfaceSet;
	}
}
