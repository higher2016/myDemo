package com.higherli.app.common.client;

public class ClientInvokeMonitorImpl implements IClientInvokeMonitor {

	final IClientMonitor monitor;

	public ClientInvokeMonitorImpl(IClientMonitor monitor) {
		this.monitor = monitor;
	}

	public void afterHandle(ClientInvokeContext ctx, String cmd, Object[] params) {

	}

	@Override
	public void beforeHandle(ClientInvokeContext ctx, String cmd, Object[] params) {
		
	}

	@Override
	public void handleThrowable(ClientInvokeContext ctx, String cmd, Object[] params, Throwable t) {
		
	}

}
