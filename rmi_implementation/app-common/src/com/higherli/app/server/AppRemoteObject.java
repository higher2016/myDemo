package com.higherli.app.server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import com.higherli.app.dto.AppRemote;

/**
 * 这个就是远程对象的抽象封装类，要实现远程对象直接继承这个对象就行. 不用自己extends UnicastRemoteObject 和
 * implements Remote(作为一个远程对象是必须要extends UnicastRemoteObject和 implements
 * Remote的). 同时还规范了AppRemote远程对象必须要实现的两个方法: onBind(),onUnbind()
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
