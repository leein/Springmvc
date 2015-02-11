package com.yinli.dao;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


/**
 * 实现hibernate Criteria查询容器的条件注入
 * 
 * @author yesa
 */
public class CriteriaModel {

	private Object object;

	private Map<String,String> relationMap;

	/**
	 * 设置每个元素的查询方式
	 * 
	 */
	public Map<String,String> getRelationMap() {
		return relationMap;
	}

	public void setRelationMap(Map<String,String> m) {
		this.relationMap = m;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * 允许不带参数实例化
	 */
	private CriteriaModel() {
	}

	/**
	 * 使用指定的对象来获取一个HqlModel实例
	 * 
	 * @param object
	 *            实例化时所需要的DTO对象
	 */
	public static CriteriaModel instanceByObj(Object object) {
		CriteriaModel hqlModel = new CriteriaModel();
		hqlModel.setObject(object);
		return hqlModel;
	}

	/**
	 * 用来进行创建Criteria的方法， 此方法可能抛出异常
	 * 
	 * @return 根据传入对象的get方法构建的Criteria语句
	 */
	public Criteria getCriteria(Criteria c) throws Exception {
		// 得道给定实例的类型。
		Class<?> theClass = this.getObject().getClass();
		// 活的该类所有属性。
		Field[] fields = object.getClass().getDeclaredFields();

		// 遍历所有属性
		for (Field field : fields) {
			// System.out.println(field.getName());

			try {
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
						theClass);

				// 获得所有属性的读取方法
				Method getMethod = pd.getReadMethod();
				// 执行读取方法，获得属性值
				Object objTemp = getMethod.invoke(object);
				// 如果属性值为null，就略过
				if (objTemp == null) {
					continue;
				}
				if (objTemp instanceof String) {
					if (isEmpty((String) objTemp)) {
						continue;
					}
				}


				// 如果不为空。
				// 判断是否开启模糊查询，添加查询条件,并且加上%%符号。
				if (null != relationMap) {
					for (String obj : relationMap.keySet()) {
						String[] str = obj.split("_");
						if (str.length != 2) {
							continue;
						}
						String first = str[0];// 实体中要进行检索的 属性
						String re = str[1]; // 检索的关系
						String second = relationMap.get(obj); // 实体中储存检索要素的属性

						if (second.equals(field.getName())) {
							String dataType = this.findType(first);
							if (dataType.endsWith("Date")
									|| dataType.endsWith("Timestamp")) {
								this.dateCr(c, first, objTemp.toString(), re);
							}
							if (dataType.endsWith("String")) {
								this.stringCr(c, first, objTemp.toString(), re);
							}
							if (dataType.endsWith("Integer")
									|| dataType.endsWith("Double")
									|| dataType.endsWith("BigDecimal")
									|| dataType.endsWith("Long")) {
								this.numeralCr(c, first, objTemp, re);
							}
						}
					}
				}

			} catch (Exception e) {
				System.out.println(field.toGenericString()
						+ " Method not found !");
			}

		}

		// 遍历所有属性
		for (Field field : fields) {

			// 此处是排序功能
			for (String obj : relationMap.keySet()) {
				String[] str = obj.split("_");
				if (str.length != 2) {
					continue;
				}
				String first = str[0];
				String second = str[1];
				if ("desc".equalsIgnoreCase(second)) {
					if (field.getName().equalsIgnoreCase(first)) {
						c.addOrder(Order.desc(first));
					}
				}
				if ("asc".equalsIgnoreCase(second)) {
					if (field.getName().equalsIgnoreCase(first)) {
						c.addOrder(Order.asc(first));
					}
				}
			}
		}

		return c;
	}

	/**
	 * 在实体类里，通过参数名，取得参数的类型
	 * 
	 * @param sStr
	 *            传入的string对应实体内属性
	 * 
	 */
	public String findType(String tStr) throws Exception {
		String backType = "";
		// 活的该类所有属性。
		Field[] fields = object.getClass().getDeclaredFields();
		// 遍历所有属性
		for (Field field : fields) {
			if (tStr.equalsIgnoreCase(field.getName())) {
				backType = field.getType().toString();
			}
		}
		return backType;
	}

