package com.aoyi.native_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import transfer_obj.GameVoteInfo;
import transfer_obj.PersonVoteInfo;
import util.ResultBean;

public interface IVoteNative extends Remote {
	public void onUnbind() throws RemoteException;

	public ResultBean<Integer> vote(int voterId, String gameName) throws RemoteException;

	public ResultBean<PersonVoteInfo> getMyVoteInfo(int voterId) throws RemoteException;

	public ResultBean<List<GameVoteInfo>> getVoteInfo() throws RemoteException;
}