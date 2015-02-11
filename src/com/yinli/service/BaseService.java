package com.yinli.service;

import java.io.Serializable;
import java.util.List;

import com.yinli.dao.BaseObject;


public interface BaseService<Pojo extends BaseObject, PK extends Serializable> {
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public Pojo get(PK id);
	
	/**
	 * 查询所有
	 * @return
	 */
	public List<Pojo> getAll();
	
	/**
	 * 保存
	 * @param pojo
	 * @return
	 */
	public PK save(Pojo pojo);
	
	/**
	 * 修改
	 * @param pojo
	 * @return
	 */
	public void update(Pojo pojo);
	
	/**
	 * 删除
	 * @param pojo
	 */
	public void delete(Pojo pojo);
	void remove(PK id);
	
	 boolean exists(PK id);
}
