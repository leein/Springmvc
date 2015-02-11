package com.yinli.service.impl;


import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yinli.dao.BaseDao;
import com.yinli.dao.BaseObject;
import com.yinli.service.BaseService;


public class BaseServiceImpl<Pojo extends BaseObject, PK extends Serializable> implements BaseService<Pojo, PK> {
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
	protected BaseDao<Pojo,PK> baseDao;

	public Pojo get(PK id) {
		return baseDao.get(id);
	}

	public List<Pojo> getAll() {
		return baseDao.getAll();
	}

	public PK save(Pojo pojo) {
		return baseDao.save(pojo);
	}

	public void update(Pojo pojo) {
		baseDao.update(pojo);
	}

	public void delete(Pojo pojo) {
		baseDao.delete(pojo);
	}

	public BaseDao<Pojo, PK> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao<Pojo, PK> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void remove(PK id) {
            baseDao.delete(id);		
	}

	@Override
	public boolean exists(PK id) {
		// TODO Auto-generated method stub
		return baseDao.exists(id);
	}

}
