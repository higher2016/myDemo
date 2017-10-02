package com.higherli.app.server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import com.higherli.app.dto.AppRemote;

/**
 * �������Զ�̶���ĳ����װ�࣬Ҫʵ��Զ�̶���ֱ�Ӽ̳�����������. �����Լ�extends UnicastRemoteObject ��
 * implements Remote(��Ϊһ��Զ�̶����Ǳ���Ҫextends UnicastRemoteObject�� implements
 * Remote��). ͬʱ���淶��AppRemoteԶ�̶������Ҫʵ�ֵ���������: onBind(),onUnbind()
 */
public abstract class AppRemoteObject extends UnicastRemoteObject implements IService, AppRemote {
	private static final long serialVersionUID = 1L;

	protected AppRemoteObject() throws RemoteException {
		this(null, null);
	}

	protected AppRemoteObject(RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
		super(0, csf, ssf);
	}

	@Override
	public void onBind() {
	}

	@Override
	public void onUnbind() {
	}

}
