package com.aoyi.app_common_rmi;

import java.util.List;

import transfer_obj.GameVoteInfo;
import transfer_obj.PersonVoteInfo;
import util.ResultBean;

import com.higherli.app.common.server.Service;

@Service
public interface IVote {
	public ResultBean<Integer> vote(int voterId, String gameName);

	public ResultBean<PersonVoteInfo> getMyVoteInfo(int voterId);

	public ResultBean<List<GameVoteInfo>> getVoteInfo();
}
