package com.kmetop.demsy.comlib.biz.field;

import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_MODULE;

import java.util.List;

import javax.persistence.Column;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;

/**
 * 用于字段类型为数据集的情况
 * 
 * @author Administrator
 * 
 */
@BzFld(precision = 2000, uiTemplate = "ui.widget.field.Composite")
public class Dataset extends JsonField<Dataset> {
	public Dataset() {
		this("");
	}

	public Dataset(String str) {
		super(str);
	}

	@BzFld(name = "继承选项", order = 1, options = "0:不继承,1:继承数据", desc = "是否继承上级板块数据源查询结果集")
	private boolean inherit;

	@BzFld(name = "数据模块", refrenceSystem = BIZSYS_ADMIN_MODULE, order = 2, options = "['type eq 2']")
	private String moduleGuid;// 功能模块

	// @BzFld(name = "业务模块", refrenceSystem = BIZSYS_SOFT_MODULE, order = 1,
	// options = "['type eq 2']")
	// private String moduleGuid;// 功能模块

	@BzFld(name = "数据分类", order = 3, combobox = true, uiTemplate = "ui.widget.field.ComboboxSys", cascadeMode = "moduleGuid:*:E")
	private String rules;// 查询规则

	@BzFld(name = "动态分类", order = 4, options = "0:不支持,1:路径参数", desc = "是否支持URL路径参数作为动态数据分类条件？只用于数据分类中指定了外键字段的情况。")
	private boolean dynamic;

	private String rules2;// rules label

	/**
	 * 逗号分隔的条件子句。
	 * <p>
	 * 语法示例：keywords like 吃
	 * <p>
	 * op 可以是：
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
	 */
	@BzFld(name = "查询条件", order = 5)
	private String cnds;

	/**
	 * 逗号分隔的动态查询条件：即请求参数中的关键字(...&_k=XXX&...)将替换动态条件中的%s部分，如果动态条件是逗号分隔的多条件子句，
	 * 则请求参数中的参数对也将是多个，如：...?_k=v&_k1=v1&_k2=v2&...
	 * <p>
	 * 语法：keywords like %s, field1 eq %s, ...
	 */
	@BzFld(name = "动态条件", order = 6)
	private String dcnds;

	@BzFld(name = "分页大小", order = 7, uiTemplate = "ui.widget.field.Spinner")
	private int pageSize;

	// 排序语法，如： order ASC, id DESC
	@BzFld(name = "排序字段", order = 8)
	private String orderBy;

	/**
	 * 逗号分隔的字段列表，只查询指定的字段，以便提高查询效率，不指定则查询所有字段。
	 */
	@Column(length = 128)
	@BzFld(name = "查询字段", order = 9, desc = "只查询指定的字段，以便提高查询效率")
	private String fieldRule;

	@BzFld(name = "分组字段", order = 10)
	private String groupBy;// 分组字段

	@Override
	protected void init(Dataset obj) {
		if (obj != null) {
			this.fieldRule = obj.fieldRule;
			this.groupBy = obj.groupBy;
			this.moduleGuid = obj.moduleGuid;
			this.rules = obj.rules;
			this.rules2 = obj.rules2;
			this.cnds = obj.cnds;
			this.pageSize = obj.pageSize;
			this.orderBy = obj.orderBy;
			this.dynamic = obj.dynamic;
			this.inherit = obj.inherit;
			this.dcnds = obj.dcnds;
		}
	}

	public String getRulesUrl() {
		if (Str.isEmpty(moduleGuid))
			return "";

		return MvcUtil.contextPath(MvcConst.URL_BZSYS_COMB_CATALOG_EXPR, moduleGuid + ":") + "?gridColumns=3&idField=" + LibConst.F_GUID;
	}

	public IModule getModule() {
		IModule module;
		try {
			module = Demsy.moduleEngine.getModule(Long.parseLong(moduleGuid));
		} catch (Throwable e) {
			module = Demsy.moduleEngine.getModule(moduleGuid);
		}

		return module;
	}

	public List<String> getExprs() {
		List<String> ret = Str.toList(rules, ",;");
		ret.addAll(Str.toList(cnds, ",;"));

		// 解析动态条件
		List<String> dlist = Str.toList(dcnds, ",;");
		int len = dlist.size();
		if (len > 0) {
			Demsy ctx = Demsy.me();
			if (ctx != null) {
				String k = ctx.param("_k", String.class, "");
				if (Str.hasContent(k))
					ret.add(String.format(dlist.get(0), k));
				for (int i = 1; i < len; i++) {
					k = ctx.param("_k" + i, String.class, "");
					if (Str.hasContent(k))
						ret.add(String.format(dlist.get(i), k));
				}
			}
		}

		return ret;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public String getRules2() {
		return rules2;
	}

	public void setRules2(String rules2) {
		this.rules2 = rules2;
	}

	public String getFieldRule() {
		return fieldRule;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setFieldRule(String fieldRule) {
		this.fieldRule = fieldRule;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getModuleGuid() {
		return moduleGuid;
	}

	public void setModuleGuid(String moduleGuid) {
		this.moduleGuid = moduleGuid;
	}

	public boolean isInherit() {
		return inherit;
	}

	public void setInherit(boolean inherit) {
		this.inherit = inherit;
	}

	public String getCnds() {
		return cnds;
	}

	public void setCnds(String customRules) {
		this.cnds = customRules;
	}

	public String getDcnds() {
		return dcnds;
	}

	public void setDcnds(String keyfield) {
		this.dcnds = keyfield;
	}

}
