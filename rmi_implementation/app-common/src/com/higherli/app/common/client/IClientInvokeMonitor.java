package com.higherli.app.common.client;

/**
 * <strong>�ͻ���</strong>���ü�أ�����ǰ�󼰱����ӿ�
 */
public interface IClientInvokeMonitor {

	void afterHandle(ClientInvokeContext ctx, String cmd, Object[] params);

	void beforeHandle(ClientInvokeContext ctx, String cmd, Object[] params);

	void handleThrowable(ClientInvokeContext ctx, String cmd, Object[] params, Throwable t);

}
