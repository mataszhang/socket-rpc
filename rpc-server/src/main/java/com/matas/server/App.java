package com.matas.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import com.matas.protocol.Packet;

public class App {
	public static void main(String[] args) throws Exception {
		int port = 9527;
		ServerSocket server = new ServerSocket(port);
		System.err.println("服务已启动，端口:" +port);
		while (true) {
			Socket socket = null;
			try {
				socket = server.accept();

				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

				Object readObject = ois.readObject();
				if (null != readObject) {
					if (readObject instanceof Packet) {
						Packet packet = (Packet) readObject;
						String clazzName = packet.getClazzName() + "Impl";
						String methodName = packet.getMethodName();
						Object[] params = packet.getArgs();
						Class<?>[] paramTypes = packet.getParamTypes();

						System.out.println("接收到RPC请求，将调用 " + clazzName + "." + methodName + "()");
						Class<?> clazz = Class.forName(clazzName);
						Method method = clazz.getMethod(methodName, paramTypes);
						Object result = method.invoke(clazz.newInstance(), params);

						oos.writeObject(result);
					} else {
						oos.writeObject(null);
					}
				} else {
					oos.writeObject(null);
				}
				oos.flush();
				oos.close();
				ois.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				closeSocket(socket);
			}
		}
	}

	private static void closeSocket(Socket socket) {
		if (null != socket && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
