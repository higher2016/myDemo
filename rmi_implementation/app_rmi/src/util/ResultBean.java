package util;

import java.io.Serializable;

public class ResultBean<T> implements Serializable {

	private static final long serialVersionUID = 1392589791480417596L;

	private T resultObj;
	private int resultId;

	@SuppressWarnings("rawtypes")
	public static ResultBean createDefaultResult() {
		return new ResultBean<>(ResultCode.SUCCESS);
	}

	public ResultBean(int resultId) {
		super();
		this.resultId = resultId;
	}

	public boolean isSuccess() {
		return resultId == ResultCode.SUCCESS;
	}

	public T getResultObj() {
		return resultObj;
	}

	public void setResultObj(T resultObj) {
		this.resultObj = resultObj;
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

}
