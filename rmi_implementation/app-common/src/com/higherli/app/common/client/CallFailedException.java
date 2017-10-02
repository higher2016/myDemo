package com.higherli.app.common.client;

import java.lang.reflect.Method;

public class CallFailedException extends Exception {
	private static final long serialVersionUID = -6324104849231161866L;
	public final Method m;

	public CallFailedException(Method m) {
		super(buildMsg(m));
		this.m = m;
	}

	private static String buildMsg(Method m) {
		return "method[" + m.toGenericString() + "]";
	}
}
