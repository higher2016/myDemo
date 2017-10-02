package com.aoyi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import transfer_obj.GameVoteInfo;
import transfer_obj.PersonVoteInfo;
import util.ResultBean;
import util.ResultCode;

@SuppressWarnings("unchecked")
public class HelloService {
	public static final HelloService INSTANCE = new HelloService();

	private Map<String, PollingRoom> candidateGame = new HashMap<String, PollingRoom>();

	private HelloService() {
		initCandidate();
	}

	private void initCandidate() {
		for (String gameName : getAllGameName()) {
			candidateGame.put(gameName, new PollingRoom(gameName));
		}
	}

	private List<String> getAllGameName() {
		return Arrays.asList("aoyi", "aoya", "aoqi", "aoch");
	}

	private boolean isGameExist(String gameName) {
		return candidateGame.get(gameName) != null;
	}

	public void onUnbind() {
		try {
			File file = new File("voteResult.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream o = new FileOutputStream(file);
			o.write(getThisTimeVoteResult().getBytes("UTF-8"));
			o.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("vote info save success!!!----------");
	}

	private String getThisTimeVoteResult() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder voteResult = new StringBuilder();
		voteResult.append("Statistics Time: " + sdf.format(new Date()) + System.lineSeparator());
		for (Map.Entry<String, PollingRoom> entry : candidateGame.entrySet()) {
			voteResult.append(entry.getValue().getVoteResult());
		}
		voteResult.append("=======================" + System.lineSeparator());
		return voteResult.toString();
	}

	
	
	//-------------------------业务方法---------------------------
	
	public ResultBean<Integer> vote(int voterId, String gameName) {
		ResultBean<Integer> result = ResultBean.createDefaultResult();
		if (!isGameExist(gameName)) {
			result.setResultId(ResultCode.GAME_NO_EXIST);
			return result;
		}
		result.setResultObj(candidateGame.get(gameName).vote(voterId));
		return result;
	}

	public ResultBean<PersonVoteInfo> getMyVoteInfo(int voterId) {
		ResultBean<PersonVoteInfo> result = ResultBean.createDefaultResult();
		for (Map.Entry<String, PollingRoom> entry : candidateGame.entrySet()) {
			Date date = entry.getValue().getVoteDate(voterId);
			if (date != null) {
				result.setResultObj(new PersonVoteInfo(entry.getKey(), date));
				return result;
			}
		}
		return result;
	}

	public ResultBean<List<GameVoteInfo>> getVoteInfo() {
		ResultBean<List<GameVoteInfo>> result = ResultBean.createDefaultResult();
		List<GameVoteInfo> gameVoteInfoList = new ArrayList<GameVoteInfo>();
		for (Map.Entry<String, PollingRoom> entry : candidateGame.entrySet()) {
			GameVoteInfo info = new GameVoteInfo(entry.getKey(), entry.getValue().getVoteNum());
			gameVoteInfoList.add(info);
		}
		return result;
	}
}
