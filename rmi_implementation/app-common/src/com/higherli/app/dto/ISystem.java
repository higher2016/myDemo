package com.higherli.app.dto;

import java.rmi.RemoteException;

/**
 * <strong>ϵͳԶ�̶���</strong>�淶
 */
public interface ISystem extends AppRemote {
	public String stopserver() throws RemoteException;

	public String[] checkService() throws RemoteException;
}
