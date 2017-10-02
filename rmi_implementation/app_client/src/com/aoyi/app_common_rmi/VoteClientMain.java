package com.aoyi.app_common_rmi;

import org.apache.log4j.xml.DOMConfigurator;

import transfer_obj.PersonVoteInfo;
import util.ResultBean;

public class VoteClientMain {
	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");
		VoteRmiHelper.INSTANCE.getService().vote(2, "aoyi");
		ResultBean<PersonVoteInfo> result = VoteRmiHelper.INSTANCE.getService().getMyVoteInfo(1);
		if(result.isSuccess()){
			System.out.println(result.getResultObj());
		}else{
			System.out.println(result.getResultId());
		}
	}
}
