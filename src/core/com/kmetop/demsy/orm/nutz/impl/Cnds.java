package com.kmetop.demsy.orm.nutz.impl;

import java.util.Date;
import java.util.List;

import org.nutz.dao.DemsyCnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Expression;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.Link;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Strings;

import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Dates;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.CndType;
import com.kmetop.demsy.orm.expr.CombCndExpr;
import com.kmetop.demsy.orm.expr.CombType;
import com.kmetop.demsy.orm.expr.FieldRexpr;
import com.kmetop.demsy.orm.expr.GroupByExpr;
import com.kmetop.demsy.orm.expr.NullCndExpr;
import com.kmetop.demsy.orm.expr.OrderExpr;
import com.kmetop.demsy.orm.expr.OrderType;
import com.kmetop.demsy.orm.expr.PagerExpr;
import com.kmetop.demsy.orm.expr.SimpleCndExpr;
import com.kmetop.demsy.orm.nutz.EnColumnMappingImpl;

abstract class Cnds {
	private static Log log = Logs.getLog(Cnds.class);

	public static Condition toCnd(CndExpr expr) {
		DemsyCnd cnd = cnd(expr);
		if (expr != null) {

			List<GroupByExpr> groupExprs = expr.getGroupExprs();
			if (groupExprs != null && groupExprs.size() > 0) {
				if (cnd == null)
					cnd = DemsyCnd.groupBy();

				for (GroupByExpr by : groupExprs)
					cnd = cnd.addGroup(by.getProp());
			}

			List<OrderExpr> oexps = expr.getOrderExprs();
			if (oexps != null && oexps.size() > 0) {
				if (cnd == null) {
					cnd = (DemsyCnd) DemsyCnd.orderBy();
				}
				for (OrderExpr oexp : oexps) {
					orderBy(cnd, oexp);
				}
			}
		}

		return cnd;
	}

	public static Pager toPager(Dao dao, CndExpr expr) {
		if (expr == null) {
			return null;
		}
		PagerExpr pexp = expr.getPagerExpr();
		if (pexp != null) {
			return dao.createPager(pexp.getPageIndex(), pexp.getPageSize());
		}
		return null;
	}

	public static String fieldRexpr(CndExpr expr) {
		if (expr == null) {
			return null;
		}
		FieldRexpr rexpr = expr.getFieldRexpr();
		return rexpr == null ? null : rexpr.getRegExpr();
	}

	public static boolean isIgloreNull(CndExpr expr) {
		if (expr == null) {
			return false;
		}
		FieldRexpr rexpr = expr.getFieldRexpr();
		return rexpr == null ? false : rexpr.isIgloreNull();
	}

	private static void orderBy(DemsyCnd cnd, OrderExpr exp) {
		OrderType type = exp.getType();
		if (type == OrderType.asc) {
			cnd.asc(exp.getProp());
		} else if (type == OrderType.desc) {
			cnd.desc(exp.getProp());
		}
	}

	public static DemsyCnd cnd(CndExpr expr) {
		if (expr instanceof CombCndExpr) {
			return cnd((CombCndExpr) expr);
		}
		if (expr instanceof SimpleCndExpr) {
			return cnd((SimpleCndExpr) expr);
		}
		if (expr instanceof NullCndExpr) {
			return null;
		}
		return null;
	}

	private static DemsyCnd cnd(CombCndExpr expr) {
		Expression cnd1 = cnd(expr.getExpr());
		Expression cnd2 = cnd(expr.getExpr2());

		CombType type = expr.getType();
		if (type == CombType.and) {
			return Cnds.and(cnd1, cnd2);
		}
		if (type == CombType.or) {
			return Cnds.or(cnd1, cnd2);
		}
		if (type == CombType.not) {
			return Cnds.not(cnd1);
		}

		return null;
	}

