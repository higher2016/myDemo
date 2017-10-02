package com.aoyi.app_common_rmi.rmi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.higherli.app.RemoteNode;
import com.higherli.app.common.client.CClient;
import com.higherli.app.common.client.ClientBuilder;
import com.higherli.app.common.client.IClientInvokeMonitor;
import com.higherli.app.common.server.Service;
import com.higherli.app.server.IService;

public class RemoteServiceProvider<T> {
	private static final HashMap<String, RemoteServiceProvider<?>> map = new HashMap<String, RemoteServiceProvider<?>>();

	private final RemoteNode remoteNode;
	private CClient<T> client;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> RemoteServiceProvider createProviderIfAbsentThenGet(RemoteNode remoteNode, T defaultService) {
		RemoteServiceProvider<?> provider = null;
		synchronized (map) {
			if (map.containsKey(remoteNode.name)) {
				provider = map.get(remoteNode.name);
			} else {
				provider = new RemoteServiceProvider(remoteNode, (IService) defaultService);
				map.put(remoteNode.name, provider);
			}
		}
		return provider;
	}

	@SuppressWarnings({ "rawtypes" })
	public static <T> RemoteServiceProvider getProvider(String remoteName) {
		return map.get(remoteName);
	}

	public RemoteServiceProvider(RemoteNode remoteNode, T defaultService) {
		this.remoteNode = remoteNode;
		loadClient(defaultService);
	}

	@SuppressWarnings("unchecked")
	private void loadClient(T defaultService) {
		Class<T> clazz = (Class<T>) findServiceInterface(defaultService.getClass());
		CClient<T> client = ClientBuilder.buildClient(clazz, defaultService, remoteNode);
		synchronized (this) {
			this.client = client;
		}
	}

	private Class<?> findServiceInterface(Class<?> clazz) {
		for (Class<?> c : getInstance(clazz)) {
			if (c.isAnnotationPresent(Service.class)) {
				return c;
			}
		}
		throw new RuntimeException(String.format("<RemoteServiceProvider>" + "%s's interfaces are not present annotation(com.baitian.app.common.server.Service)", clazz.getName()));
	}

	private Set<Class<?>> getInstance(Class<?> c) {
		if (c == null) {
			return new HashSet<Class<?>>(0);
		}

		HashSet<Class<?>> set = new HashSet<Class<?>>();
		set.addAll(Arrays.asList(c.getInterfaces()));
		set.addAll(getInstance(c.getSuperclass()));
		return set;
	}

	final public synchronized T getService() {
		return client.getService();
	}

	public synchronized void setMonitor(IClientInvokeMonitor monitor) {
		client.setMonitor(monitor);
	}
}
