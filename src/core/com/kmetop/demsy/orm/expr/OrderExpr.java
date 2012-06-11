package com.kmetop.demsy.orm.expr;

import com.kmetop.demsy.lang.Str;

/**
 * 排序表达式：用来描述SQL语句中的 order by 部分。
 * 
 * @author yongshan.ji
 */
public class OrderExpr extends Expr {

	private OrderType type;

	private String prop;

	OrderExpr(String prop, OrderType type) {
		this.prop = prop;
		this.type = type;
	}

	/**
	 * 获取排序字段
	 * 
	 * @return
	 */
	public String getProp() {
		return prop;
	}

	public OrderType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + type.hashCode();
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
		OrderExpr thatObj = (OrderExpr) that;
		if ((this == that)) {
			return true;
		}
		if (type == thatObj.type && Str.equals(prop, thatObj.prop)) {
			return true;
		}
		return false;
	}

}
