package transfer_obj;

/**
 * 房间投票信息
 */
public class GameVoteInfo implements java.io.Serializable {
	private static final long serialVersionUID = 327899983807755821L;
	private String gameName;
	private int voteNum;

	public GameVoteInfo(String gameName, int voteNum) {
		super();
		this.gameName = gameName;
		this.voteNum = voteNum;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getVoteNum() {
		return voteNum;
	}

	public void setVoteNum(int voteNum) {
		this.voteNum = voteNum;
	}
}
