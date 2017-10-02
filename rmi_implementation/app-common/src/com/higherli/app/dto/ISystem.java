package com.higherli.app.dto;

import java.rmi.RemoteException;

/**
 * <strong>系统远程对象</strong>规范
 */
public interface ISystem extends AppRemote {
	public String stopserver() throws RemoteException;

	public String[] checkService() throws RemoteException;
}
