package com.aoyi.app_common;

import java.rmi.RemoteException;

import org.apache.log4j.xml.DOMConfigurator;

import com.aoyi.app_common_rmi.VoteConfig;
import com.higherli.app.common.server.CServer;
import com.higherli.app.server.AppServer;

public class VoteServerMain {
	public static void main(String[] args) throws RemoteException {
		DOMConfigurator.configure("log4j.xml");
		AppServer.setLocalHostName("127.0.0.1");
		AppServer server = AppServer.createServer(VoteConfig.VOTE_PORT);
		server.bindService(VoteConfig.VOTE_REMOTE_NAME, new CServer(new VoteRemote(), new VoteServerMonitor()));
		System.out.println(">>>>>INFO: Remote object have registered(by app-common way)");
	}
}
