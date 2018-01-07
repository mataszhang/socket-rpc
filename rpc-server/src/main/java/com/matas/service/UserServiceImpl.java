package com.matas.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.matas.entity.User;

public class UserServiceImpl implements UserService {
	private static Map<Integer, User> data = null;
	static {
		data = new LinkedHashMap<>();
		data.put(1, new User("jack", 12));
		data.put(2, new User("alex", 13));
		data.put(3, new User("lisi", 14));
	}

	@Override
	public List<User> list() {
		return new ArrayList<>(data.values());
	}

	@Override
	public User get(Integer id) {
		return data.get(id);
	}
}