	/**
	 * 时间格式的Criteria 条件添加
	 * 
	 * @param c
	 *            查询容器 ，key 对应实体的属性，val 此属性要查询的值 ，rel 查询关系
	 * 
	 */
	private Criteria dateCr(Criteria c, String key, String val, String rel) {
		if ("eq".equalsIgnoreCase(rel)) {
			c.add(Restrictions.eq(key, StringToDate(val
					+ " 00:00:00 ", "yyyy-MM-dd hh:mm:ss")));
		}
		if ("ge".equalsIgnoreCase(rel)) {
			c.add(Restrictions.ge(key, StringToDate(val
					+ " 00:00:00 ", "yyyy-MM-dd hh:mm:ss")));
		}
		if ("le".equalsIgnoreCase(rel)) {
			c.add(Restrictions.le(key, StringToDate(val
					+ " 23:59:59 ", "yyyy-MM-dd hh:mm:ss")));
		}
		if ("gt".equalsIgnoreCase(rel)) {
			c.add(Restrictions.gt(key, StringToDate(val
					+ " 00:00:00 ", "yyyy-MM-dd hh:mm:ss")));
		}
		if ("lt".equalsIgnoreCase(rel)) {
			c.add(Restrictions.lt(key, StringToDate(val
					+ " 23:59:59 ", "yyyy-MM-dd hh:mm:ss")));
		}
		return c;
	}

	/**
	 * 数据格式的Criteria 条件添加
	 * 
	 * @param c
	 *            查询容器 ，key 对应实体的属性，val 此属性要查询的值 ，rel 查询关系
	 * 
	 */
	private Criteria numeralCr(Criteria c, String key, Object val, String rel) {

		if ("eq".equalsIgnoreCase(rel)) {
			c.add(Restrictions.eq(key, val));
		}
		if ("ge".equalsIgnoreCase(rel)) {
			c.add(Restrictions.ge(key, val));
		}
		if ("le".equalsIgnoreCase(rel)) {
			c.add(Restrictions.le(key, val));
		}
		if ("gt".equalsIgnoreCase(rel)) {
			c.add(Restrictions.gt(key, val));
		}
		if ("lt".equalsIgnoreCase(rel)) {
			c.add(Restrictions.lt(key, val));
		}
		return c;
	}

	/**
	 * string格式的Criteria 条件添加
	 * 
	 * @param c
	 *            查询容器 ，key 对应实体的属性，val 此属性要查询的值 ，rel 查询关系
	 * 
	 */
	private Criteria stringCr(Criteria c, String key, String val, String rel) {
		if ("eq".equalsIgnoreCase(rel)) {
			c.add(Restrictions.eq(key, val));
		}
		if ("%like%".equalsIgnoreCase(rel)) {
			c.add(Restrictions.like(key, "%" + val + "%"));
		}
		if ("%like".equalsIgnoreCase(rel)) {
			c.add(Restrictions.like(key, "%" + val));
		}
		if ("like%".equalsIgnoreCase(rel)) {
			c.add(Restrictions.like(key, val + "%"));
		}
		return c;
	}
	
	/**
	 * 判断字符串是否为<code>null<code>或为""
	 * @param str
	 * @return
	 */
	private boolean isEmpty(String str) {
		if(null == str || "".equals(str)) {
			return true;
		}
		return false;
	}
	
	/***************************************************************************
	 * 把format格式的字符串转化为java.util.date类型
	 * 
	 * @param strDate
	 * @return 返回转化后的Date对象如果strDate为null或""或转换失败 返回null
	 **************************************************************************/
	private Date StringToDate(String strDate, String format) {
		if (!isEmpty(strDate)) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				java.util.Date d = formatter.parse(strDate);
				return d;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

}