	private static DemsyCnd cnd(SimpleCndExpr expr) {
		DemsyCnd cnd = null;

		String prop = expr.getProp();
		Object value = expr.getValue();
		CndType type = expr.getType();
		if (type == CndType.gl) {
			cnd = Cnds.gl(prop, value, expr.getValue2());
		} else if (type == CndType.eq) {
			cnd = Cnds.eq(prop, value);
		} else if (type == CndType.ge) {
			cnd = Cnds.ge(prop, value);
		} else if (type == CndType.gt) {
			cnd = Cnds.gt(prop, value);
		} else if (type == CndType.in) {
			cnd = Cnds.in(prop, (List) value);
		} else if (type == CndType.ni) {
			cnd = Cnds.ni(prop, (List) value);
		} else if (type == CndType.nn) {
			cnd = Cnds.nn(prop);
		} else if (type == CndType.nu) {
			cnd = Cnds.nu(prop);
		} else if (type == CndType.le) {
			cnd = Cnds.le(prop, value);
		} else if (type == CndType.lk) {
			cnd = Cnds.lk(prop, (String) value);
		} else if (type == CndType.nl) {
			cnd = Cnds.nl(prop, (String) value);
		} else if (type == CndType.lt) {
			cnd = Cnds.lt(prop, value);
		} else if (type == CndType.ne) {
			cnd = Cnds.ne(prop, value);
		}
		return cnd;
	}

	static DemsyCnd and(Expression exp1, Condition con) {
		if (con != null) {
			return (DemsyCnd) DemsyCnd.exps(exp1).and(new ConditionExpression(con));
		} else {
			return DemsyCnd.where(exp1);
		}
	}

	static DemsyCnd and(Expression exp1, Expression exp2) {
		if (exp1 != null && exp2 != null)
			return (DemsyCnd) DemsyCnd.exps(exp1).and(DemsyCnd.exps(exp2));
		if (exp1 != null)
			return (DemsyCnd) DemsyCnd.exps(exp1);
		if (exp2 != null)
			return (DemsyCnd) DemsyCnd.exps(exp2);

		return null;
	}

	static DemsyCnd or(Expression exp1, Expression exp2) {
		if (exp1 != null && exp2 != null)
			return (DemsyCnd) DemsyCnd.exps(exp1).or(DemsyCnd.exps(exp2));
		if (exp1 != null)
			return (DemsyCnd) DemsyCnd.exps(exp1);
		if (exp2 != null)
			return (DemsyCnd) DemsyCnd.exps(exp2);

		return null;
	}

	static DemsyCnd not(Expression exp) {
		if (exp == null) {
			return null;
		}
		return (DemsyCnd) DemsyCnd.exps(new NotExpression(exp));
	}

	static DemsyCnd lk(String propertyName, String value) {
		return DemsyCnd.where(new Exp(propertyName, "like", value));
	}

	static DemsyCnd nl(String propertyName, String value) {
		return DemsyCnd.where(new Exp(propertyName, "not like", value));
	}

	static DemsyCnd bw(String propertyName, String value) {
		return DemsyCnd.where(new Exp(propertyName, "like", value + "%"));
	}

	static DemsyCnd ew(String propertyName, String value) {
		return DemsyCnd.where(new Exp(propertyName, "like", "%" + value));
	}

	static DemsyCnd cn(String propertyName, String value) {
		return DemsyCnd.where(new Exp(propertyName, "like", "%" + value + "%"));
	}

	static DemsyCnd in(String propertyName, List value) {
		return DemsyCnd.where(new Exp(propertyName, "in", value));
	}

	static DemsyCnd ni(String propertyName, List value) {
		return DemsyCnd.where(new Exp(propertyName, "not in", value));
	}

	static DemsyCnd eq(String propertyName, Object value) {
		return DemsyCnd.where(new Exp(propertyName, "=", value));
	}

	static DemsyCnd ge(String propertyName, Object value) {
		return DemsyCnd.where(new Exp(propertyName, ">=", value));
	}

	static DemsyCnd gt(String propertyName, Object value) {
		return DemsyCnd.where(new Exp(propertyName, ">", value));
	}

	static DemsyCnd ne(String propertyName, Object value) {
		return DemsyCnd.where(new Exp(propertyName, "<>", value));
	}

