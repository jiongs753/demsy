package com.kmetop.demsy.orm;

import java.util.Collections;
import java.util.List;

import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.orm.expr.PagerExpr;

/**
 * 查询分页器
 * 
 * @author yongshan.ji
 */
public class Pager<T> {
	// 实体类
	private Class<T> type;

	private CndExpr queryExpr;

	private Integer pageSize;

	private Integer pageIndex;

	// 总记录数
	private int totalRecord = -1;

	// 结果集
	private List<T> result;

	public static Pager make(Class type, int pageSize, int pageIndex, CndExpr queryExpr) {
		Pager pager = new Pager(type);
		pager.setQueryExpr(queryExpr.setPager(Expr.page(pageIndex, pageSize)));

		return pager;
	}

	public Pager(Class<T> entityClass) {
		this.type = entityClass;
	}

	/**
	 * 取得页内的记录列表.
	 */
	public List<T> getResult() {
		if (result == null)
			return Collections.emptyList();

		return result;
	}

	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPageIndex() {
		if (pageIndex != null) {
			return pageIndex;
		}
		if (queryExpr == null) {
			pageIndex = PagerExpr.DEFAULT_PAGE_INDEX;
			return pageIndex;
		}
		PagerExpr pageExpr = queryExpr.getPagerExpr();
		if (pageExpr == null) {
			pageIndex = PagerExpr.DEFAULT_PAGE_INDEX;
			return pageIndex;
		}
		pageIndex = pageExpr.getPageIndex();
		return pageIndex;
	}

	/**
	 * 获得每页的记录数量,默认为1.
	 */
	public int getPageSize() {
		if (pageSize != null) {
			return pageSize;
		}
		if (queryExpr == null) {
			pageSize = PagerExpr.DEFAULT_PAGE_SIZE;
			return pageSize;
		}
		PagerExpr pageExpr = queryExpr.getPagerExpr();
		if (pageExpr == null) {
			pageSize = PagerExpr.DEFAULT_PAGE_SIZE;
			return pageSize;
		}
		pageSize = pageExpr.getPageSize();
		return pageSize;
	}

	/**
	 * 根据pageIndex和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 */
	public int getFromRecord() {
		return ((getPageIndex() - 1) * getPageSize()) + 1;
	}

	/**
	 * 偏移量： 即结果集中记录序号的的起始位置
	 * 
	 * @return
	 */
	public int getToRecord() {
		return (getPageIndex() - 1) * getPageSize() + (result == null ? getPageSize() : result.size());
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	/**
	 * 总页数： 根据pageSize与totalRecord自动计算, 默认值为-1.
	 */
	public int getTotalPage() {
		if (totalRecord < 0)
			return -1;

		int count = totalRecord / getPageSize();
		if (totalRecord % getPageSize() > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean hasNext() {
		return (getPageIndex() + 1 <= getTotalPage());
	}

	/**
	 * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPage() {
		if (hasNext())
			return getPageIndex() + 1;
		else
			return getPageIndex();
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean hasPre() {
		return (getPageIndex() - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
	 */
	public int getPrePage() {
		if (hasPre())
			return getPageIndex() - 1;
		else
			return getPageIndex();
	}

	public Class getType() {
		return type;
	}

	public void setType(Class entityClass) {
		this.type = entityClass;
	}

	public CndExpr getQueryExpr() {
		return queryExpr;
	}

	public void setQueryExpr(CndExpr expr) {
		this.queryExpr = expr;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

}
