package com.higherli.app.common.server;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * 方法（映射）的抽象,通过映射调用方法。 methodName + paramTypes
 */
public class MethodSign implements Serializable {

	private static final long serialVersionUID = -4786157575355158045L;
	private static final int prime = 31;

	private String methodName;
	private Class<?>[] paramTypes;

	public MethodSign(Method method) {
		this.methodName = method.getName();
		this.paramTypes = method.getParameterTypes();
	}

	public MethodSign(String methodName, Class<?>[] paramTypes) {
		super();
		this.methodName = methodName;
		this.paramTypes = paramTypes;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = prime * result + (this.methodName == null ? 0 : this.methodName.hashCode());
		result = prime * result + Arrays.hashCode(this.paramTypes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MethodSign))
			return false;
		MethodSign other = (MethodSign) obj;
		if (this.methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!this.methodName.equals(other.methodName)) {
			return false;
		}
		return Arrays.equals(this.paramTypes, other.paramTypes);
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class<?>[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	@Override
	public String toString() {
		return String.format("method[%s(%s)]", new Object[] { methodName, StringUtils.join(paramTypes, ", ") });
	}

}
