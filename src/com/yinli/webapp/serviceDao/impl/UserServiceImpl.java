package com.yinli.webapp.serviceDao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yinli.pojo.User;
import com.yinli.service.impl.BaseServiceImpl;
import com.yinli.webapp.entityDao.UserDao;
import com.yinli.webapp.serviceDao.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements
		UserService {

	private UserDao userDao;

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<User> getUsers() {
		
		return userDao.getUsers();
	}

	@Override
	public void saveUser(User user) {
		userDao.saveUser(user);

	}

	@Override
	public boolean deleteUser(Integer id) {
		return userDao.deleteUser(id);
	}

	@Override
	public User getUser(Integer id) {
		
		return userDao.getUser(id);
	}

}
