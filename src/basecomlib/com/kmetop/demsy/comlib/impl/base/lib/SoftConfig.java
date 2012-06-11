package com.kmetop.demsy.comlib.impl.base.lib;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_CONFIG;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_CONFIG;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.entity.ISoftConfig;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "系统参数设置", code = BIZSYS_ADMIN_CONFIG, catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_CONFIG, buildin = true//
, actions = {
		@BzAct(name = "新增", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.lib.ClearSoftConfigCache")//
		,
		@BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin = "com.kmetop.demsy.plugins.lib.ClearSoftConfigCache") //
		,
		@BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d", plugin = "com.kmetop.demsy.plugins.lib.ClearSoftConfigCache") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "parent", mode = "c:E e:E") //
		, @BzFld(name = "配置项名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "配置项编号", property = "code", mode = "c:M e:M") //
		, @BzFld(name = "配置项内容", property = "value", mode = "c:M e:M", privacy = true) //
		, @BzFld(name = "配置项说明", property = "desc") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
}) }// end groups
, jsonData = "SoftConfig.data.js"//
)
public class SoftConfig extends BizComponent implements ISoftConfig {
	@Column(length = 255)
	private String value;

	@ManyToOne
	@BzFld(name = "配置项分组")
	private SoftConfig parent;

	@OneToMany(mappedBy = "parent")
	protected List<SoftConfig> children;

	public List<SoftConfig> getChildren() {
		return children;
	}

	public void setChildren(List<SoftConfig> children) {
		this.children = children;
	}

	public SoftConfig getParent() {
		return parent;
	}

	public void setParent(SoftConfig parent) {
		this.parent = parent;
	}

	public String getValue() {
		if (Str.isEmpty(value)) {
			return super.get("value");
		}
		return value;
	}

	public void setValue(String value) {
		if (value == null)
			value = "";

		if (value.length() > 127) {
			super.set("value", value);
			this.value = null;
		} else {
			super.set("value", null);
			this.value = value;
		}
	}

	@Override
	public String getKey() {
		return getCode();
	}
}
