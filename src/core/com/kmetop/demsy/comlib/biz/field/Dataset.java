package com.kmetop.demsy.comlib.biz.field;

import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_MODULE;

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

	@BzFld(name = "业务模块", refrenceSystem = BIZSYS_ADMIN_MODULE, order = 1, options = "['type eq 2']")
	private String moduleGuid;// 功能模块

	// @BzFld(name = "业务模块", refrenceSystem = BIZSYS_SOFT_MODULE, order = 1,
	// options = "['type eq 2']")
	// private String moduleGuid;// 功能模块

	@Column(length = 512)
	@BzFld(name = "查询条件", order = 2, combobox = true, uiTemplate = "ui.widget.field.ComboboxSys", cascadeMode = "moduleGuid:*:E")
	private String rules;// 查询规则

	private String rules2;// rules label

	@BzFld(name = "分页大小", order = 3, cascadeMode = "moduleGuid:*:E", uiTemplate = "ui.widget.field.Spinner")
	private int pageSize;

	@Column(length = 128)
	@BzFld(name = "数据排序", order = 5, cascadeMode = "moduleGuid:*:E")
	private String orderBy;// 排序规则，如： order ASC, id DESC

	@Column(length = 128)
	@BzFld(name = "查询字段", order = 6, cascadeMode = "moduleGuid:*:E")
	private String fieldRule;

	@Column(length = 128)
	@BzFld(name = "分组字段", order = 7, cascadeMode = "moduleGuid:*:E")
	private String groupBy;// 分组字段

	@BzFld(name = "动态数据", order = 8, options = "0:固定,1:动态参数")
	private boolean dynamic;

	@BzFld(name = "上级板块数据", order = 9, options = "0:不继承,1:继承")
	private boolean inherit;

	@Override
	protected void init(Dataset obj) {
		if (obj != null) {
			this.fieldRule = obj.fieldRule;
			this.groupBy = obj.groupBy;
			this.moduleGuid = obj.moduleGuid;
			this.rules = obj.rules;
			this.rules2 = obj.rules2;
			this.pageSize = obj.pageSize;
			this.orderBy = obj.orderBy;
			this.dynamic = obj.dynamic;
			this.inherit = obj.inherit;
		}
	}

	public String getRulesUrl() {
		if (Str.isEmpty(moduleGuid))
			return "";

		return MvcUtil.contextPath(MvcConst.URL_BZSYS_COMB_EXPR, moduleGuid + ":") + "?gridColumns=3&idField=" + LibConst.F_GUID;
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

	public IModule getModule() {
		IModule module;
		try {
			module = Demsy.moduleEngine.getModule(Long.parseLong(moduleGuid));
		} catch (Throwable e) {
			module = Demsy.moduleEngine.getModule(moduleGuid);
		}

		return module;
	}

	public boolean isInherit() {
		return inherit;
	}

	public void setInherit(boolean inherit) {
		this.inherit = inherit;
	}

}
