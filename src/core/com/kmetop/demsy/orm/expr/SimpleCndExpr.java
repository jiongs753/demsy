package com.kmetop.demsy.orm.expr;

import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;

/**
 * 条件表达式： 用于描述SQL查询语句的where条件部分
 * 
 * @author yongshan.ji
 */
public class SimpleCndExpr extends CndExpr {

	private CndType type;

	private String prop;

	private Object value;

	Object value2;

	private void init(String prop, CndType type, Object value) {
		this.type = type;
		this.prop = prop;
		this.value = value;
	}

	public SimpleCndExpr(String prop, CndType type, Object value) {
		this.init(prop, type, value);
	}

	public String getProp() {
		return prop;
	}

	public Object getValue() {
		return value;
	}

	public Object getValue2() {
		return value2;
	}

	public CndType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + type.hashCode();
		result = 37 * result + prop.hashCode();
		if (value != null)
			result = 37 * result + value.hashCode();
		if (value2 != null)
			result = 37 * result + value2.hashCode();
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
		SimpleCndExpr thatObj = (SimpleCndExpr) that;
		if ((this == that)) {
			return true;
		}
		if (type == thatObj.type && Str.equals(prop, thatObj.prop) && Obj.equals(value, thatObj.value)
				&& Obj.equals(value2, thatObj.value2)) {
			return true;
		}
		return false;
	}

}
