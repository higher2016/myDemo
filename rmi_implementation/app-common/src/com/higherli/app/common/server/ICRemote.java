package com.higherli.app.common.server;

import java.rmi.RemoteException;

import com.higherli.app.dto.AppRemote;

/**
 * Client与Server的之间的网络远程调用由CServer与CClient通过ICRemote接口提供. 主要提供了服务的远程反射调用和连接检查.
 */
public interface ICRemote extends AppRemote {
	/**
	 * 反射调用
	 * @throws RemoteException 
	 */
	public <T> T exe(MethodSign paramMethodSign, Object[] paramArrayOfObject) throws RemoteException;

	public void checkConnection() throws RemoteException;
}
