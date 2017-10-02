package com.aoyi;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import util.ResultCode;

public class PollingRoom {

	private AtomicInteger candidateGame = new AtomicInteger(0);
	private Map<Integer, Date> allVoterIdMap = new ConcurrentHashMap<Integer, Date>();
	private String gameName;

	public PollingRoom(String gameName) {
		super();
		this.gameName = gameName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int vote(int voterId) {
		if (allVoterIdMap.containsKey(voterId)) {
			return ResultCode.HAVE_VOTE;
		}
		return candidateGame.incrementAndGet();
	}

	public Date getVoteDate(int voterId) {
		return allVoterIdMap.get(voterId);
	}

	public int getVoteNum() {
		return candidateGame.get();
	}

	public String getVoteResult() {
		return String.format("%s vote number: %d." + System.lineSeparator(), gameName, getVoteNum());
	}
}