	static DemsyCnd lt(String propertyName, Object value) {
		return DemsyCnd.where(new Exp(propertyName, "<", value));
	}

	static DemsyCnd le(String propertyName, Object value) {
		return DemsyCnd.where(new Exp(propertyName, "<=", value));
	}

	static DemsyCnd nu(String propertyName) {
		return DemsyCnd.where(new NullExpression(propertyName));
	}

	static DemsyCnd nn(String propertyName) {
		return DemsyCnd.where(new NotNullExpression(propertyName));
	}

	static DemsyCnd gl(String propertyName, Object lo, Object hi) {
		return DemsyCnd.where(new BetweenExpression(propertyName, lo, hi));
	}

	static DemsyCnd peq(String propertyName, String otherPropertyName) {
		return DemsyCnd.where(new PropertyExpression(propertyName, "=", otherPropertyName));
	}

	static DemsyCnd pge(String propertyName, String otherPropertyName) {
		return DemsyCnd.where(new PropertyExpression(propertyName, ">=", otherPropertyName));
	}

	static DemsyCnd pgt(String propertyName, String otherPropertyName) {
		return DemsyCnd.where(new PropertyExpression(propertyName, ">", otherPropertyName));
	}

	static DemsyCnd pne(String propertyName, String otherPropertyName) {
		return DemsyCnd.where(new PropertyExpression(propertyName, "<>", otherPropertyName));
	}

	static DemsyCnd plt(String propertyName, String otherPropertyName) {
		return DemsyCnd.where(new PropertyExpression(propertyName, "<", otherPropertyName));
	}

	static DemsyCnd ple(String propertyName, String otherPropertyName) {
		return DemsyCnd.where(new PropertyExpression(propertyName, "<=", otherPropertyName));
	}

	private static class PropertyExpression implements Expression {

		private final String propertyName;

		private final String otherPropertyName;

		private final String op;

		protected PropertyExpression(String propertyName, String op, String otherPropertyName) {
			this.propertyName = propertyName;
			this.op = op;
			this.otherPropertyName = otherPropertyName;
		}

		public void render(StringBuilder sb, Entity<?> en) {
			if (null != en) {
				EntityField ef = en.getField(propertyName);
				sb.append(null != ef ? ef.getColumnName() : propertyName);
			} else
				sb.append(propertyName);

			sb.append(op);

			if (null != en) {
				EntityField ef = en.getField(otherPropertyName);
				sb.append(null != ef ? ef.getColumnName() : otherPropertyName);
			} else
				sb.append(otherPropertyName);
		}
	}

	private static class BetweenExpression implements Expression {
		private final String name;

		private final Object lo;

		private final Object hi;

		protected BetweenExpression(String propertyName, Object lo, Object hi) {
			this.name = propertyName;
			this.lo = lo;
			this.hi = hi;
		}

		public void render(StringBuilder sb, Entity<?> en) {
			String subSelect = null;
			if (null != en) {
				EntityField ef = en.getField(name);
				if (ef == null) {
					subSelect = buildSubSelect(en, name);
					sb.append(subSelect);
				} else
					sb.append(null != ef ? ef.getColumnName() : name);

			} else {
				sb.append(name);
			}
			sb.append(" between " + lo + " and " + hi);

			if (subSelect != null) {
				sb.append(" )");
			}

			log.debugf("条件表达式[%s<%s>]: %s", en == null ? "" : (en.getType().getSimpleName()), en == null ? "" : en.getTableName(), sb);
		}
	}

	private static class NullExpression implements Expression {
		NullExpression(String propertyName) {
			this.name = propertyName;
			int dot = name.indexOf(".");
			if (dot > 0) {
				name = name.substring(0, dot);
			}
		}

		private String name;

