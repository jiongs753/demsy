package com.kmetop.demsy.actions;

import static com.kmetop.demsy.Demsy.bizManagerFactory;
import static com.kmetop.demsy.comlib.LibConst.F_ID;

import java.util.List;
import java.util.StringTokenizer;

import org.nutz.json.Json;
import org.nutz.lang.Mirror;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizConst;
import com.kmetop.demsy.biz.IBizManager;
import com.kmetop.demsy.comlib.impl.base.log.RunningLog;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ObjcetNaviNode;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.orm.expr.ExprRule;
import com.kmetop.demsy.orm.expr.ExprRuleGroup;
import com.kmetop.demsy.orm.expr.SimpleCndExpr;
import com.kmetop.demsy.orm.nutz.EnColumnMappingImpl;
import com.kmetop.demsy.orm.nutz.EnMappingImpl;

public abstract class ModuleActions implements BizConst, MvcConst {
	protected static Log log = Logs.get();

	/**
	 * 获取模块业务管理器
	 * 
	 * @param moduleID
	 *            模块ID，可以是字符串类型的模块编号或数字型的模块id。
	 * @return 返回模块业务管理器
	 * @throws DemsyException
	 */
	protected static IBizManager getBizManager(String moduleID) throws DemsyException {
		if (!Str.isEmpty(moduleID)) {
			return bizManagerFactory.getManager(moduleID);
		} else {
			throw new DemsyException("URL路径中未指定模块ID[%s]", Demsy.me().actionContext().getPath());
		}
	}

	protected CndExpr getBizCndExpr(IOrm orm, Class classOfEntity) {
		log.trace("计算条件表达式...");

		Demsy ctx = Demsy.me();
		CndExpr expr = null;

		String fixedNaviRuleString = ctx.param("fixedNaviRules", String.class, null);
		expr = CndExpr.make(fixedNaviRuleString);

		String naviRuleString = ctx.param("naviRules", String.class, null);
		CndExpr naviExpr = CndExpr.make(naviRuleString);
		if (naviExpr instanceof SimpleCndExpr) {
			SimpleCndExpr simpleExpr = (SimpleCndExpr) naviExpr;
			String prop = simpleExpr.getProp();
			String nextProp = F_ID;
			int dot = prop.indexOf(".");
			if (dot > -1) {
				nextProp = prop.substring(dot + 1);
				prop = prop.substring(0, dot);
			}
			try {
				Class refType = Mirror.me(classOfEntity).getField(prop).getType();
				if (refType.equals(classOfEntity)) {
					naviExpr = Expr.or(naviExpr, Expr.eq(nextProp, simpleExpr.getValue()));
					naviExpr.addAsc(prop);
				}
			} catch (NoSuchFieldException e) {
			}
		}
		if (expr == null) {
			expr = naviExpr;
		} else {
			expr = expr.and(naviExpr);
		}

		boolean isSearch = ctx.param("_search", Boolean.class, false);
		if (isSearch) {
			String filters = ctx.param("filters", String.class, null);

			log.tracef("计算过滤条件：%s", filters);

			if (filters != null) {
				ExprRuleGroup rg = Json.fromJson(ExprRuleGroup.class, filters);
				this.evalBizRuleGroup(orm, classOfEntity, rg);

				if (expr == null)
					expr = rg.toExpr();
				else
					expr = expr.and(rg.toExpr());
			}
		}

		log.tracef("计算条件表达式: 结束. [%s]", expr);

		return expr;
	}

	/**
	 * 从HttpServertRequest中获取条件条件并构建成条件表达式
	 * 
	 * @param bm
	 *            业务管理器
	 * @param classOfEntity
	 * @return 查询条件表达式
	 */
	protected CndExpr getBizCndOrderExpr(IOrm orm, Class classOfEntity) {
		log.trace("计算条件排序表达式...");

		CndExpr expr = this.getBizCndExpr(orm, classOfEntity);

		expr = makeOrderExpr(orm, classOfEntity, expr);

		log.tracef("计算条件排序表达式: 结束. [%s]", expr);

		return expr;
	}

	protected CndExpr makeOrderExpr(IOrm orm, Class classOfEntity, CndExpr expr) {
		log.trace("计算排序表达式...");

		Demsy ctx = Demsy.me();

		String dind = ctx.param("sidx", String.class, "orderby,id");
		String dord = ctx.param("sord", String.class, "asc,desc");
		if (RunningLog.class.isAssignableFrom(classOfEntity) && dind.equals("orderby,id")) {
			dord = "asc,asc";
		}
		String[] sidxs = Str.toArray(dind, ",");
		String[] sords = Str.toArray(dord, ",");
		for (int i = 0; i < sidxs.length; i++) {
			String sidx = sidxs[i];
			String sord = sords[i];

			if (!Cls.hasField(classOfEntity, sidx))
				continue;

			int dot = sidx.indexOf('.');
			if (dot > -1) {
				sidx = sidx.substring(0, dot);
			}
			if (sidx.length() > 0) {
				if (sord.toLowerCase().endsWith("desc")) {
					if (expr == null)
						expr = CndExpr.desc(sidx);
					else
						expr.addDesc(sidx);
				} else {
					if (expr == null)
						expr = CndExpr.asc(sidx);
					else
						expr.addAsc(sidx);
				}
			}
		}

		log.tracef("计算排序表达式: 结束. [%s]", expr);

		return expr;
	}

