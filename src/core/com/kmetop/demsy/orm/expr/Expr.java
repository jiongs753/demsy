package com.kmetop.demsy.orm.expr;

import java.util.List;

/**
 * 
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
 * <LI>lk: LIKE
 * <LI>nl: NOT LIKE
 * <LI>gl: between, greater and less
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public abstract class Expr {

	public static SimpleCndExpr rule(String field, String op, String data) {
		return new ExprRule(field, op, data).toExpr();
	}

	public static SimpleCndExpr eq(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.eq, value);
	}

	public static SimpleCndExpr gt(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.gt, value);
	}

	public static SimpleCndExpr ge(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.ge, value);
	}

	public static SimpleCndExpr le(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.le, value);
	}

	public static SimpleCndExpr lt(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.lt, value);
	}

	public static SimpleCndExpr ne(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.ne, value);
	}

	public static SimpleCndExpr eqProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.eq, prop2);
	}

	public static SimpleCndExpr gtProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.gt, prop2);
	}

	public static SimpleCndExpr geProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.ge, prop2);
	}

	public static SimpleCndExpr leProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.le, prop2);
	}

	public static SimpleCndExpr ltProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.lt, prop2);
	}

	public static SimpleCndExpr neProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.ne, prop2);
	}

	public static SimpleCndExpr in(String prop, List value) {
		return new SimpleCndExpr(prop, CndType.in, value);
	}

	public static SimpleCndExpr notIn(String prop, List value) {
		return new SimpleCndExpr(prop, CndType.ni, value);
	}

	public static SimpleCndExpr like(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.lk, value);
	}

	public static SimpleCndExpr contains(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.lk, "%" + value + "%");
	}

	public static SimpleCndExpr notContains(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.nl, "%" + value + "%");
	}

	public static SimpleCndExpr beginWith(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.lk, value + "%");
	}

	public static SimpleCndExpr notBeginWith(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.nl, value + "%");
	}

	public static SimpleCndExpr endWith(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.lk, "%" + value);
	}

	public static SimpleCndExpr notEndWith(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.nl, "%" + value);
	}

	public static SimpleCndExpr between(String prop, Object lo, Object hi) {
		SimpleCndExpr w = new SimpleCndExpr(prop, CndType.gl, lo);
		w.value2 = hi;
		return w;
	}

	public static SimpleCndExpr isNull(String prop) {
		return new SimpleCndExpr(prop, CndType.nu, null);
	}

	public static SimpleCndExpr notNull(String prop) {
		return new SimpleCndExpr(prop, CndType.nn, null);
	}

	public static CombCndExpr and(CndExpr exp1, CndExpr exp2) {
		return new CombCndExpr(exp1, CombType.and, exp2);
	}

	public static CombCndExpr or(CndExpr exp1, CndExpr exp2) {
		return new CombCndExpr(exp1, CombType.or, exp2);
	}

	public static CombCndExpr not(CndExpr exp) {
		return new CombCndExpr(exp, CombType.not, null);
	}

	public static NullCndExpr desc(String prop) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.addOrder(new OrderExpr(prop, OrderType.desc));
		return nexp;
	}

	public static NullCndExpr asc(String prop) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.addOrder(new OrderExpr(prop, OrderType.asc));
		return nexp;
	}

	public static NullCndExpr orderby(String prop) {
		NullCndExpr nexp = new NullCndExpr();
		boolean desc = prop.indexOf(" desc") > 0;
		if (desc)
			nexp.addOrder(new OrderExpr(prop.replace(" desc", ""), OrderType.desc));
		else
			nexp.addOrder(new OrderExpr(prop.replace(" asc", ""), OrderType.asc));

		return nexp;
	}

	public static NullCndExpr group(String prop) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.addGroup(new GroupByExpr(prop));
		return nexp;
	}

	public static NullCndExpr page(int pageIndex, int pageSize) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.setPager(new PagerExpr(pageIndex, pageSize));
		return nexp;
	}

	public static NullCndExpr fieldRexpr(String rexpr) {
		return fieldRexpr(rexpr, false);
	}

	public static NullCndExpr fieldRexpr(String rexpr, boolean igloreNull) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.setFieldRexpr(new FieldRexpr(rexpr, igloreNull));
		return nexp;
	}

}