		public void render(StringBuilder sb, Entity<?> en) {
			if (null != en) {
				EntityField ef = en.getField(name);
				if (ef == null) {
					List<Link> links = en.getLinks(name);
					if (links != null && links.size() > 0) {
						Link link = links.get(0);

						String table = link.getRelation();
						String from = "";

						if (link.get("mappedBy") != null) {
							from = link.getFrom();
						} else {
							from = link.getFrom();
						}
						String idcol = en.getIdField().getColumnName();

						sb.append(idcol).append(" not in ( select ").append(from).append(" from ").append(table).append(" )");
					}
				} else {
					sb.append(null != ef ? ef.getColumnName() : name).append(" is null");
				}

			} else {
				sb.append(name).append(" is null");
			}

			log.debugf("条件表达式[%s<%s>]: %s", en == null ? "" : (en.getType().getSimpleName()), en == null ? "" : en.getTableName(), sb);
		}
	}

	private static class NotNullExpression implements Expression {
		NotNullExpression(String propertyName) {
			this.name = propertyName;
			int dot = name.indexOf(".");
			if (dot > 0) {
				name = name.substring(0, dot);
			}
		}

		private String name;

		public void render(StringBuilder sb, Entity<?> en) {
			String subSelect = null;
			if (null != en) {
				EntityField ef = en.getField(name);
				if (ef == null) {
					subSelect = buildSubSelect(en, name);
					sb.append(subSelect);
				} else
					sb.append(null != ef ? ef.getColumnName() : name);

			} else
				sb.append(name);

			sb.append(" is not null");

			if (subSelect != null) {
				sb.append(" )");
			}

			log.debugf("条件表达式[%s<%s>]: %s", en == null ? "" : (en.getType().getSimpleName()), en == null ? "" : en.getTableName(), sb);
		}
	}

	private static class NotExpression implements Expression {
		NotExpression(Expression exp) {
			this.exp = exp;
		}

		private Expression exp;

		public void render(StringBuilder sb, Entity<?> en) {
			sb.append(" not (");
			exp.render(sb, en);
			sb.append(")");

			log.debugf("条件表达式[%s<%s>]: %s", en == null ? "" : (en.getType().getSimpleName()), en == null ? "" : en.getTableName(), sb);
		}
	}

	private static class ConditionExpression implements Expression {
		ConditionExpression(Condition con) {
			this.con = con;
		}

		private Condition con;

		public void render(StringBuilder sb, Entity<?> en) {
			sb.append(con.toSql(en));

			log.debugf("条件表达式[%s<%s>]: %s", en == null ? "" : (en.getType().getSimpleName()), en == null ? "" : en.getTableName(), sb);
		}
	}

	private static class Exp implements Expression {

		Exp(String name, String op, Object value) {
			this.name = name;
			this.op = Strings.trim(op);
			this.value = value;
		}

		private String name;

		private String op;

		private Object value;

		public void render(final StringBuilder sb, Entity<?> en) {
			if (Str.isEmpty(name)) {
				return;
			}

			if (name.endsWith(".id")) {
				name = name.substring(0, name.length() - 3);
			}

			if (null != en) {
				EnColumnMappingImpl ef = (EnColumnMappingImpl) en.getField(name);
				if (ef == null) {
					buildSubSelect(sb, en, name, (null != ef ? ef.getColumnName() : name), op, value);
				} else {
					boolean isNotNeedQuote = true;
					if (ef.getField() != null) {
						Class type = ef.getField().getType();
						if (Cls.isBoolean(type)) {
							try {
								Boolean b = null;
								if (value instanceof Boolean) {
									b = (Boolean) value;
								}
								if (b != null)
									if (b) {
										value = 1;
									} else {
										value = 0;
									}
							} catch (Throwable e) {
							}
						}
						if (!Cls.isEntityType(type))
							isNotNeedQuote = Sqls.isNotNeedQuote(type);
					}

					sb.append(null != ef ? ef.getColumnName() : name).append(" ").append(op).append(" ").append(buildValue(value, isNotNeedQuote));
				}
			} else {
				sb.append(name).append(" ").append(op).append(" ").append(buildValue(value, true));
			}

			log.debugf("条件表达式[%s<%s>]: %s", en == null ? "" : (en.getType().getSimpleName()), en == null ? "" : en.getTableName(), sb);
		}
	}

