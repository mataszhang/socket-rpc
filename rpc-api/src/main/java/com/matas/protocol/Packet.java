package com.matas.protocol;

import java.io.Serializable;

/**
 * 远程调用协议。 <br/>
 * 客户的通过序列化该对象来发送协议给服务端。<br/>
 * 服务端反序列化该对象实例后，通过反射来调用服务端实现类的对应方法。
 * 
 * @author matas
 *
 */
public class Packet implements Serializable {

	private static final long serialVersionUID = 8420851946575330635L;
	private String clazzName;
	private String methodName;
	private Class<?>[] paramTypes;
	private Object[] args;

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class<?>[] paramTypes) {
		this.paramTypes = paramTypes;
	}
}
