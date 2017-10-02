package com.higherli.app.common.server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

import com.higherli.app.server.AppRemoteObject;
import com.higherli.app.server.IService;

public class CServer extends AppRemoteObject implements ICRemote {

	private static final long serialVersionUID = -3883152276651959862L;
	private IServerMonitor monitor;
	private final CServerBean bean;

	public CServer(IService service, IServerMonitor monitor) throws RemoteException {
		this(service, monitor, null, null);
	}

	public CServer(IService service, IServerMonitor monitor, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
			throws RemoteException {
		super(csf, ssf);
		this.bean = new CServerBean(service);
		this.monitor = monitor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T exe(MethodSign methodSign, Object[] args) throws RemoteException {
		try {
			this.monitor.beforeExe(methodSign.getMethodName(), args);
			Object ret = this.bean.exe(methodSign, args);
			this.monitor.afterExe(methodSign.getMethodName(), args);
			return (T) ret;
		} catch (Throwable t) {
			this.monitor.onThrowable(methodSign.getMethodName(), args, t);
			throw new RemoteException(t.getMessage());
		}
	}

	@Override
	public void checkConnection() throws RemoteException {
	}

	public void onBind() {
		this.bean.getService().onBind();
	}

	public void onUnbind() {
		this.bean.getService().onUnbind();
	}

}
