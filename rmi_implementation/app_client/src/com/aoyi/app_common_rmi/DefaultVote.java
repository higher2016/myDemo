package com.aoyi.app_common_rmi;

import java.util.List;

import transfer_obj.GameVoteInfo;
import transfer_obj.PersonVoteInfo;
import util.ResultBean;

@SuppressWarnings({"rawtypes","unchecked"})
public class DefaultVote implements IVote {
	private static final ResultBean SYSTEM_ERROR = new ResultBean<>(-404);

	@Override
	public ResultBean<Integer> vote(int voterId, String gameName) {
		return SYSTEM_ERROR;
	}

	@Override
	public ResultBean<PersonVoteInfo> getMyVoteInfo(int voterId) {
		return SYSTEM_ERROR;
	}

	@Override
	public ResultBean<List<GameVoteInfo>> getVoteInfo() {
		return SYSTEM_ERROR;
	}

}
