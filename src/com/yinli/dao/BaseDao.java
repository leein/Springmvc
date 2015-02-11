package com.yinli.dao;


import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface BaseDao<Pojo extends BaseObject, PK extends Serializable> {
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
	
	 List<Pojo> getAllDistinct();
	 
	 
	 boolean exists(PK id);
	
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
	
	/**
	 * 根据ID进行删除
	 * @param id
	 */
	public void delete(PK id);
	
	/**
	 * 原始sql语句
	 * @param sql
	 * @return
	 */
	public String getBackStr(final String sql);
	
	/**
	 * 查询分页功能入口
	 * 
	 * @param baseQuery 分页参数
	 * @param entity
	 *            传入的检索实体
	 * @param p
	 *            检索的条件关系
	 * 
	 * @author yesa
	 * @return List<Pojo> 返回的实体的List
	 
	public List<Pojo> getListAndPageSize(BaseQuery baseQuery,
			Object entity, HashMap h);
*/
	
	/**
	 * 查询分页功能入口
	 * 
	 * @param baseQuery 分页参数、查询条件，必须继承BaseQuery
	 * 
	 * @param p 检索的条件关系
	 * 
	 * @author yesa
	 * @return List<Pojo> 返回的实体的List
	 */
	public List<Pojo> getListAndPageSize(BaseQuery baseQuery, Map<String,String> h);
	
	/**
	 * 根据HQL语句 分页查询，并把查询的总条数设置到BaseQuery中
	 * <pre>
	 * @Title: getListAndSetTotalItem 
	 * @Description: 根据HQL语句 分页查询，并把查询的总条数设置到BaseQuery中
	 *
	 * @param hql 查询语句
	 * @param baseQuery 分页参数,为null时忽略分页，查询所有
	 * @param values 查询条件，为null时表示没有条件
	 * 
	 * @return List<T>    返回类型 
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public List<Pojo> getListAndSetTotalItem(final String hql, 
			final BaseQuery baseQuery, final Map<String, Object> values);

	/**
	 * 根据Hibernate提供的DetachedCriteria类 分页查询，并把查询的总条数设置到BaseQuery中
	 * <pre>
	 * @Title: getListAndSetTotalItem 
	 * @Description: 分页查询，并把查询的总条数设置到BaseQuery中
	 *
	 * @param baseQuery 分页信息,为null时，忽略分页，查询所有
	 * @param dc 查询条件
	 * 
	 * @return List<T>    返回类型 
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 *
	public List<Pojo> getListAndSetTotalItem(final BaseQuery baseQuery, final DetachedCriteria dc);
*/
	
	/**
	 * 根据sql语句查询
	 * @param sql
	 * @return
	 */
	public List<Map<String,Object>> getListBySql(String sql);
}
