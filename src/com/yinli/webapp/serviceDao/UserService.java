package com.yinli.webapp.serviceDao;

import java.util.List;

import com.yinli.pojo.User;
import com.yinli.service.BaseService;

public interface UserService extends BaseService<User, Integer> {

	List<User> getUsers();

    void saveUser(User user);

    boolean deleteUser(Integer id);
    
    User getUser(Integer id);

}
