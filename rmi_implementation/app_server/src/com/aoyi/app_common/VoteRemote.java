package com.aoyi.app_common;

import java.util.List;

import transfer_obj.GameVoteInfo;
import transfer_obj.PersonVoteInfo;
import util.ResultBean;

import com.aoyi.HelloService;
import com.aoyi.app_common_rmi.IVote;
import com.higherli.app.server.IService;

public class VoteRemote implements IService, IVote {
	@Override
	public void onBind() {
	}

	@Override
	public void onUnbind() {
		HelloService.INSTANCE.onUnbind();
	}

	@Override
	public ResultBean<Integer> vote(int voterId, String gameName) {
		return HelloService.INSTANCE.vote(voterId, gameName);
	}

	@Override
	public ResultBean<PersonVoteInfo> getMyVoteInfo(int voterId) {
		return HelloService.INSTANCE.getMyVoteInfo(voterId);
	}

	@Override
	public ResultBean<List<GameVoteInfo>> getVoteInfo() {
		return HelloService.INSTANCE.getVoteInfo();
	}

}
