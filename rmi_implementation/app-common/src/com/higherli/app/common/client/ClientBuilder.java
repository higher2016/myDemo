package com.higherli.app.common.client;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.server.RMIClientSocketFactory;

import com.higherli.app.RemoteNode;
import com.higherli.app.common.server.ICRemote;
import com.higherli.app.common.server.MethodSign;

@SuppressWarnings("unchecked")
public class ClientBuilder {
	
	@Deprecated
	public static <T> T buildClient(Class<T> c, T defaultService, IClientWrapper wrapper, IClientMonitor monitor) {
		return (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[] { c },
				new RemoteHandler(defaultService, wrapper, new ClientInvokeMonitorImpl(monitor)));
	}

	static <T> T buildClient(Class<T> c, T defaultService, IClientWrapper wrapper, IClientInvokeMonitor monitor) {
		return (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[] { c },
				new RemoteHandler(defaultService, wrapper, monitor));
	}

	public static <T> T buildClient(Class<T> c, IClientWrapper wrapper, IClientMonitor monitor) {
		return buildClient(c, buildExceptionClient(c), wrapper, monitor);
	}

	public static <T> CClient<T> buildClient(Class<T> c, T defaultService, RemoteNode node) {
		return new CClient<>(node, c, defaultService);
	}

	public static <T> CClient<T> buildClient(Class<T> c, T defaultService, RemoteNode node,
			RMIClientSocketFactory csf) {
		return new CClient<>(node, c, defaultService, csf);
	}

	public static <T> CClient<T> buildClient(Class<T> c, RemoteNode node) {
		return buildClient(c, buildExceptionClient(c), node);
	}

	public static <T> T buildExceptionClient(Class<T> c) {
		return (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[] { c }, new ExceptionInvocationHandler());
	}

	private static class ExceptionInvocationHandler implements InvocationHandler, Serializable {
		private static final long serialVersionUID = 1987960023989463335L;

		public Object invoke(Object obj, Method m, Object[] args) throws Throwable {
			throw new CallFailedException(m);
		}
	}

	private static class RemoteHandler implements InvocationHandler, Serializable {
		public static final long serialVersionUID = 5183781473361476522L;
		private final Object obj;
		private final IClientWrapper wrapper;
		private final IClientInvokeMonitor monitor;

		private RemoteHandler(Object obj, IClientWrapper wrapper, IClientInvokeMonitor monitor) {
			this.obj = obj;
			this.wrapper = wrapper;
			this.monitor = monitor;
		}

		public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
			String cmd = method.getName();
			ClientInvokeContext ctx = new ClientInvokeContext();
			try {
				this.monitor.beforeHandle(ctx, method.getName(), args);
				ICRemote remote = this.wrapper.getRemoteClient();
				Object value = null;
				if (remote == null)
					value = method.invoke(this.obj, args);
				else {
					value = remote.exe(new MethodSign(method), args);
				}
				this.monitor.afterHandle(ctx, method.getName(), args);
				return value;
			} catch (Throwable t) {
				this.monitor.handleThrowable(ctx, cmd, args, t);
			}
			return method.invoke(this.obj, args);
		}
	}
}