package com.aoyi.app_common_rmi;

import com.higherli.app.common.client.ClientInvokeContext;
import com.higherli.app.common.client.IClientInvokeMonitor;

/**
 * 客户端远程对象调用监听器
 */
public class VoteClientMonitor implements IClientInvokeMonitor {
	@Override
	public void afterHandle(ClientInvokeContext ctx, String cmd, Object[] params) {
	}

	@Override
	public void beforeHandle(ClientInvokeContext ctx, String cmd, Object[] params) {
	}

	@Override
	public void handleThrowable(ClientInvokeContext ctx, String cmd, Object[] params, Throwable t) {
	}
}
