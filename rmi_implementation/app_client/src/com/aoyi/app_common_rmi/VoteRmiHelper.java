package com.aoyi.app_common_rmi;

import com.aoyi.app_common_rmi.rmi.RemoteServiceProvider;
import com.higherli.app.RemoteNode;

public class VoteRmiHelper {
	private RemoteServiceProvider<IVote> provider;

	public static final VoteRmiHelper INSTANCE = new VoteRmiHelper();

	private VoteRmiHelper() {
		RemoteNode remoteNode = new RemoteNode("127.0.0.1", VoteConfig.VOTE_PORT, VoteConfig.VOTE_REMOTE_NAME);
		IVote defHello = new DefaultVote();
		RemoteServiceProvider<IVote> provider = new RemoteServiceProvider<IVote>(remoteNode, defHello);
		provider.setMonitor(new VoteClientMonitor());
		this.provider = provider;
	}

	public IVote getService() {
		return provider.getService();
	}
}