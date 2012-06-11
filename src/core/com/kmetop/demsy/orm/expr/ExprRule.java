package com.kmetop.demsy.orm.expr;

import java.util.List;

import com.kmetop.demsy.lang.Str;

/**
 * <UL>
 * <LI>eq: equal, =
 * <LI>ne: not equal, <>
 * <LI>lt: less, <
 * <LI>le: less or equal,<=
 * <LI>gt: greater, >
 * <LI>ge: greater or equal, >=
 * <LI>bw: begins with, LIKE
 * <LI>bn: does not begin with, NOT LIKE
 * <LI>in: in, IN
 * <LI>ni: not in, NOT IN
 * <LI>ew: ends with, LIKE
 * <LI>en: does not end with, NOT LIKE
 * <LI>cn: contains, LIKE
 * <LI>nc: does not contain, NOT LIKE
 * <LI>nu: is null, IS NULL
 * <LI>nn: is not null, IS NOT NULL
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class ExprRule {

	public SimpleCndExpr toExpr() {

		if (op.equals("eq") || op.equals("="))
			return Expr.eq(field, data);

		if (op.equals("ne") || op.equals("<>"))
			return Expr.ne(field, data);

		if (op.equals("lt") || op.equals("<"))
			return Expr.lt(field, data);

		if (op.equals("le") || op.equals("<="))
			return Expr.le(field, data);

		if (op.equals("gt") || op.equals(">"))
			return Expr.gt(field, data);

		if (op.equals("ge") || op.equals(">="))
			return Expr.ge(field, data);

		if (op.equals("bw"))
			return Expr.beginWith(field, (String) data);

		if (op.equals("bn"))
			return Expr.notBeginWith(field, (String) data);

		if (op.equals("in") || op.toUpperCase().equals("IN")) {
			if (data instanceof List)
				return Expr.in(field, (List) data);
			else
				return Expr.in(field, Str.toList(data.toString(), ","));
		}

		if (op.equals("ni") || op.toUpperCase().equals("NOT IN"))
			if (data instanceof List)
				return Expr.notIn(field, (List) data);
			else
				return Expr.notIn(field, Str.toList(data.toString(), ","));

		if (op.equals("ew"))
			return Expr.endWith(field, (String) data);

		if (op.equals("en"))
			return Expr.notEndWith(field, (String) data);

		if (op.equals("cn") || op.toUpperCase().equals("LIKE"))
			return Expr.contains(field, (String) data);

		if (op.equals("nc") || op.toUpperCase().equals("NOT LIKE"))
			return Expr.notContains(field, (String) data);

		if (op.equals("nu") || op.toUpperCase().equals("IS NULL"))
			return Expr.isNull(field);

		if (op.equals("nn") || op.toUpperCase().equals("IS NOT NULL"))
			return Expr.notNull(field);

		return null;
	}

	private String field;

	private String op;

	private Object data;

	public ExprRule() {
	}

	public ExprRule(String f, String o, Object d) {
		this.field = f;
		this.op = o;
		this.data = d;
	}

	public ExprRule(String rule) {
		if (rule != null) {
			rule = rule.trim();

			// field
			int blank = rule.indexOf(" ");
			if (blank > 0) {
				this.field = rule.substring(0, blank);
				rule = rule.substring(blank + 1).trim();
				this.op = rule;
			}

			// op
			blank = rule.indexOf(" ");
			if (blank > 0) {
				this.op = rule.substring(0, blank);
				this.data = rule.substring(blank + 1).trim();
			}

		}
	}

	public String getField() {
		return field;
	}

	public String getOp() {
		return op;
	}

	public Object getData() {
		return data;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String toString() {
		return field + " " + op + " " + data;
	}
}
