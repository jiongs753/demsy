package com.kmetop.demsy.orm.expr;


/**
 * 分页表达式
 * 
 * @author yongshan.ji
 */
public class PagerExpr extends Expr {
	public static int DEFAULT_PAGE_SIZE = 20;
	public static int DEFAULT_PAGE_INDEX = 1;

	private int pageSize = DEFAULT_PAGE_SIZE;// 记录大小
	private int pageIndex = DEFAULT_PAGE_INDEX;// 页索引

	PagerExpr(int pageIndex, int pageSize) {
		if (pageIndex > 0) {
			this.pageIndex = pageIndex;
		}
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}
	}

	/**
	 * 获取也大小
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取也索引
	 * 
	 * @return
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + new Integer(pageSize).hashCode();
		result = 37 * result + new Integer(pageIndex).hashCode();
		return result;
	}

	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}
		if (!getClass().equals(that.getClass())) {
			return false;
		}
		PagerExpr thatObj = (PagerExpr) that;
		if ((this == that)) {
			return true;
		}
		if (pageSize == thatObj.pageSize && pageIndex == thatObj.pageIndex) {
			return true;
		}
		return false;
	}
}
