package com.kmetop.demsy.orm.expr;

import com.kmetop.demsy.lang.Str;

/**
 * 字段正则表达式：用来描述SQL语句中要查询、修改、添加的字段集合。
 * 
 * @author yongshan.ji
 */
public class FieldRexpr extends Expr {

	private String regExpr;

	private boolean igloreNull;

	FieldRexpr(String regExpr, boolean igloreNull) {
		this.regExpr = regExpr;
	}

	/**
	 * 获取正则表达式
	 * 
	 * @return
	 */
	public String getRegExpr() {
		return regExpr;
	}

	public boolean isIgloreNull() {
		return igloreNull;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + new Boolean(igloreNull).hashCode();
		result = 37 * result + regExpr.hashCode();
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
		FieldRexpr thatObj = (FieldRexpr) that;
		if ((this == that)) {
			return true;
		}
		if (igloreNull == thatObj.igloreNull && Str.equals(regExpr, thatObj.regExpr)) {
			return true;
		}
		return false;
	}

	public String toString() {
		return this.regExpr;
	}

}
