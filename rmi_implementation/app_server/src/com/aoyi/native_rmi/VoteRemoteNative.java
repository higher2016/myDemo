package com.aoyi.native_rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.aoyi.HelloService;

import transfer_obj.GameVoteInfo;
import transfer_obj.PersonVoteInfo;
import util.ResultBean;

public class VoteRemoteNative extends UnicastRemoteObject implements IVoteNative {
	private static final long serialVersionUID = 2085486720158521542L;

	public VoteRemoteNative() throws RemoteException {
	}

	@Override
	public void onUnbind() throws RemoteException {
		HelloService.INSTANCE.onUnbind();
		System.exit(0);
	}

	@Override
	public ResultBean<Integer> vote(int voterId, String gameName) throws RemoteException {
		return HelloService.INSTANCE.vote(voterId, gameName);
	}

	@Override
	public ResultBean<PersonVoteInfo> getMyVoteInfo(int voterId) throws RemoteException {
		return HelloService.INSTANCE.getMyVoteInfo(voterId);
	}

	@Override
	public ResultBean<List<GameVoteInfo>> getVoteInfo() throws RemoteException {
		return HelloService.INSTANCE.getVoteInfo();
	}
}