	private static String buildValue(Object value, boolean isNotNeedQuote) {
		StringBuffer sb = new StringBuffer();

		if (value instanceof List) {
			List list = (List) value;
			StringBuffer sbValue = new StringBuffer();
			sbValue.append(" (");
			int size = list.size();
			if (size == 0) {
				if (isNotNeedQuote)
					sbValue.append("-1");
				else
					sbValue.append("'").append("'");

			}
			for (int i = 0; i < size; i++) {
				Object obj = list.get(i);
				if (i != 0) {
					sbValue.append(",");
				}
				if (isNotNeedQuote) {
					if (Obj.isEntity(obj))
						sbValue.append(Obj.getId(obj));
					else
						sbValue.append(obj);
				} else {
					sbValue.append("'").append(Sqls.escapeFieldValue(obj.toString())).append("'");
				}
			}
			sbValue.append(")");
			value = sbValue.toString();
			isNotNeedQuote = true;
		}

		if (value == null) {
			sb.append("NULL");
		} else {
			if (isNotNeedQuote) {
				if (Obj.isEntity(value))
					sb.append(Obj.getId(value));
				else if (Cls.isDate(value.getClass()))
					sb.append(Sqls.escapeFieldValue(value == null ? "" : Dates.formatDateTime((Date) value)));
				else
					sb.append(value);
			} else {
				sb.append("'");
				if (Obj.isEntity(value))
					sb.append(Obj.getId(value));
				else if (Cls.isDate(value.getClass()))
					sb.append(Sqls.escapeFieldValue(value == null ? "" : Dates.formatDateTime((Date) value)));
				else
					sb.append(Sqls.escapeFieldValue(value == null ? "" : value.toString()));
				sb.append("'");
			}
		}

		return sb.toString().replace("@", "@@");
	}

	private static void buildSubSelect(StringBuilder sb, Entity en, String name, String columnName, String op, Object value) {
		String fkname = name;
		String subname = null;

		int dot = name.indexOf(".");
		if (dot > 0) {
			fkname = name.substring(0, dot);
			subname = name.substring(dot + 1);
		}

		List<Link> links = en.getLinks(fkname);
		if (links != null && links.size() > 0) {
			Link link = links.get(0);

			if (link.isManyMany()) {
				String table = link.getRelation();
				String from = "";
				String to = "";

				if (link.get("mappedBy") != null) {
					from = link.getFrom();
					to = link.getTo();
				} else {
					from = link.getFrom();
					to = link.getTo();
				}

				sb.append(columnName).append(" ");
				sb.append(en.getIdField().getColumnName());
				sb.append(" in ( select ").append(from);
				sb.append(" from ").append(table);
				sb.append(" where ");
				sb.append(to).append(" ").append(op).append(" ").append(buildValue(value, true)).append(")");
			} else {
				Entity targetEntity = (Entity) link.get("linkEntity");
				EntityField subfld = targetEntity.getField(subname);

				sb.append(" ");
				sb.append(en.getField(fkname).getColumnName());
				sb.append(" in ( select ").append(targetEntity.getIdField().getColumnName());
				sb.append(" from ").append(targetEntity.getTableName());
				sb.append(" where ");
				sb.append(subfld.getColumnName()).append(" ").append(op).append(" ").append(buildValue(value, !subfld.isString())).append(")");
			}
		} else
			sb.append(columnName).append(" ").append(op).append(" ").append(buildValue(value, true));
	}

	private static String buildSubSelect(Entity en, String name) {
		List<Link> links = en.getLinks(name);
		if (links != null && links.size() > 0) {
			Link link = links.get(0);

			String table = link.getRelation();
			String from = "";
			String to = "";

			if (link.get("mappedBy") != null) {
				from = link.getFrom();
				to = link.getTo();
			} else {
				from = link.getFrom();
				to = link.getTo();
			}
			String idcol = en.getIdField().getColumnName();

			return new StringBuffer().append(idcol).append(" in ( select ").append(from).append(" from ").append(table).append(" where ").append(to).toString();
		}

		return null;
	}
}
