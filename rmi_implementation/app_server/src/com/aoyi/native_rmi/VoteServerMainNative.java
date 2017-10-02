package com.aoyi.native_rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class VoteServerMainNative {
	public static IVoteNative VOTE_NATIVE;

	public static void main(String args[]) throws NotBoundException {
		try {
			LocateRegistry.createRegistry(12000);
			IVoteNative vote = new VoteRemoteNative();
			VOTE_NATIVE = vote;
			Naming.bind("//localhost:12000/NativeVote", vote);

			System.out.println(">>>>>INFO: Remote object have registered(by native rmi way)");
		} catch (RemoteException e) {
			System.out.println("Remote wrong");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.out.println("Object hava registered");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}