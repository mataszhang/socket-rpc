package com.matas.service;

import java.util.List;

import com.matas.entity.User;

public interface UserService {
	List<User> list();

	User get(Integer id);
}
