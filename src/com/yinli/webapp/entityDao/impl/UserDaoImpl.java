package com.yinli.webapp.entityDao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.yinli.dao.impl.BaseDaoImpl;
import com.yinli.pojo.User;
import com.yinli.webapp.entityDao.UserDao;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

	@Override
	public List<User> getUsers() {
		final String sql = "select * from user";
		return getJdbcTemplate().query(sql,  new UserRowMapper());
	}

	@Override
	public void saveUser(User user) {
		int a=0;
		String sqluser = "select count(*) from user where id="+user.getId();
		try{
		 a = getJdbcTemplate().queryForInt(sqluser);
		}catch(Exception e){
			e.printStackTrace();
			a=-1;
		}
		if (a > 0) {
			final String sql = "update user set name=?,age=? where id=?";
			getJdbcTemplate().update(sql, user.getName(), user.getAge(),
					user.getId());
		} else {
			final String sql = "insert into user values(?,?,?)";
			getJdbcTemplate().update(sql, user.getId(), user.getName(),
					user.getAge());
		}

	}

	@Override
	public boolean deleteUser(Integer id) {
		final String sql = "delete from user where id=?";
		int b = getJdbcTemplate().update(sql, id);
		return b > 0;
	}

	@Override
	public User getUser(Integer id) {
		final String sql = "select * from user where id="+id;

		return getJdbcTemplate().queryForObject(sql,  new UserRowMapper());
		
	}

	protected class UserRowMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setAge(rs.getInt("age"));
			return user;
		}

	}

}
