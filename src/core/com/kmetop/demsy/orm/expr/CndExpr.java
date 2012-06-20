package com.kmetop.demsy.orm.expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.json.Json;

import com.kmetop.demsy.lang.Str;

/**
 * 条件表达式： 用于描述SQL查询语句的where条件部分
 * 
 * @author yongshan.ji
 */
public abstract class CndExpr extends Expr {
	// 排序表达式集合
	protected List<OrderExpr> orderExprs = new ArrayList();

	// 分组表达式集合
	protected List<GroupByExpr> groupByExprs = new ArrayList();

	// 分页表达式
	protected PagerExpr pagerExpr;

	// 分页表达式
	protected FieldRexpr fieldRexpr;

	public CndExpr and(CndExpr expr) {
		if (expr == null) {
			return this;
		}
		CndExpr ret = Expr.and(this, expr);
		copyOtherExpr(expr, ret);
		return ret;
	}

	public CndExpr or(CndExpr expr) {
		if (expr == null) {
			return this;
		}
		CndExpr ret = Expr.or(this, expr);
		copyOtherExpr(expr, ret);
		return ret;
	}

	public CndExpr not() {
		CndExpr ret = Expr.not(this);
		copyOtherExpr(null, ret);
		return ret;
	}

	private void copyOtherExpr(CndExpr from, CndExpr to) {
		to.orderExprs.addAll(this.orderExprs);
		to.groupByExprs.addAll(this.groupByExprs);
		if (from != null) {
			to.orderExprs.addAll(from.orderExprs);
			to.groupByExprs.addAll(from.groupByExprs);
		}
		to.pagerExpr = (this.pagerExpr == null && from != null) ? from.pagerExpr : this.pagerExpr;
		to.fieldRexpr = (this.fieldRexpr == null && from != null) ? from.fieldRexpr : this.fieldRexpr;
	}

	public CndExpr addOrder(NullCndExpr exp) {
		this.orderExprs.addAll(exp.getOrderExprs());
		return this;
	}

	public CndExpr setFieldRexpr(NullCndExpr exp) {
		this.fieldRexpr = exp.getFieldRexpr();
		return (CndExpr) this;
	}

	public CndExpr setPager(NullCndExpr exp) {
		this.pagerExpr = exp.getPagerExpr();
		return this;
	}

	public CndExpr addGroup(NullCndExpr exp) {
		this.groupByExprs.addAll(exp.getGroupExprs());
		return this;
	}

	public CndExpr addDesc(String prop) {
		this.orderExprs.add(new OrderExpr(prop, OrderType.desc));
		return this;
	}

	public CndExpr addAsc(String prop) {
		this.orderExprs.add(new OrderExpr(prop, OrderType.asc));
		return this;
	}

	public CndExpr addOrder(String stmt) {
		if (stmt.indexOf(" desc") > -1) {
			return this.addDesc(stmt.replace(" desc", ""));
		} else if (stmt.indexOf(" DESC") > -1) {
			return this.addDesc(stmt.replace(" DESC", ""));
		} else if (stmt.indexOf(" asc") > -1) {
			return this.addAsc(stmt.replace(" asc", ""));
		} else if (stmt.indexOf(" ASC") > -1) {
			return this.addAsc(stmt.replace(" ASC", ""));
		}

		return this.addAsc(stmt);
	}

	public CndExpr addGroup(String prop) {
		this.groupByExprs.add(new GroupByExpr(prop));
		return this;
	}

	public CndExpr setPager(int pageIndex, int pageSize) {
		this.pagerExpr = new PagerExpr(pageIndex, pageSize);
		return this;
	}

	public CndExpr setFieldRexpr(String rexpr, boolean igloreNull) {
		this.fieldRexpr = new FieldRexpr(rexpr, igloreNull);
		return (CndExpr) this;
	}

	// =============================================================
	// getter methods
	// =============================================================
	public List<OrderExpr> getOrderExprs() {
		return orderExprs;
	}

	public PagerExpr getPagerExpr() {
		return pagerExpr;
	}

	public List<GroupByExpr> getGroupExprs() {
		return groupByExprs;
	}

	public FieldRexpr getFieldRexpr() {
		return fieldRexpr;
	}

	public static CndExpr make(String rules) {
		if (Str.isEmpty(rules)) {
			return null;
		}
		if (rules.startsWith("[") && rules.endsWith("]")) {
			return make((List) Json.fromJson(rules));
		} else if (rules.startsWith("{") && rules.endsWith("}")) {
			ExprRuleGroup rg = Json.fromJson(ExprRuleGroup.class, rules);
			return rg.toExpr();
		} else {
			return make(Str.toList(rules, ","));
		}
	}

	/**
	 * 相同字段的“等于”表达式将被合并为“in”表达式，即相同字段的“等于”表达式被认为是“或”的关系；
	 * <p>
	 * 相同字段的“不等于”表达式将被合并为“not in”表达式，即相同字段的“不等于”表达式也被认为是“或”的关系；
	 * <p>
	 * 合并后的表达式之间被认为是“与”的关系。
	 * 
	 * @param rules
	 * @return
	 */
	public static CndExpr make(List<String> rules) {
		CndExpr expr = null;

		List<String> ruleFields = new LinkedList();
		Map<String, List<String>> inRules = new HashMap();
		Map<String, String> notInRules = new HashMap();
		List<ExprRule> others = new LinkedList();
		ExprRule rule = null;
		for (String naviRule : rules) {
			if (Str.isEmpty(naviRule)) {
				continue;
			}

			rule = new ExprRule(naviRule);
			String fld = rule.getField();

			if (Str.isEmpty(fld)) {
				continue;
			}

			if (!ruleFields.contains(fld))
				ruleFields.add(fld);

			// 处理 null 逻辑
			if ("ni".equals(rule.getOp())) {
				if (!notInRules.containsKey(rule.getField())) {
					notInRules.put(rule.getField(), (String) rule.getData());
				}
				continue;
			}

			// 默认按 in 逻辑处理
			List<String> list = inRules.get(fld);
			if (list == null) {
				list = new LinkedList();
				inRules.put(fld, list);
			}

			String op = rule.getOp();
			if ("in".equals(op)) {
				list.addAll(Str.toList((String) rule.getData(), ",;"));
			} else if ("eq".equals(op) || "=".equals(op)) {// 将多个eq转换为in
				list.add((String) rule.getData());
			} else {
				others.add(rule);
			}
		}

		CndExpr naviExpr = null;
		for (String fld : ruleFields) {

			List<String> data = inRules.get(fld);
			if (data != null && data.size() > 0) {
				if (data.size() == 1)
					naviExpr = CndExpr.eq(fld, data.get(0));
				else
					naviExpr = CndExpr.in(fld, data);
			}
			if (notInRules.get(fld) != null) {
				CndExpr tmp = Expr.notIn(fld, Str.toList(notInRules.get(fld), ",")).or(Expr.isNull(fld));
				if (naviExpr == null) {
					naviExpr = tmp;
				} else {
					naviExpr = naviExpr.or(tmp);
				}
			}

			if (naviExpr != null) {
				if (expr == null) {
					expr = naviExpr;
				} else {
					expr = expr.and(naviExpr);
				}
			}
		}

		for (ExprRule exprRule : others) {
			if (expr == null) {
				expr = exprRule.toExpr();
			} else {
				expr = expr.and(exprRule.toExpr());
			}
		}

		return expr;
	}
}
