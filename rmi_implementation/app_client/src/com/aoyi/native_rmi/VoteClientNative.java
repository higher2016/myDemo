package com.aoyi.native_rmi;

import java.rmi.RemoteException;

public class VoteClientNative {
	public static void main(String args[]) throws RemoteException {
		IVoteNative remote = VoteClientProxyNative.INSTANCE.getRmoteObj();
		remote.getMyVoteInfo(1);
		remote.getVoteInfo();
		remote.vote(1, "aoyi");
	}
}
