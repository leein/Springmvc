package com.yinli.webapp.entityDao;

import java.util.List;

import com.yinli.dao.BaseDao;
import com.yinli.pojo.User;

public interface UserDao extends BaseDao<User, Integer> {
	
	    List<User> getUsers();
	    
	    User getUser(Integer id);

	    void saveUser(User user);

	    boolean deleteUser(Integer id);

}