	protected CndExpr getBizCndPageExpr(IOrm orm, Class classOfEntity) {

		Demsy ctx = Demsy.me();
		CndExpr expr = this.getBizCndOrderExpr(orm, classOfEntity);

		int rows = ctx.param("rows", Integer.class, 20);
		int page = ctx.param("page", Integer.class, 1);
		if (expr == null)
			expr = CndExpr.page(page, rows);
		else
			expr.setPager(page, rows);

		log.tracef("计算分页表达式：[rows=%s,page=%s]", rows, page);

		return expr;
	}

	protected void evalBizRuleGroup(IOrm orm, Class classOfEntity, ExprRuleGroup group) {
		ExprRule[] rules = group.getRules();
		if (rules != null) {
			for (int i = 0; i < rules.length; i++) {
				ExprRule rule = rules[i];
				rules[i] = this.evalBizRefRule(orm, classOfEntity, rule);
			}
		}
		ExprRuleGroup[] groups = group.getGroups();
		if (groups != null) {
			for (ExprRuleGroup g : groups) {
				evalBizRuleGroup(orm, classOfEntity, g);
			}
		}
	}

	/**
	 * 
	 * 计算外键字段嵌套路径表达式规则
	 * <p>
	 * 如：sex.name=男
	 * 
	 * @param orm
	 *            ORM 用于查询业务实体数据
	 * @param classOfEntity
	 *            业务实体类
	 * @param rule
	 *            原始表达式规则
	 * @return 计算后的表达式规则
	 */
	protected ExprRule evalBizRefRule(IOrm orm, Class classOfEntity, ExprRule rule) {
		// 检查是否是一个实体类
		if (!Cls.isEntityType(classOfEntity)) {
			if (log.isTraceEnabled())
				log.tracef("计算表达式规则：<%s>不是实体类", classOfEntity.getSimpleName());

			return rule;
		}

		// 检查实体是否存在
		EnMappingImpl em = (EnMappingImpl) orm.getEnMapping(classOfEntity);
		if (em == null) {
			if (log.isTraceEnabled())
				log.tracef("计算表达式规则：实体<%s>映射不存在", classOfEntity.getSimpleName());

			return rule;
		}

		// 计算字段路径
		String path = rule.getField();
		String field;
		String nextPath = null;
		int dot = path.indexOf(".");
		if (dot > -1) {
			field = path.substring(0, dot);
			nextPath = path.substring(dot + 1);
		} else
			field = path;

		// 检查字段映射是否存在
		EnColumnMappingImpl cm = (EnColumnMappingImpl) em.getField(field);
		if (cm == null || cm.getField() == null) {
			if (log.isTraceEnabled())
				log.tracef("计算表达式规则：实体<%s>字段<%s>不存在", classOfEntity.getSimpleName(), field);

			return rule;
		}

		// 检查字段类型是否为外键引用
		Class type = cm.getField().getType();
		if (!Cls.isEntityType(type)) {// 不是外键
			return rule;
		} else {// 是外键
			String op = rule.getOp();

			if (op.equals("nu") || op.toUpperCase().equals("IS NULL") || op.equals("nn") || op.toUpperCase().equals("IS NOT NULL")) {
				rule.setField(field);
				return rule;
			}
			if (Str.isEmpty((String) rule.getData())) {
				rule.setField(field);
				rule.setOp("nu");
				return rule;
			}

			if (Str.isEmpty(nextPath)) {
				nextPath = "name";
			}
			if (log.isTraceEnabled())
				log.tracef("计算表达式规则：实体<%s>字段<%s>类型<%s>是实体类，计算嵌套路径<%s>查询规则...", classOfEntity.getSimpleName(), field, type.getSimpleName(), nextPath);
		}

		ExprRule nextRule = this.evalBizRefRule(orm, type, new ExprRule(nextPath, rule.getOp(), rule.getData()));

		if (log.isTraceEnabled())
			log.tracef("计算表达式规则：查询引用实体<%s>数据[%s]", type.getSimpleName(), nextRule);

		List list = orm.query(type, nextRule.toExpr());

		ExprRule ret;
		if (list == null || list.size() == 0) {
			ret = new ExprRule(field, "in", "" + Long.MIN_VALUE);
		} else {
			ret = new ExprRule(field, "in", Str.join(list, F_ID));
		}

		if (log.isTraceEnabled())
			log.tracef("计算表达式规则：实体<%s>返回[%s]", classOfEntity.getSimpleName(), ret);

		return ret;
	}

	protected Object loadBizData(ObjcetNaviNode dataNode, IBizManager bizManager, Class bizClass, String actionMode, String dataID) throws DemsyException {
		Mirror mirror = Mirror.me(bizClass);
		Object data = null;
		if (!Str.isEmpty(dataID)) {
			Long id = 0l;
			try {
				id = Long.parseLong(dataID);
			} catch (NumberFormatException e) {
				throw new DemsyException("非法数据ID! [%s] %s", dataID, e);
			}
			data = bizManager.load(id, actionMode);
		}
		return dataNode.inject(mirror, data, null);
	}

	protected static String[] parseParam(String param) {
		int size = 2;
		int count = 0;
		String[] params = new String[size];

		if (param != null) {
			// 用空串填充未提供的参数路径
			if (param.startsWith(":")) {
				params[count++] = "";
			}

			StringTokenizer token = new StringTokenizer(param, ":");
			while (token.hasMoreElements() && count < size) {
				params[count++] = (String) token.nextElement();
			}

			// 用空串填充未提供的参数路径
			if (param.endsWith(":")) {
				while (count < size) {
					params[count++] = "";
				}
			}
		}

		return params;
	}

	protected static boolean isAjaxParam(String param) {
		if (Str.isEmpty(param)) {
			return false;
		}
		return param.startsWith(URL_PREFIX_AJAX);
	}
}
