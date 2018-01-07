package com.matas.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.List;

import com.matas.entity.User;
import com.matas.protocol.Packet;
import com.matas.service.UserService;

public class App {

	public static void main(String[] args) {
		UserService service = createProxy(UserService.class);
		User user = service.get(1);
		System.out.println(user);
		
		System.out.println("============");
		
		List<User> list = service.list();
		if (null != list) {
			for (User u : list) {
				System.out.println(u);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<? extends T> clazz) {
		return (T) Proxy.newProxyInstance(App.class.getClassLoader(), new Class[] { clazz }, new ProxyInvoker());
	}

	/**
	 * 发起RPC调用
	 * @author matas
	 *
	 */
	static class ProxyInvoker implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Socket socket = null;
			Object result = null;

			try {
				socket = new Socket("localhost", 9527);

				String clazzName = method.getDeclaringClass().getName();
				String methodName = method.getName();
				Class<?>[] parameterTypes = method.getParameterTypes();
				/**
				 * 创建并发送协议包
				 */
				Packet packet = new Packet();
				packet.setClazzName(clazzName);
				packet.setMethodName(methodName);
				packet.setParamTypes(parameterTypes);
				packet.setArgs(args);

				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(packet);
				oos.flush();

				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				result = ois.readObject();

				oos.close();
				ois.close();
			} finally {
				if (null != socket && !socket.isClosed()) {
					socket.close();
				}
			}
			return result;
		}
	}

}
