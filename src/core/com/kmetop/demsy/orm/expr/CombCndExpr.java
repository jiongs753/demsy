package com.kmetop.demsy.orm.expr;

import com.kmetop.demsy.lang.Obj;

/**
 * 组合表达式：用来描述条件组合
 * <p>
 * 用关系运算符AND/OR/NOT于将条件表达式组合在一起成为SQL语句的where部分。
 * 
 * @author yongshan.ji
 */
public class CombCndExpr extends CndExpr {

	private CombType type;
	private CndExpr expr;
	private CndExpr expr2;

	CombCndExpr(CndExpr expr, CombType type, CndExpr expr2) {
		this.type = type;
		this.expr = expr;
		this.expr2 = expr2;
	}

	public CndExpr getExpr() {
		return expr;
	}

	public CndExpr getExpr2() {
		return expr2;
	}

	public CombType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + type.hashCode();
		result = 37 * result + expr.hashCode();
		if (expr2 != null)
			result = 37 * result + expr2.hashCode();
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
		CombCndExpr thatObj = (CombCndExpr) that;
		if ((this == that)) {
			return true;
		}
		if (type == thatObj.type && Obj.equals(expr, thatObj.expr) && Obj.equals(expr2, thatObj.expr2)) {
			return true;
		}
		return false;
	}
}
