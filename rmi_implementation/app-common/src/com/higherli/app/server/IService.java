package com.higherli.app.server;

/**
 * 规范远程对象所要实现的方法
 */
public interface IService {
	/**
	 * 绑定远程对象前要进行的操作
	 */
	public void onBind();

	/**
	 * 解绑定远程对象后要进行的操作
	 */
	public void onUnbind();
}
