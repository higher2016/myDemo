package com.aoyi.app_common;

import com.aoyi.app_common_rmi.VoteConfig;
import com.higherli.app.server.AppAdmin;

public class VoteServerShutDown {
	public static void main(String[] args) {
		AppAdmin.shutdownServer(VoteConfig.VOTE_PORT);
		System.out.println(String.format("*********end shut down %s****************", VoteConfig.VOTE_REMOTE_NAME));
		System.exit(0);
	}
}
