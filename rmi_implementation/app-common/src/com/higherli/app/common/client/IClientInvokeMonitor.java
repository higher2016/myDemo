package com.higherli.app.common.client;

/**
 * <strong>客户端</strong>调用监控（调用前后及报错）接口
 */
public interface IClientInvokeMonitor {

	void afterHandle(ClientInvokeContext ctx, String cmd, Object[] params);

	void beforeHandle(ClientInvokeContext ctx, String cmd, Object[] params);

	void handleThrowable(ClientInvokeContext ctx, String cmd, Object[] params, Throwable t);

}
