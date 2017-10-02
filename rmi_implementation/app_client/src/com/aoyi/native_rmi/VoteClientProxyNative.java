package com.aoyi.native_rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class VoteClientProxyNative {
	public static final VoteClientProxyNative INSTANCE = new VoteClientProxyNative();

	private VoteClientProxyNative() {
		init();
	}

	private IVoteNative rmoteVote;
	
	public IVoteNative getRmoteObj() {
		return rmoteVote;
	}

	public void setRmoteObjello(IVoteNative rmoteObjello) {
		this.rmoteVote = rmoteObjello;
	}

	private void init() {
		try {
			rmoteVote = (IVoteNative) Naming.lookup("rmi://localhost:12000/NativeVote");
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
