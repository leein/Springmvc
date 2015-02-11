package com.yinli.dao.impl;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.yinli.dao.BaseDao;
import com.yinli.dao.BaseObject;
import com.yinli.dao.BaseQuery;
import com.yinli.dao.CriteriaModel;


public class BaseDaoImpl<Pojo extends BaseObject, PK extends Serializable>
		implements BaseDao<Pojo, PK> {
	private Class<Pojo> persistentClass;
	@Resource
	private SessionFactory sessionFactory;
	private JdbcTemplate jdbcTemplate;

	public BaseDaoImpl(Class<Pojo> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public BaseDaoImpl(Class<Pojo> persistentClass,
			SessionFactory sessionFactory) {
		this.persistentClass = persistentClass;
		this.sessionFactory = sessionFactory;
	}


	public Session getSession() throws HibernateException {
		Session sess = null;
		try {
			sess = getSessionFactory().getCurrentSession();
		} catch (HibernateException e) {
			 e.printStackTrace();
		}

		if (sess == null) {
			sess = getSessionFactory().openSession();
		}
		return sess;
	}

	public static void closeSession(Session session){
		if(session!=null){
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Pojo> getAll() {
		return getSession().createCriteria(persistentClass).list();

	}
	@SuppressWarnings("unchecked")
	public Pojo get(PK id) {
		return (Pojo) this.getSession().get(persistentClass, id);
	}

	@SuppressWarnings("unchecked")
	public PK save(Pojo pojo) {
		return (PK) getSession().save(pojo);
	}

	public void update(Pojo pojo) {
		getSession().update(pojo);
	}

	public void delete(Pojo pojo) {
		getSession().delete(pojo);
	}

	public void delete(PK id) {
		getSession().delete(get(id));
	}

	public String getBackStr(final String sql) {
		// TODO Auto-generated method stub
		final StringBuffer backStr = new StringBuffer("");
		this.getSession().doWork(new Work() {
			ResultSet rs = null;
			Statement stmt = null;

			public void execute(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				stmt = connection.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				// log.info(DateTimeUtils.getNowStrTime() + "--" + sql);
				rs = stmt.executeQuery(sql);
				while (rs != null && rs.next()) {
					if (rs.getString("str") != null)
						backStr.append(rs.getString("str"));
				}
			}
		});
		return backStr.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Pojo> getListAndPageSize(BaseQuery baseQuery,
			Map<String, String> h) {
		List<Pojo> list = null;
		Session session = null;
		Criteria c = null;
		Criteria cCount = null;
		try {
			session = this.getSession();
			c = session.createCriteria(this.persistentClass);
			CriteriaModel cm = CriteriaModel.instanceByObj(baseQuery);
			cm.setRelationMap(h);
			c = cm.getCriteria(c);
			cCount = cm.getCriteria(cCount);
			if (baseQuery != null) {
				// 获取总页数
				cCount = session.createCriteria(this.persistentClass);
				cCount = cm.getCriteria(cCount);
				Object o = cCount.setProjection(Projections.rowCount())
						.uniqueResult();
				baseQuery.setTotalItem(Integer.valueOf(o.toString()));

				// 设置分页
				c.setFirstResult(baseQuery.getStartRow().intValue());
				c.setMaxResults(baseQuery.getPageSize().intValue());
			}
			list = c.list();
		} catch (Exception e) {
			// SystemLogUtil.saveError(Constants.LOG_ERROR, e.getMessage());
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * 查询分页功能入口
	 * 
	 * @param baseQuery
	 *            分页参数
	 * @param entity
	 *            传入的检索实体
	 * @param p
	 *            检索的条件关系
	 * 
	 * @author yesa
	 * @return HashMap 在HashMap里有页数和检索返回的实体的List
	 */

	public List<Pojo> getListAndSetTotalItem(final String hql,
			final BaseQuery baseQuery, final Map<String, Object> values) {

		List<Pojo> list = null;

		Query query = this.getSession().createQuery(hql);
		if (baseQuery != null) {
			query.setFirstResult(baseQuery.getStartRow().intValue());
			query.setMaxResults(baseQuery.getPageSize().intValue());
		}

		setQueryParameter(query, values);// 查询参数设置
		list = query.list();

		if (list == null || list.size() == 0) {
			list = null;
			if (baseQuery != null) {
				baseQuery.setTotalItem(0);
			}
		} else if (baseQuery != null) {
			String allCountHql = hql.replaceAll(".*[F|f][R|r][O|o][M|m]",
					"select count(*) from");

			baseQuery.setTotalItem(this.getAllCount(allCountHql, values)
					.intValue());
		}

		return list;
	}

	/**
	 * 设置查询参数
	 * 
	 * @param query
	 * @param values
	 */
	private void setQueryParameter(Query query, Map<String, Object> values) {
		if (values != null) {
			Set<String> keys = values.keySet();
			if (keys != null && keys.size() > 0) {
				for (String key : keys) {
					query.setParameter(key, values.get(key));
				}
			}
		}
		// return query;
	}

	/**
	 * 获取符合查询条件记录总数
	 * 
	 * @param hql
	 * @param values
	 * @return
	 */
	private Long getAllCount(final String hql, final Map<String, Object> values) {
		Query query = this.getSession().createQuery(hql);
		setQueryParameter(query, values);
		return (Long) query.uniqueResult();
	}

	@Override
	public List<Map<String, Object>> getListBySql(String sql) {
		return jdbcTemplate.queryForList(sql);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Pojo> getAllDistinct() {
		Collection<Pojo> result = new LinkedHashSet<Pojo>(getAll());
		return new ArrayList<Pojo>(result);
	}

	@Override
	public boolean exists(PK id) {
		Pojo entity =this.get(id);
		return entity != null;
	}

}
