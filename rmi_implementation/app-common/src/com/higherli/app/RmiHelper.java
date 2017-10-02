package com.higherli.app;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;

import org.apache.log4j.Logger;

/**
 * rmi工具类--主要是提供获取本机已注册的远程对象
 */
public class RmiHelper {
	private static final Logger LOG = Logger.getLogger(RmiHelper.class);

	@SuppressWarnings("unchecked")
	public static <T extends Remote> T getRemote(String name, int port) {
		try {
			Registry localRegistry = LocateRegistry.getRegistry(port);
			Remote t = localRegistry.lookup(name);
			return (T) t;
		} catch (Exception e) {
			warn(String.format("<RmiHelper> get local Remote error! port[%d] name[%s]", new Object[] { port, name }),
					e);
		}
		return null;
	}

	public static <T extends Remote> T getRemote(String name, String ip, int port) {
		return getRemote(name, ip, port, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Remote> T getRemote(String name, String ip, int port, RMIClientSocketFactory csf) {
		try {
			Registry registry = LocateRegistry.getRegistry(ip, port, csf);
			Remote t = registry.lookup(name);
			return (T) t;
		} catch (Exception e) {
			warn(String.format("<RmiHelper> get local Remote error! ip[%s] port[%d] name[%s]",
					new Object[] { ip, port, name }), e);
		}
		return null;
	}

	public static void warn(String msg, Throwable t) {
		LOG.error(msg, t);
	}
}
