package com.kmetop.demsy.orm.expr;

import com.kmetop.demsy.lang.Str;

/**
 * 分组表达式：用于生成SQL语句中的 group by 部分。
 * 
 * @author yongshan.ji
 */
public class GroupByExpr extends Expr {

	private String prop;

	GroupByExpr(String prop) {
		this.prop = prop;
	}

	/**
	 * 获取排序字段
	 * 
	 * @return
	 */
	public String getProp() {
		return prop;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + prop.hashCode();
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
		GroupByExpr thatObj = (GroupByExpr) that;
		if ((this == that)) {
			return true;
		}
		if (Str.equals(prop, thatObj.prop)) {
			return true;
		}
		return false;
	}

	public String toString() {
		return prop;
	}

}
