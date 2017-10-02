package com.higherli.app.common.server;

public interface IServerMonitor {
	void beforeExe(String paramString, Object[] paramArrayOfObject);

	void afterExe(String paramString, Object[] paramArrayOfObject);

	void onThrowable(String paramString, Object[] paramArrayOfObject, Throwable paramThrowable);
}
