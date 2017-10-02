package transfer_obj;

import java.util.Date;

public class PersonVoteInfo implements java.io.Serializable{

	private static final long serialVersionUID = -837624589791221846L;
	private String voteGameName;
	private Date voteTime;
	public PersonVoteInfo(String voteGameName, Date voteTime) {
		super();
		this.voteGameName = voteGameName;
		this.voteTime = voteTime;
	}
	public String getVoteGameName() {
		return voteGameName;
	}
	public void setVoteGameName(String voteGameName) {
		this.voteGameName = voteGameName;
	}
	public Date getVoteTime() {
		return voteTime;
	}
	public void setVoteTime(Date voteTime) {
		this.voteTime = voteTime;
	}
}
