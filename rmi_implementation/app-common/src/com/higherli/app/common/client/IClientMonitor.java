package com.higherli.app.common.client;

public interface IClientMonitor {
	 public void handleThrowable(String paramString, Object[] paramArrayOfObject, Throwable paramThrowable);

	  public void beforeHandle(String paramString, Object[] paramArrayOfObject);
}
