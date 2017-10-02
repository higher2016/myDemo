package com.higherli.app.common.client;

import java.net.ConnectException;
import java.rmi.server.RMIClientSocketFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.higherli.app.RemoteNode;
import com.higherli.app.RmiHelper;
import com.higherli.app.common.server.ICRemote;

public class CClient<T> {
	private static final Logger LOG = Logger.getLogger(CClient.class);
	public static final int DEFAULT_RELOAD_CD = 5000;
	private static final IClientInvokeMonitor EmptyMonitor = new IClientInvokeMonitor() {
		public void afterHandle(ClientInvokeContext ctx, String cmd, Object[] params) {
		}

		public void beforeHandle(ClientInvokeContext ctx, String cmd, Object[] params) {
		}

		public void handleThrowable(ClientInvokeContext ctx, String cmd, Object[] params, Throwable t) {
		}
	};
	public final RemoteNode node;
	public final Class<T> serviceClass;
	private final T defaultService;
	private final RMIClientSocketFactory csf;
	private volatile CClient<T>.DefaultRemoteClientMonitor monitor;
	private volatile int reloadCD = 5000;
	private long lastReloadTime = 0L;
	private boolean enableAutoReload = true;

	private volatile ICRemote remote = null;
	private volatile T service;

	CClient(RemoteNode node, Class<T> serviceClass, T defaultService) {
		this(node, serviceClass, defaultService, null);
	}

	CClient(RemoteNode node, Class<T> serviceClass, T defaultService, RMIClientSocketFactory csf) {
		this.node = node;
		this.serviceClass = serviceClass;
		this.defaultService = defaultService;
		this.csf = csf;
		this.monitor = new DefaultRemoteClientMonitor(this);
		autoReloadWithDelayCD();
	}

	public T getService() {
		if ((this.service == null) && (!autoReloadWithDelayCD())) {
			return this.defaultService;
		}

		return this.service;
	}

	public ICRemote getRemote() {
		if ((this.remote == null) && (!autoReloadWithDelayCD())) {
			return null;
		}

		return this.remote;
	}

	/**
	 * 实现五秒重连的机制非常简单，但这个是客户端调用时才会触发（我还以为app-common会定时reload）
	 */
	private boolean autoReloadWithDelayCD() {
		if (checkConnect()) {
			return true;
		}
		if (System.currentTimeMillis() < this.lastReloadTime + this.reloadCD) {
			return false;
		}
		this.lastReloadTime = System.currentTimeMillis();
		return reload();
	}

	public synchronized boolean reload() {
		this.remote = ((ICRemote) RmiHelper.getRemote(this.node.name, this.node.host, this.node.port, this.csf));
		boolean suc = checkConnect();
		if (suc)
			this.service = ClientBuilder.buildClient(this.serviceClass, this.defaultService,
					new DefaultCommonRemoteClientWrapper(), this.monitor);
		else {
			disconnect();
		}
		return suc;
	}

	public void disconnect() {
		this.service = null;
		this.remote = null;
	}

	public boolean checkConnect() {
		try {
			if (this.remote == null) {
				return false;
			}
			this.remote.checkConnection();
			return true;
		} catch (Throwable t) {
			LOG.error("<CClient> check conn error!", t);
		}
		return false;
	}

	@Deprecated
	public void setMonitor(IClientMonitor monitor) {
		setMonitor(new ClientInvokeMonitorImpl(monitor));
	}

	public void setMonitor(IClientInvokeMonitor monitor) {
		this.monitor.setMonitor(monitor);
		reload();
	}

	public void enableAutoReload() {
		this.enableAutoReload = true;
	}

	public void disableAutoReload() {
		this.enableAutoReload = false;
	}

	public int setReloadCD() {
		return this.reloadCD;
	}

	public void setReloadCD(int reloadCD) {
		this.reloadCD = reloadCD;
	}

	private class DefaultCommonRemoteClientWrapper implements IClientWrapper {
		private DefaultCommonRemoteClientWrapper() {
		}

		public ICRemote getRemoteClient() {
			return CClient.this.remote;
		}
	}

	class DefaultRemoteClientMonitor implements IClientInvokeMonitor {
		public final CClient<?> client;
		private IClientInvokeMonitor monitor = CClient.EmptyMonitor;

		@SuppressWarnings("rawtypes")
		public DefaultRemoteClientMonitor(CClient client) {
			this.client = client;
		}

		void setMonitor(IClientInvokeMonitor monitor) {
			this.monitor = monitor;
		}

		public void afterHandle(ClientInvokeContext ctx, String cmd, Object[] params) {
			this.monitor.afterHandle(ctx, cmd, params);
		}

		public void beforeHandle(ClientInvokeContext ctx, String cmd, Object[] params) {
			this.monitor.beforeHandle(ctx, cmd, params);
		}

		public void handleThrowable(ClientInvokeContext ctx, String cmd, Object[] params, Throwable t) {
			if (ConnectException.class.equals(t.getClass())) {
				CClient.LOG.error(
						String.format("<App Connect failed> ip[%s] port[%s] name[%s]", new Object[] {
								this.client.node.host, Integer.valueOf(this.client.node.port), this.client.node.name }),
						t);
				this.client.disconnect();
				if (CClient.this.enableAutoReload)
					this.client.autoReloadWithDelayCD();
			} else {
				CClient.LOG.error(String.format("<App invoke failed> ip[%s] port[%s] name[%s] cmd[%s] params[%s]",
						new Object[] { this.client.node.host, Integer.valueOf(this.client.node.port),
								this.client.node.name, cmd, StringUtils.join(params, ",") }),
						t);
			}
			this.monitor.handleThrowable(ctx, cmd, params, t);
		}
	}
}