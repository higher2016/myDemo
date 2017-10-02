package com.aoyi.native_rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.aoyi.app_common_rmi.VoteConfig;

public class VoteServerShutDownNative {
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		IVoteNative rmoteVote = (IVoteNative) Naming.lookup("rmi://localhost:12000/NativeVote");
		rmoteVote.onUnbind();
		System.out.println(String.format("*********end shut down %s****************", VoteConfig.VOTE_REMOTE_NAME));
		System.exit(0);
	}
}
