package com.yinli.dao;


import java.io.Serializable;

/**
 * 查询基类   分页参数
 */
public class BaseQuery implements Serializable {
	/**
	 * 序列化标识
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 默认首页
	 */
	private static final Integer defaultFristPage = new Integer(1);
	/**
	 * 默认总条数 
	 */
	private static final Integer defaultTotleItem = new Integer(0);
	/**
	 * 每页显示条数
	 */
	private Integer pageSize = new Integer(10);
	
	/**
	 * 当前页
	 */
	private Integer currPage = 1;
	
	/**
	 * 总条数
	 */
	private Integer totalItem;
	/**
	 * 排序标记   1：降序 ; 2：升序
	 */
//	private Integer orderMark;
	/**
	 * 排序字段索引
	 */
//	private Integer fieldIndex;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if(pageSize != null) {
			this.pageSize = pageSize;
		}
	}

	public Integer getCurrPage() {
		if(currPage == null) {
			return defaultFristPage;
		}
		return currPage;
	}

	public void setCurrPage(Integer currPage) {
		if (currPage == null || currPage.intValue() <= 0) {
			this.currPage = null;
		} else {
			this.currPage = currPage;
		}
	}
	
	public void setCurrPageString(String currPage) {
		if (currPage != null && !"".equals(currPage.trim())) {
			this.setCurrPage(new Integer(1));
		}
		try {
			Integer integer = new Integer(currPage);
			this.setCurrPage(integer);
		} catch (NumberFormatException ignore) {
			this.setCurrPage(new Integer(1));
		}
	}

	public Integer getTotalItem() {
		if(totalItem == null) {
			return defaultTotleItem;
		}
		return totalItem;
	}

	public void setTotalItem(Integer totalItem) {
		if(totalItem == null) {
			totalItem = defaultTotleItem;
		} else {
			this.totalItem = totalItem;
			int current = this.getCurrPage().intValue();
			int lastPage = this.getTotalPages();
			if (current > lastPage) {
				this.setCurrPage(new Integer(lastPage));
			}
		}
	}


	/**
	 * <pre>
	 * @Title: 获取总页数
	 * @Description: 根据总条数和每页显示数据计算总页数
	 *
	 * @return Integer    总页数  
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public Integer getTotalPages() {
		Integer totalPages = this.getTotalItem() / this.getPageSize();
		if(this.getTotalItem() % this.getPageSize() > 0) {
			totalPages ++;
		}
		return totalPages;
	}
	
	/**
	 * 
	 * <pre>
	 * @Title: 取得查询起始行
	 * @Description: 根据当前页和每页显示条数计算查询起始行
	 *
	 * @return Integer   查询起始行
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public Integer getStartRow() {
		return (this.getCurrPage() - 1) * this.getPageSize();
	}
	
	/**
	 * <pre>
	 * @Title: 取得下一页 
	 * @Description: 取得下一页 
	 *
	 * @return Integer   下一页  
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public Integer getNextPage() {
		Integer page = this.getCurrPage() + 1;
		if(page > getTotalPages()) {
			page = getTotalPages();
		}
		return page;
	}
	
	/**
	 * <pre>
	 * @Title: 上一页
	 * @Description: 上一页
	 *
	 * @return Integer   上一页
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public Integer getPrePage() {
		Integer page = this.getCurrPage() - 1;
		if(page < 1) {
			page = 1;
		}
		return page;
	}
	
	/**
	 * <pre>
	 * @Title: 是否首页
	 * @Description: 是否首页
	 *
	 * @return boolean  是否首页
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public boolean isFirstPage() {
		if(this.getCurrPage().intValue() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * <pre>
	 * @Title: 是否末页
	 * @Description: 是否末页
	 *
	 * @return boolean   是否末页
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public boolean isLastPage() {
		if(this.getCurrPage().intValue() >= getTotalPages().intValue()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * <pre>
	 * @Title: 获得当前页第1条的位置
	 * @Description: 获得当前页第1条的位置
	 *
	 * @return Integer    返回类型 
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public Integer getCurrFirstItem() {
		return (this.getCurrPage().intValue() - 1) * this.getPageSize().intValue() + 1;
	}
	
	/**
	 * <pre>
	 * @Title: 获得当前页最后一条的位置
	 * @Description: 获得当前页最后一条的位置
	 *
	 * @return 
	 * 
	 * @return Integer    返回类型 
	 * @throws 
	 * ************************* 修改记录 *********************************
	 *   修改时间     |  修改人员    |    修改原因
	 *
	 * </pre>
	 */
	public Integer getCurrLastItem() {
		if(this.isLastPage()) {
			return this.getTotalItem();
		}
		return this.getPageSize() * this.getCurrPage();
	}
}
