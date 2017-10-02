package com.higherli.app.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.higherli.app.RmiHelper;
import com.higherli.app.dto.ISystem;

/**
 * 远程对象管理类：注册表的创建:createServer(int port)并将注册表缓存到对象中（registry）.
 * 并将本地的所有远程对象都纳入到remoteMap缓存中管理.类中封装了注册表的创建和销毁方法.    
 * 并且在类创建的时候会向该对象绑定一个系统远程对象：MySystemRemote
 */
public class AppServer {
	private static final Map<Integer, AppServer> port2Server = new HashMap<>();

	private static final Logger LOG = Logger.getLogger(AppServer.class);
	public static final String SYSTEM_SERVICE = "_system";
	public final int port;
	private Registry registry;
	private final Map<String, AppRemoteObject> remoteMap = new HashMap<>();
	private ShutdownHook destroyHook = new ShutdownHook() {
		public void onDestroy() {
		}
	};

	public AppServer(int port, Registry registry) {
		super();
		this.port = port;
		this.registry = registry;
		bindSystemService();
	}

	public static synchronized AppServer createServer(int port) {
		return createCustomServer(port, null, null);
	}

	private static synchronized AppServer createCustomServer(int port, RMIClientSocketFactory csf,
			RMIServerSocketFactory ssf) {
		try {
			if (port2Server.containsKey(port))
				return port2Server.get(port);
			Registry localReistry;
			localReistry = LocateRegistry.createRegistry(port, csf, ssf);
			AppServer server = new AppServer(port, localReistry);
			port2Server.put(port, server);
			return server;
		} catch (RemoteException e) {
			RmiHelper.warn("", e);
		}
		return null;
	}

	public static void setLocalHostName(String hostName) {
		System.setProperty("java.rmi.server.hostname", hostName);
	}

	public void destroy() {
		LOG.info("Shuting down...");
		try {
			this.registry.unbind(SYSTEM_SERVICE);
			Collection<String> services = new HashSet<String>();
			services.addAll(this.remoteMap.keySet());
			for (String serviceName : services)
				_unbingService(serviceName);
		} catch (Exception e) {
			RmiHelper.warn("", e);
		}
		this.destroyHook.onDestroy();
		LOG.info("Server Shutdown!");
		System.exit(0);
	}

	private void _unbingService(String serviceName) {
		try {
			if (this.remoteMap.containsKey(serviceName))
				this.registry.unbind(serviceName);
			this.remoteMap.get(serviceName).onUnbind();
		} catch (Throwable t) {
			RmiHelper.warn("", t);
		}
	}

	private void bindSystemService() {
		try {
			this.registry.bind(SYSTEM_SERVICE, new MySystemRemote());
		} catch (RemoteException | AlreadyBoundException e) {
			RmiHelper.warn("", e);
		}
	}

	public boolean bindService(String remoteName, AppRemoteObject remoteObject) {
		try {
			_bindService(remoteName, remoteObject);
			return true;
		} catch (Exception e) {
			RmiHelper.warn("", e);
		}
		return false;
	}

	private void _bindService(String name, AppRemoteObject remoteObject) throws Exception {
		remoteObject.onBind();
		this.registry.bind(name, remoteObject);// 向注册表中绑定远程对象
		this.remoteMap.put(name, remoteObject);
	}

	public static interface ShutdownHook {
		public void onDestroy();
	}

	private class MySystemRemote extends AppRemoteObject implements ISystem {

		private static final long serialVersionUID = -1558047238491269202L;

		protected MySystemRemote() throws RemoteException {
		}

		@Override
		public String stopserver() throws RemoteException {
			new Thread(new Runnable() {
				@Override
				public void run() {
					AppServer.this.destroy();
				}
			}).start();
			return "Stop command reveived, app shuting down!";
		}

		@Override
		public String[] checkService() throws RemoteException {
			return (String[]) AppServer.this.remoteMap.keySet().toArray(new String[0]);
		}

	}
}
