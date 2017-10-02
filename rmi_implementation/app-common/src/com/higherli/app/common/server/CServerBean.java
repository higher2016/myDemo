package com.higherli.app.common.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.higherli.app.server.IService;

/**
 * 封装服务器对象，不让客户端直接调用服务器的远端对象，而是通过CServerBean这个中间层来进行映射调用
 */
public class CServerBean {
	private Map<MethodSign, Method> cmdHandlerMap = new HashMap<>();
	private IService service;

	public CServerBean(IService service) {
		this.service = service;
		initMethods();
	}

	private void initMethods() {
		Class<?> c = this.service.getClass();
		Set<Method> methods = CServerHelper.findServiceMethods(c);
		for (Method m : methods) {
			this.cmdHandlerMap.put(new MethodSign(m), m);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T exe(MethodSign methodSign, Object[] args) throws Exception {
		Method m = (Method) this.cmdHandlerMap.get(methodSign);
		Object ret = null;
		if (m != null)
			ret = m.invoke(this.service, args);
		else
			throw new IllegalArgumentException(String.format("cmd[%s] not found!", new Object[] { methodSign.toString() }));
		return (T) ret;
	}

	public IService getService() {
		return this.service;
	}
}
