package com.kmetop.demsy.orm.expr;

/**
 * AND 或者 OR
 */
public class ExprRuleGroup {

	private String groupOp;

	private ExprRule[] rules;

	private ExprRuleGroup[] groups;

	public ExprRuleGroup() {
	}

	public ExprRuleGroup(String groupOp, ExprRule[] rules) {
		this.groupOp = groupOp;
		this.rules = rules;
	}

	public CndExpr toExpr() {
		if (rules == null) {
			return null;
		}
		CndExpr expr = null;
		if (rules != null) {
			if (groupOp.toUpperCase().equals("AND"))
				for (ExprRule r : rules) {
					if (expr == null)
						expr = r.toExpr();
					else
						expr = expr.and(r.toExpr());
				}
			else
				for (ExprRule r : rules) {
					if (expr == null)
						expr = r.toExpr();
					else
						expr = expr.or(r.toExpr());
				}
		}

		if (groups != null) {
			if (groupOp.toUpperCase().equals("AND"))
				for (ExprRuleGroup r : groups) {
					if (expr == null)
						expr = r.toExpr();
					else
						expr = expr.and(r.toExpr());
				}
			else
				for (ExprRuleGroup r : groups) {
					if (expr == null)
						expr = r.toExpr();
					else
						expr = expr.or(r.toExpr());
				}
		}

		return expr;
	}

	public String getGroupOp() {
		return groupOp;
	}

	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}

	public ExprRule[] getRules() {
		return rules;
	}

	public void setRules(ExprRule[] rules) {
		this.rules = rules;
	}

	public ExprRuleGroup[] getGroups() {
		return groups;
	}

	public void setGroups(ExprRuleGroup[] groups) {
		this.groups = groups;
	}

}
